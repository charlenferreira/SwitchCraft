package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.KeycapProfile
import com.example.model.KeyboardThemePalette
import com.example.model.RgbEffect
import com.example.model.SwitchSoundProfile
import com.example.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "switchcraft_preferences")

data class KeyboardSettings(
    val paletteId: String = KeyboardThemePalette.PRETO_AMOLED.id,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = false,
    val keycapProfile: KeycapProfile = KeycapProfile.BRAMS,
    val springPhysics: Float = 0.65f,
    val uppercaseOnKeys: Boolean = false,
    val blankKeycaps: Boolean = false,
    val numberRow: Boolean = false,
    val keyPreview: Boolean = true,
    val showSecondarySymbols: Boolean = true,
    val keyboardHeightScale: Float = 1.0f,
    val bottomPaddingDp: Int = 4,
    val keySpacingDp: Int = 4,
    val rowSpacingDp: Int = 4,
    val cornerRadiusPercent: Float = 0.35f,
    val rgbEffect: RgbEffect = RgbEffect.ONDA,
    val rgbBrightness: Float = 0.85f,
    val functionKeyTint: Float = 0.70f,
    val rgbAccentColorHex: Long = 0xFF00E5FF,
    val soundProfile: SwitchSoundProfile = SwitchSoundProfile.GRAVE,
    val soundVolume: Float = 0.85f,
    val muteInSilentMode: Boolean = true,
    val hapticIntensity: Float = 0.60f,
    val spacebarHapticIntensity: Float = 0.85f,
    val voiceSilenceTimeoutSec: Float = 1.5f,
    val physicalLayout: String = "ABNT2",
    val oneHandedMode: String = "OFF",
    val incognitoMode: Boolean = false,
    val autoCapitalize: Boolean = true,
    val doubleSpacePeriod: Boolean = true,
    val swipeTyping: Boolean = true
)

class KeyboardPreferences(private val context: Context) {

    private object Keys {
        val PALETTE_ID = stringPreferencesKey("palette_id")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val KEYCAP_PROFILE = stringPreferencesKey("keycap_profile")
        val SPRING_PHYSICS = floatPreferencesKey("spring_physics")
        val UPPERCASE_ON_KEYS = booleanPreferencesKey("uppercase_on_keys")
        val BLANK_KEYCAPS = booleanPreferencesKey("blank_keycaps")
        val NUMBER_ROW = booleanPreferencesKey("number_row")
        val KEY_PREVIEW = booleanPreferencesKey("key_preview")
        val SHOW_SECONDARY_SYMBOLS = booleanPreferencesKey("show_secondary_symbols")
        val KEYBOARD_HEIGHT_SCALE = floatPreferencesKey("keyboard_height_scale")
        val BOTTOM_PADDING_DP = intPreferencesKey("bottom_padding_dp")
        val KEY_SPACING_DP = intPreferencesKey("key_spacing_dp")
        val ROW_SPACING_DP = intPreferencesKey("row_spacing_dp")
        val CORNER_RADIUS_PERCENT = floatPreferencesKey("corner_radius_percent")
        val RGB_EFFECT = stringPreferencesKey("rgb_effect")
        val RGB_BRIGHTNESS = floatPreferencesKey("rgb_brightness")
        val FUNCTION_KEY_TINT = floatPreferencesKey("function_key_tint")
        val RGB_ACCENT_COLOR = longPreferencesKey("rgb_accent_color")
        val SOUND_PROFILE = stringPreferencesKey("sound_profile")
        val SOUND_VOLUME = floatPreferencesKey("sound_volume")
        val MUTE_IN_SILENT_MODE = booleanPreferencesKey("mute_in_silent_mode")
        val HAPTIC_INTENSITY = floatPreferencesKey("haptic_intensity")
        val SPACEBAR_HAPTIC_INTENSITY = floatPreferencesKey("spacebar_haptic_intensity")
        val VOICE_SILENCE_TIMEOUT = floatPreferencesKey("voice_silence_timeout")
        val PHYSICAL_LAYOUT = stringPreferencesKey("physical_layout")
        val ONE_HANDED_MODE = stringPreferencesKey("one_handed_mode")
        val INCOGNITO_MODE = booleanPreferencesKey("incognito_mode")
        val AUTO_CAPITALIZE = booleanPreferencesKey("auto_capitalize")
        val DOUBLE_SPACE_PERIOD = booleanPreferencesKey("double_space_period")
        val SWIPE_TYPING = booleanPreferencesKey("swipe_typing")
    }

