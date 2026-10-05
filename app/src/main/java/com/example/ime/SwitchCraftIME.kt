package com.example.ime

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.MainActivity
import com.example.SwitchCraftApplication
import com.example.audio.MechanicalSoundEngine
import com.example.data.KeyboardPreferences
import com.example.data.StatisticsRepository
import com.example.haptics.HapticFeedbackEngine
import com.example.physical.PhysicalLayoutManager
import com.example.ui.keyboard.SwitchCraftKeyboard
import com.example.voice.VoiceBridge
import com.example.voice.VoiceInputActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SwitchCraftIME : InputMethodService(), LifecycleOwner, SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private lateinit var preferences: KeyboardPreferences
    private lateinit var statisticsRepository: StatisticsRepository
    private lateinit var soundEngine: MechanicalSoundEngine
    private lateinit var hapticEngine: HapticFeedbackEngine
    private val physicalLayoutManager = PhysicalLayoutManager()

    private var isListeningVoice = false
    private var lastSpacePressTime = 0L
    private val recentClips = mutableListOf<String>()

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        val app = applicationContext as SwitchCraftApplication
        preferences = app.preferences
        statisticsRepository = app.statisticsRepository
        soundEngine = app.soundEngine
        hapticEngine = app.hapticEngine

        serviceScope.launch {
            preferences.settingsFlow.collectLatest { settings ->
                physicalLayoutManager.activeLayout = settings.physicalLayout
            }
        }

        serviceScope.launch {
            VoiceBridge.voiceTextFlow.collectLatest { transcribedText ->
                if (transcribedText.isNotBlank()) {
                    injectVoiceTextWithSmartSpacing(transcribedText)
                }
            }
        }

        serviceScope.launch {
            VoiceBridge.isListeningFlow.collectLatest { listening ->
                isListeningVoice = listening
            }
        }

        updateClipboardHistory()
    }

    override fun onCreateInputView(): View {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@SwitchCraftIME)
            setViewTreeSavedStateRegistryOwner(this@SwitchCraftIME)

            setContent {
                SwitchCraftKeyboard(
                    preferences = preferences,
                    statisticsRepository = statisticsRepository,
                    soundEngine = soundEngine,
                    hapticEngine = hapticEngine,
                    isVoiceListening = isListeningVoice,
                    recentClips = recentClips,
                    onCommitText = { text -> handleTextCommit(text) },
                    onDeleteSurroundingText = { before, after -> currentInputConnection?.deleteSurroundingText(before, after) },
                    onSendEnter = { handleEnter() },
                    onStartVoice = { launchVoiceDictation() },
                    onSwitchKeyboard = {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.showInputMethodPicker()
                    },
                    onOpenSettings = {
                        val intent = Intent(this@SwitchCraftIME, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        }
                        startActivity(intent)
                    },
                    onMoveCursor = { direction -> handleCursorMove(direction) },
                    onSelectAllText = { currentInputConnection?.performContextMenuAction(android.R.id.selectAll) },
                    onCutText = { currentInputConnection?.performContextMenuAction(android.R.id.cut) },
                    onCopyText = { currentInputConnection?.performContextMenuAction(android.R.id.copy) },
                    onPasteText = { currentInputConnection?.performContextMenuAction(android.R.id.paste) }
                )
            }
        }

        return composeView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        updateClipboardHistory()
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        physicalLayoutManager.pendingDeadKey = null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (physicalLayoutManager.isPhysicalKeyboard(event)) {
            val handled = physicalLayoutManager.handleKeyDown(keyCode, event, currentInputConnection) { typed ->
                val isSpace = typed == "SPACE" || keyCode == KeyEvent.KEYCODE_SPACE
                soundEngine.playKeystroke(isSpacebar = isSpace)
                if (isSpace) hapticEngine.vibrateSpacebar() else hapticEngine.vibrateKey()
                serviceScope.launch {
                    statisticsRepository.recordKeystroke(typed)
                    if (isSpace) statisticsRepository.recordWordTyped()
                }
            }
            if (handled) return true
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun handleTextCommit(text: String) {
        val ic = currentInputConnection ?: return

        if (text == " ") {
            val now = System.currentTimeMillis()
            if (now - lastSpacePressTime < 450L) {
                ic.deleteSurroundingText(1, 0)
                ic.commitText(". ", 1)
                lastSpacePressTime = 0L
                return
            }
            lastSpacePressTime = now
        } else {
            lastSpacePressTime = 0L
        }

        ic.commitText(text, 1)
    }

    private fun handleEnter() {
        val ic = currentInputConnection ?: return
        val editorInfo = currentInputEditorInfo
        val imeAction = editorInfo?.imeOptions?.and(EditorInfo.IME_MASK_ACTION) ?: EditorInfo.IME_ACTION_NONE

        if (imeAction != EditorInfo.IME_ACTION_NONE && imeAction != EditorInfo.IME_ACTION_UNSPECIFIED) {
            ic.performEditorAction(imeAction)
        } else {
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
        }
    }

    private fun handleCursorMove(direction: String) {
        val ic = currentInputConnection ?: return
        val keyCode = when (direction) {
            "LEFT" -> KeyEvent.KEYCODE_DPAD_LEFT
            "RIGHT" -> KeyEvent.KEYCODE_DPAD_RIGHT
            "UP" -> KeyEvent.KEYCODE_DPAD_UP
            "DOWN" -> KeyEvent.KEYCODE_DPAD_DOWN
            else -> return
        }
        ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
        ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyCode))
    }

    private fun launchVoiceDictation() {
        val intent = Intent(this, VoiceInputActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
            putExtra(VoiceInputActivity.EXTRA_SILENCE_TIMEOUT, 1.5f)
        }
        startActivity(intent)
    }

    private fun injectVoiceTextWithSmartSpacing(text: String) {
        val ic = currentInputConnection ?: return
        val before = ic.getTextBeforeCursor(1, 0)
        val after = ic.getTextAfterCursor(1, 0)

        val prefix = if (!before.isNullOrEmpty() && !before.endsWith(" ")) " " else ""
        val suffix = if (!after.isNullOrEmpty() && !after.startsWith(" ")) " " else ""

        val formattedText = "$prefix${text.trim()}$suffix"
        ic.commitText(formattedText, 1)
        soundEngine.playKeystroke()
    }

    private fun updateClipboardHistory() {
        try {
            val clipManager = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            if (clipManager != null && clipManager.hasPrimaryClip()) {
                val clipData = clipManager.primaryClip
                if (clipData != null && clipData.itemCount > 0) {
                    val itemText = clipData.getItemAt(0)?.coerceToText(this)?.toString()
                    if (!itemText.isNullOrBlank() && !recentClips.contains(itemText)) {
                        recentClips.add(0, itemText)
                        if (recentClips.size > 15) recentClips.removeAt(recentClips.size - 1)
                    }
                }
            }
        } catch (_: Exception) {}
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        serviceScope.cancel()
        super.onDestroy()
    }
}
