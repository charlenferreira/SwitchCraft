package com.example.ui.keyboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.audio.MechanicalSoundEngine
import com.example.data.KeyboardPreferences
import com.example.data.KeyboardSettings
import com.example.data.StatisticsRepository
import com.example.haptics.HapticFeedbackEngine
import com.example.model.KeyType
import com.example.model.KeyboardKey
import com.example.model.KeyboardThemePalette
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class KeyboardSubSheet {
    NONE,
    EMOJI,
    CLIPBOARD,
    TEXT_EDIT
}

@Composable
fun SwitchCraftKeyboard(
    preferences: KeyboardPreferences,
    statisticsRepository: StatisticsRepository,
    soundEngine: MechanicalSoundEngine,
    hapticEngine: HapticFeedbackEngine,
    isVoiceListening: Boolean,
    recentClips: List<String>,
    onCommitText: (String) -> Unit,
    onDeleteSurroundingText: (Int, Int) -> Unit,
    onSendEnter: () -> Unit,
    onStartVoice: () -> Unit,
    onSwitchKeyboard: () -> Unit,
    onOpenSettings: () -> Unit,
    onMoveCursor: (String) -> Unit,
    onSelectAllText: () -> Unit,
    onCutText: () -> Unit,
    onCopyText: () -> Unit,
    onPasteText: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by preferences.settingsFlow.collectAsState(initial = KeyboardSettings())
    val palette = KeyboardThemePalette.fromId(settings.paletteId)

    LaunchedEffect(settings) {
        soundEngine.currentProfile = settings.soundProfile
        soundEngine.volume = settings.soundVolume
        soundEngine.muteInSilentMode = settings.muteInSilentMode
        hapticEngine.generalIntensity = settings.hapticIntensity
        hapticEngine.spacebarIntensity = settings.spacebarHapticIntensity
    }

    var isShiftActive by remember { mutableStateOf(false) }
    var isSymbolsMode by remember { mutableStateOf(false) }
    var isSymbolsPage2 by remember { mutableStateOf(false) }
    var currentSubSheet by remember { mutableStateOf(KeyboardSubSheet.NONE) }
    var currentLocale by remember { mutableStateOf(settings.physicalLayout) }

    var lastPressedCol by remember { mutableIntStateOf(0) }
    var lastPressedRow by remember { mutableIntStateOf(0) }
    var lastPressedTimeMs by remember { mutableLongStateOf(0L) }
    var currentTimeMs by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTimeMs = System.currentTimeMillis()
            delay(32L)
        }
    }

    val keyboardEntryOffset = remember { Animatable(100f) }
    val keycapsStaggerAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        soundEngine.playKeystroke(isSpacebar = true)
        launch {
            keyboardEntryOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
            )
        }
        delay(80L)
        soundEngine.playKeystroke(isSpacebar = false)
        keycapsStaggerAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 200)
        )
    }

    val onKeyTriggered: (KeyboardKey, Int, Int) -> Unit = { key, row, col ->
        lastPressedCol = col
        lastPressedRow = row
        lastPressedTimeMs = System.currentTimeMillis()

        val isSpace = key.type == KeyType.SPACE

        soundEngine.playKeystroke(isSpacebar = isSpace)
        if (isSpace) {
            hapticEngine.vibrateSpacebar()
        } else {
            hapticEngine.vibrateKey()
        }

        if (!settings.incognitoMode) {
            kotlinx.coroutines.GlobalScope.launch {
                statisticsRepository.recordKeystroke(key.primaryLabel)
                if (isSpace || key.type == KeyType.ENTER) {
                    statisticsRepository.recordWordTyped()
                }
            }
        }

        when (key.type) {
            KeyType.CHARACTER -> {
                val text = if (isShiftActive || settings.uppercaseOnKeys) {
                    key.primaryLabel.uppercase()
                } else {
                    key.primaryLabel.lowercase()
                }
                onCommitText(text)
                if (isShiftActive) isShiftActive = false
            }
            KeyType.SPACE -> {
                onCommitText(" ")
            }
            KeyType.BACKSPACE -> {
                onDeleteSurroundingText(1, 0)
                if (!settings.incognitoMode) {
                    kotlinx.coroutines.GlobalScope.launch {
                        statisticsRepository.recordCorrection()
                    }
                }
            }
            KeyType.ENTER -> {
                onSendEnter()
            }
            KeyType.SHIFT -> {
                isShiftActive = !isShiftActive
            }
            KeyType.SYMBOLS_TOGGLE -> {
                if (!isSymbolsMode) {
                    isSymbolsMode = true
                    isSymbolsPage2 = false
                } else {
                    isSymbolsPage2 = !isSymbolsPage2
                }
            }
            KeyType.ABC_TOGGLE -> {
                isSymbolsMode = false
                isSymbolsPage2 = false
            }
            KeyType.VOICE -> {
                onStartVoice()
            }
            KeyType.EMOJI -> {
                currentSubSheet = if (currentSubSheet == KeyboardSubSheet.EMOJI) KeyboardSubSheet.NONE else KeyboardSubSheet.EMOJI
            }
            else -> {
                onCommitText(key.primaryLabel)
            }
        }
    }

    val rows = KeyboardLayoutProvider.getAlphabetRows(currentLocale, isSymbolsMode, isSymbolsPage2)
    val numberRow = if (settings.numberRow && !isSymbolsMode) KeyboardLayoutProvider.getNumberRow() else null

    val horizontalAlignment = when (settings.oneHandedMode) {
        "LEFT" -> Alignment.Start
        "RIGHT" -> Alignment.End
        else -> Alignment.CenterHorizontally
    }
    val keyboardWidthFraction = if (settings.oneHandedMode != "OFF") 0.82f else 1.0f

    val baseRowHeight = (46 * settings.keyboardHeightScale).dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = keyboardEntryOffset.value
                alpha = keycapsStaggerAlpha.value
            }
            .background(palette.baseBackground)
            .padding(bottom = settings.bottomPaddingDp.dp),
        horizontalAlignment = horizontalAlignment
    ) {
        KeyboardToolbar(
            palette = palette,
            isVoiceListening = isVoiceListening,
            incognitoActive = settings.incognitoMode,
            oneHandedMode = settings.oneHandedMode,
            onVoiceClick = onStartVoice,
            onEmojiClick = {
                currentSubSheet = if (currentSubSheet == KeyboardSubSheet.EMOJI) KeyboardSubSheet.NONE else KeyboardSubSheet.EMOJI
            },
            onClipboardClick = {
                currentSubSheet = if (currentSubSheet == KeyboardSubSheet.CLIPBOARD) KeyboardSubSheet.NONE else KeyboardSubSheet.CLIPBOARD
            },
            onTextEditClick = {
                currentSubSheet = if (currentSubSheet == KeyboardSubSheet.TEXT_EDIT) KeyboardSubSheet.NONE else KeyboardSubSheet.TEXT_EDIT
            },
            onOneHandedClick = {
                val nextMode = when (settings.oneHandedMode) {
                    "OFF" -> "RIGHT"
                    "RIGHT" -> "LEFT"
                    else -> "OFF"
                }
                kotlinx.coroutines.GlobalScope.launch {
                    preferences.updateOneHandedMode(nextMode)
                }
            },
            onIncognitoToggle = {
                kotlinx.coroutines.GlobalScope.launch {
                    preferences.updateIncognitoMode(!settings.incognitoMode)
                }
            },
            onLanguageClick = {
                currentLocale = if (currentLocale.startsWith("pt")) "en_US" else "pt_BR"
            },
            onSwitchKeyboardClick = onSwitchKeyboard,
            onSettingsClick = onOpenSettings
        )

        when (currentSubSheet) {
            KeyboardSubSheet.EMOJI -> {
                EmojiPickerSheet(
                    palette = palette,
                    onEmojiSelected = { emoji ->
                        soundEngine.playKeystroke()
                        hapticEngine.vibrateKey()
                        onCommitText(emoji)
                    },
                    onBackspace = { onDeleteSurroundingText(1, 0) },
                    onClose = { currentSubSheet = KeyboardSubSheet.NONE }
                )
            }
            KeyboardSubSheet.CLIPBOARD -> {
                ClipboardSheet(
                    palette = palette,
                    clips = recentClips,
                    onPasteClip = { clip ->
                        soundEngine.playKeystroke()
                        hapticEngine.vibrateKey()
                        onCommitText(clip)
                        currentSubSheet = KeyboardSubSheet.NONE
                    },
                    onClearAll = {},
                    onClose = { currentSubSheet = KeyboardSubSheet.NONE }
                )
            }
            KeyboardSubSheet.TEXT_EDIT -> {
                TextEditPadSheet(
                    palette = palette,
                    onMoveCursor = onMoveCursor,
                    onSelectAll = onSelectAllText,
                    onCut = onCutText,
                    onCopy = onCopyText,
                    onPaste = onPasteText,
                    onClose = { currentSubSheet = KeyboardSubSheet.NONE }
                )
            }
            KeyboardSubSheet.NONE -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(keyboardWidthFraction)
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(settings.rowSpacingDp.dp)
                ) {
                    if (numberRow != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(baseRowHeight * 0.85f),
                            horizontalArrangement = Arrangement.spacedBy(settings.keySpacingDp.dp)
                        ) {
                            numberRow.forEachIndexed { colIdx, key ->
                                val rgb = RgbEngine.computeKeyColor(
                                    effect = settings.rgbEffect,
                                    col = colIdx,
                                    totalCols = numberRow.size,
                                    row = 0,
                                    totalRows = rows.size + 1,
                                    elapsedTimeMs = currentTimeMs,
                                    accentColor = Color(settings.rgbAccentColorHex),
                                    lastPressedCol = lastPressedCol,
                                    lastPressedRow = lastPressedRow,
                                    timeSinceLastPressMs = currentTimeMs - lastPressedTimeMs
                                )

                                KeycapComposable(
                                    key = key,
                                    settings = settings,
                                    palette = palette,
                                    rgbColor = rgb,
                                    isShiftActive = isShiftActive,
                                    modifier = Modifier.weight(key.weight),
                                    onTap = { onKeyTriggered(key, 0, colIdx) }
                                )
                            }
                        }
                    }

                    rows.forEachIndexed { rowIdx, rowKeys ->
                        val actualRowIdx = if (numberRow != null) rowIdx + 1 else rowIdx
                        val totalRowsCount = if (numberRow != null) rows.size + 1 else rows.size

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(baseRowHeight),
                            horizontalArrangement = Arrangement.spacedBy(settings.keySpacingDp.dp)
                        ) {
                            rowKeys.forEachIndexed { colIdx, key ->
                                val rgb = RgbEngine.computeKeyColor(
                                    effect = settings.rgbEffect,
                                    col = colIdx,
                                    totalCols = rowKeys.size,
                                    row = actualRowIdx,
                                    totalRows = totalRowsCount,
                                    elapsedTimeMs = currentTimeMs,
                                    accentColor = Color(settings.rgbAccentColorHex),
                                    lastPressedCol = lastPressedCol,
                                    lastPressedRow = lastPressedRow,
                                    timeSinceLastPressMs = currentTimeMs - lastPressedTimeMs
                                )

                                KeycapComposable(
                                    key = key,
                                    settings = settings,
                                    palette = palette,
                                    rgbColor = rgb,
                                    isShiftActive = isShiftActive,
                                    modifier = Modifier.weight(key.weight),
                                    onTap = { onKeyTriggered(key, actualRowIdx, colIdx) },
                                    onLongPress = {
                                        if (key.secondaryLabel != null) {
                                            soundEngine.playKeystroke()
                                            hapticEngine.vibrateKey()
                                            onCommitText(key.secondaryLabel)
                                        } else {
                                            onKeyTriggered(key, actualRowIdx, colIdx)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
