package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.data.KeyboardSettings
import com.example.data.TypingStats
import com.example.ui.screens.AboutTab
import com.example.ui.screens.PhysicalSettingsTab
import com.example.ui.screens.RgbSettingsTab
import com.example.ui.screens.SoundSettingsTab
import com.example.ui.screens.StatsTab
import com.example.ui.screens.TypingTestTab
import com.example.ui.screens.VisualSettingsTab
import com.example.ui.theme.SwitchCraftTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var isImeEnabledState = mutableStateOf(false)
    private var isImeSelectedState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = applicationContext as SwitchCraftApplication
        val preferences = app.preferences
        val statisticsRepository = app.statisticsRepository
        val soundEngine = app.soundEngine
        val hapticEngine = app.hapticEngine

        checkImeStatus()

        setContent {
            val settings by preferences.settingsFlow.collectAsState(initial = KeyboardSettings())
            val stats by statisticsRepository.statsFlow.collectAsState(initial = TypingStats())
            val scope = rememberCoroutineScope()
            var currentNavIndex by remember { mutableIntStateOf(0) }

            SwitchCraftTheme(
                themeMode = settings.themeMode,
                dynamicColor = settings.dynamicColor
            ) {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        NavigationBar {
                            val items = listOf(
                                Triple("Digitação", Icons.Default.Keyboard, 0),
                                Triple("Switches", Icons.Default.VolumeUp, 1),
                                Triple("Visual", Icons.Default.Layers, 2),
                                Triple("RGB", Icons.Default.Flare, 3),
                                Triple("Físico", Icons.Default.Usb, 4),
                                Triple("Stats", Icons.Default.AutoGraph, 5),
                                Triple("Sobre", Icons.Default.Info, 6)
                            )

                            items.forEach { (label, icon, index) ->
                                NavigationBarItem(
                                    selected = currentNavIndex == index,
                                    onClick = { currentNavIndex = index },
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label, fontSize = 10.sp, fontFamily = FontFamily.SansSerif) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentNavIndex) {
                            0 -> TypingTestTab(
                                settings = settings,
                                isImeEnabled = isImeEnabledState.value,
                                isImeSelected = isImeSelectedState.value,
                                onEnableImeClick = {
                                    startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                                },
                                onSelectImeClick = {
                                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                                    imm?.showInputMethodPicker()
                                }
                            )
                            1 -> SoundSettingsTab(
                                settings = settings,
                                soundEngine = soundEngine,
                                hapticEngine = hapticEngine,
                                onSelectSoundProfile = { scope.launch { preferences.updateSoundProfile(it) } },
                                onVolumeChange = { scope.launch { preferences.updateSoundVolume(it) } },
                                onMuteInSilentToggle = { scope.launch { preferences.updateMuteInSilentMode(it) } },
                                onHapticIntensityChange = { scope.launch { preferences.updateHapticIntensity(it) } },
                                onSpacebarHapticChange = { scope.launch { preferences.updateSpacebarHapticIntensity(it) } }
                            )
                            2 -> VisualSettingsTab(
                                settings = settings,
                                onSelectProfile = { scope.launch { preferences.updateKeycapProfile(it) } },
                                onSpringPhysicsChange = { scope.launch { preferences.updateSpringPhysics(it) } },
                                onSelectPalette = { scope.launch { preferences.updatePalette(it) } },
                                onUppercaseToggle = { scope.launch { preferences.updateUppercaseOnKeys(it) } },
                                onBlankKeycapsToggle = { scope.launch { preferences.updateBlankKeycaps(it) } },
                                onNumberRowToggle = { scope.launch { preferences.updateNumberRow(it) } },
                                onKeyPreviewToggle = { scope.launch { preferences.updateKeyPreview(it) } },
                                onSecondarySymbolsToggle = { scope.launch { preferences.updateShowSecondarySymbols(it) } },
                                onHeightScaleChange = { scope.launch { preferences.updateKeyboardHeightScale(it) } },
                                onBottomPaddingChange = { scope.launch { preferences.updateBottomPadding(it) } },
                                onKeySpacingChange = { scope.launch { preferences.updateKeySpacing(it) } },
                                onRowSpacingChange = { scope.launch { preferences.updateRowSpacing(it) } },
                                onCornerRadiusChange = { scope.launch { preferences.updateCornerRadius(it) } }
                            )
                            3 -> RgbSettingsTab(
                                settings = settings,
                                onRgbEffectSelect = { scope.launch { preferences.updateRgbEffect(it) } },
                                onBrightnessChange = { scope.launch { preferences.updateRgbBrightness(it) } },
                                onFunctionKeyTintChange = { scope.launch { preferences.updateFunctionKeyTint(it) } },
                                onAccentColorSelect = { scope.launch { preferences.updateRgbAccentColor(it) } }
                            )
                            4 -> PhysicalSettingsTab(
                                settings = settings,
                                onSelectLayout = { scope.launch { preferences.updatePhysicalLayout(it) } }
                            )
                            5 -> StatsTab(
                                stats = stats
                            )
                            6 -> AboutTab()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkImeStatus()
    }

    private fun checkImeStatus() {
        val packageName = packageName
        val enabledImes = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_INPUT_METHODS) ?: ""
        val currentIme = Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD) ?: ""

        isImeEnabledState.value = enabledImes.contains(packageName)
        isImeSelectedState.value = currentIme.contains(packageName)
    }
}
