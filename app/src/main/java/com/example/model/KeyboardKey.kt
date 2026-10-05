package com.example.model

enum class KeyType {
    CHARACTER,
    SHIFT,
    BACKSPACE,
    ENTER,
    SPACE,
    SYMBOLS_TOGGLE,
    ABC_TOGGLE,
    VOICE,
    EMOJI,
    HIDE_KEYBOARD,
    ALT_GR,
    TAB,
    LANGUAGE
}

data class KeyboardKey(
    val primaryCode: String,
    val primaryLabel: String,
    val secondaryLabel: String? = null,
    val weight: Float = 1.0f,
    val type: KeyType = KeyType.CHARACTER,
    val isAccentKey: Boolean = false,
    val altGrChar: String? = null
)