    val settingsFlow: Flow<KeyboardSettings> = context.dataStore.data.map { pref ->
        KeyboardSettings(
            paletteId = pref[Keys.PALETTE_ID] ?: KeyboardThemePalette.PRETO_AMOLED.id,
            themeMode = ThemeMode.valueOf(pref[Keys.THEME_MODE] ?: ThemeMode.SYSTEM.name),
            dynamicColor = pref[Keys.DYNAMIC_COLOR] ?: false,
            keycapProfile = KeycapProfile.fromName(pref[Keys.KEYCAP_PROFILE]),
            springPhysics = pref[Keys.SPRING_PHYSICS] ?: 0.65f,
            uppercaseOnKeys = pref[Keys.UPPERCASE_ON_KEYS] ?: false,
            blankKeycaps = pref[Keys.BLANK_KEYCAPS] ?: false,
            numberRow = pref[Keys.NUMBER_ROW] ?: false,
            keyPreview = pref[Keys.KEY_PREVIEW] ?: true,
            showSecondarySymbols = pref[Keys.SHOW_SECONDARY_SYMBOLS] ?: true,
            keyboardHeightScale = pref[Keys.KEYBOARD_HEIGHT_SCALE] ?: 1.0f,
            bottomPaddingDp = pref[Keys.BOTTOM_PADDING_DP] ?: 4,
            keySpacingDp = pref[Keys.KEY_SPACING_DP] ?: 4,
            rowSpacingDp = pref[Keys.ROW_SPACING_DP] ?: 4,
            cornerRadiusPercent = pref[Keys.CORNER_RADIUS_PERCENT] ?: 0.35f,
            rgbEffect = RgbEffect.fromName(pref[Keys.RGB_EFFECT]),
            rgbBrightness = pref[Keys.RGB_BRIGHTNESS] ?: 0.85f,
            functionKeyTint = pref[Keys.FUNCTION_KEY_TINT] ?: 0.70f,
            rgbAccentColorHex = pref[Keys.RGB_ACCENT_COLOR] ?: 0xFF00E5FF,
            soundProfile = SwitchSoundProfile.fromName(pref[Keys.SOUND_PROFILE]),
            soundVolume = pref[Keys.SOUND_VOLUME] ?: 0.85f,
            muteInSilentMode = pref[Keys.MUTE_IN_SILENT_MODE] ?: true,
            hapticIntensity = pref[Keys.HAPTIC_INTENSITY] ?: 0.60f,
            spacebarHapticIntensity = pref[Keys.SPACEBAR_HAPTIC_INTENSITY] ?: 0.85f,
            voiceSilenceTimeoutSec = pref[Keys.VOICE_SILENCE_TIMEOUT] ?: 1.5f,
            physicalLayout = pref[Keys.PHYSICAL_LAYOUT] ?: "ABNT2",
            oneHandedMode = pref[Keys.ONE_HANDED_MODE] ?: "OFF",
            incognitoMode = pref[Keys.INCOGNITO_MODE] ?: false,
            autoCapitalize = pref[Keys.AUTO_CAPITALIZE] ?: true,
            doubleSpacePeriod = pref[Keys.DOUBLE_SPACE_PERIOD] ?: true,
            swipeTyping = pref[Keys.SWIPE_TYPING] ?: true
        )
    }

    suspend fun updatePalette(paletteId: String) {
        context.dataStore.edit { it[Keys.PALETTE_ID] = paletteId }
    }

    suspend fun updateThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun updateDynamicColor(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DYNAMIC_COLOR] = enabled }
    }

    suspend fun updateKeycapProfile(profile: KeycapProfile) {
        context.dataStore.edit { it[Keys.KEYCAP_PROFILE] = profile.name }
    }

    suspend fun updateSpringPhysics(value: Float) {
        context.dataStore.edit { it[Keys.SPRING_PHYSICS] = value.coerceIn(0f, 1f) }
    }

    suspend fun updateUppercaseOnKeys(enabled: Boolean) {
        context.dataStore.edit { it[Keys.UPPERCASE_ON_KEYS] = enabled }
    }

    suspend fun updateBlankKeycaps(enabled: Boolean) {
        context.dataStore.edit { it[Keys.BLANK_KEYCAPS] = enabled }
    }

    suspend fun updateNumberRow(enabled: Boolean) {
        context.dataStore.edit { it[Keys.NUMBER_ROW] = enabled }
    }

    suspend fun updateKeyPreview(enabled: Boolean) {
        context.dataStore.edit { it[Keys.KEY_PREVIEW] = enabled }
    }

    suspend fun updateShowSecondarySymbols(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_SECONDARY_SYMBOLS] = enabled }
    }

    suspend fun updateKeyboardHeightScale(scale: Float) {
        context.dataStore.edit { it[Keys.KEYBOARD_HEIGHT_SCALE] = scale.coerceIn(0.8f, 1.3f) }
    }

    suspend fun updateBottomPadding(dp: Int) {
        context.dataStore.edit { it[Keys.BOTTOM_PADDING_DP] = dp.coerceIn(0, 40) }
    }

    suspend fun updateKeySpacing(dp: Int) {
        context.dataStore.edit { it[Keys.KEY_SPACING_DP] = dp.coerceIn(0, 8) }
    }

    suspend fun updateRowSpacing(dp: Int) {
        context.dataStore.edit { it[Keys.ROW_SPACING_DP] = dp.coerceIn(0, 8) }
    }

    suspend fun updateCornerRadius(percent: Float) {
        context.dataStore.edit { it[Keys.CORNER_RADIUS_PERCENT] = percent.coerceIn(0f, 1f) }
    }

    suspend fun updateRgbEffect(effect: RgbEffect) {
        context.dataStore.edit { it[Keys.RGB_EFFECT] = effect.name }
    }

    suspend fun updateRgbBrightness(value: Float) {
        context.dataStore.edit { it[Keys.RGB_BRIGHTNESS] = value.coerceIn(0f, 1f) }
    }

    suspend fun updateFunctionKeyTint(value: Float) {
        context.dataStore.edit { it[Keys.FUNCTION_KEY_TINT] = value.coerceIn(0f, 1f) }
    }

    suspend fun updateRgbAccentColor(colorHex: Long) {
        context.dataStore.edit { it[Keys.RGB_ACCENT_COLOR] = colorHex }
    }

    suspend fun updateSoundProfile(profile: SwitchSoundProfile) {
        context.dataStore.edit { it[Keys.SOUND_PROFILE] = profile.name }
    }

    suspend fun updateSoundVolume(vol: Float) {
        context.dataStore.edit { it[Keys.SOUND_VOLUME] = vol.coerceIn(0f, 1f) }
    }

    suspend fun updateMuteInSilentMode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.MUTE_IN_SILENT_MODE] = enabled }
    }

    suspend fun updateHapticIntensity(value: Float) {
        context.dataStore.edit { it[Keys.HAPTIC_INTENSITY] = value.coerceIn(0f, 1f) }
    }

    suspend fun updateSpacebarHapticIntensity(value: Float) {
        context.dataStore.edit { it[Keys.SPACEBAR_HAPTIC_INTENSITY] = value.coerceIn(0f, 1f) }
    }

    suspend fun updateVoiceSilenceTimeout(timeoutSec: Float) {
        context.dataStore.edit { it[Keys.VOICE_SILENCE_TIMEOUT] = timeoutSec.coerceIn(1.0f, 5.0f) }
    }

    suspend fun updatePhysicalLayout(layout: String) {
        context.dataStore.edit { it[Keys.PHYSICAL_LAYOUT] = layout }
    }

    suspend fun updateOneHandedMode(mode: String) {
        context.dataStore.edit { it[Keys.ONE_HANDED_MODE] = mode }
    }

    suspend fun updateIncognitoMode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.INCOGNITO_MODE] = enabled }
    }
}
