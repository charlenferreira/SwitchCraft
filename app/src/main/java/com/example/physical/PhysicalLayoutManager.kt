package com.example.physical

import android.view.InputDevice
import android.view.KeyEvent
import android.view.inputmethod.InputConnection

class PhysicalLayoutManager {

    var activeLayout: String = "ABNT2"
    var pendingDeadKey: Char? = null

    fun isPhysicalKeyboard(event: KeyEvent): Boolean {
        val source = event.source
        return (source and InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD
    }

    fun handleKeyDown(
        keyCode: Int,
        event: KeyEvent,
        inputConnection: InputConnection?,
        onKeyHandled: (String) -> Unit
    ): Boolean {
        if (inputConnection == null) return false

        when (keyCode) {
            KeyEvent.KEYCODE_DEL -> {
                pendingDeadKey = null
                inputConnection.deleteSurroundingText(1, 0)
                onKeyHandled("BACKSPACE")
                return true
            }
            KeyEvent.KEYCODE_ENTER -> {
                flushDeadKey(inputConnection, onKeyHandled)
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
                onKeyHandled("ENTER")
                return true
            }
            KeyEvent.KEYCODE_SPACE -> {
                if (pendingDeadKey != null) {
                    val dead = pendingDeadKey!!
                    pendingDeadKey = null
                    inputConnection.commitText(dead.toString(), 1)
                    onKeyHandled(dead.toString())
                    return true
                }
                inputConnection.commitText(" ", 1)
                onKeyHandled("SPACE")
                return true
            }
        }

        return when (activeLayout) {
            "US_INTL" -> handleUsInternational(keyCode, event, inputConnection, onKeyHandled)
            "ISO_EUROPEAN" -> handleIsoEuropean(keyCode, event, inputConnection, onKeyHandled)
            else -> handleAbnt2(keyCode, event, inputConnection, onKeyHandled)
        }
    }

    private fun handleAbnt2(
        keyCode: Int,
        event: KeyEvent,
        inputConnection: InputConnection,
        onKeyHandled: (String) -> Unit
    ): Boolean {
        val isShift = event.isShiftPressed
        val isAltGr = (event.metaState and KeyEvent.META_ALT_RIGHT_ON) != 0 || (event.isAltPressed && !event.isCtrlPressed)

        if (isAltGr) {
            val altGrChar = when (keyCode) {
                KeyEvent.KEYCODE_Q -> "/"
                KeyEvent.KEYCODE_W -> "?"
                KeyEvent.KEYCODE_E -> "°"
                KeyEvent.KEYCODE_1 -> "¹"
                KeyEvent.KEYCODE_2 -> "²"
                KeyEvent.KEYCODE_3 -> "³"
                KeyEvent.KEYCODE_4 -> "£"
                KeyEvent.KEYCODE_5 -> "¢"
                KeyEvent.KEYCODE_6 -> "¬"
                KeyEvent.KEYCODE_C -> "₢"
                KeyEvent.KEYCODE_SLASH -> "°"
                else -> null
            }
            if (altGrChar != null) {
                inputConnection.commitText(altGrChar, 1)
                onKeyHandled(altGrChar)
                return true
            }
        }

        if (keyCode == KeyEvent.KEYCODE_SEMICOLON) {
            val c = if (isShift) "Ç" else "ç"
            inputConnection.commitText(c, 1)
            onKeyHandled(c)
            return true
        }

        val unicode = event.getUnicodeChar(event.metaState)
        if (unicode != 0) {
            val charTyped = unicode.toChar().toString()
            inputConnection.commitText(charTyped, 1)
            onKeyHandled(charTyped)
            return true
        }

        return false
    }

    private fun handleUsInternational(
        keyCode: Int,
        event: KeyEvent,
        inputConnection: InputConnection,
        onKeyHandled: (String) -> Unit
    ): Boolean {
        val isShift = event.isShiftPressed
        val rawChar = event.getUnicodeChar(event.metaState).toChar()

        val incomingDeadKey = when {
            keyCode == KeyEvent.KEYCODE_APOSTROPHE && !isShift -> '\''
            keyCode == KeyEvent.KEYCODE_APOSTROPHE && isShift -> '"'
            keyCode == KeyEvent.KEYCODE_GRAVE && !isShift -> '`'
            keyCode == KeyEvent.KEYCODE_GRAVE && isShift -> '~'
            keyCode == KeyEvent.KEYCODE_6 && isShift -> '^'
            rawChar in listOf('\'', '"', '`', '~', '^') -> rawChar
            else -> null
        }

        if (incomingDeadKey != null && pendingDeadKey == null) {
            pendingDeadKey = incomingDeadKey
            return true
        }

        if (pendingDeadKey != null) {
            val dead = pendingDeadKey!!
            pendingDeadKey = null

            if (incomingDeadKey == dead) {
                inputConnection.commitText(dead.toString(), 1)
                onKeyHandled(dead.toString())
                return true
            }

            val combined = combineDeadKey(dead, rawChar)
            if (combined != null) {
                inputConnection.commitText(combined.toString(), 1)
                onKeyHandled(combined.toString())
                return true
            } else {
                val fallbackText = "$dead$rawChar"
                inputConnection.commitText(fallbackText, 1)
                onKeyHandled(rawChar.toString())
                return true
            }
        }

        if (rawChar.code != 0) {
            inputConnection.commitText(rawChar.toString(), 1)
            onKeyHandled(rawChar.toString())
            return true
        }

        return false
    }

    private fun handleIsoEuropean(
        keyCode: Int,
        event: KeyEvent,
        inputConnection: InputConnection,
        onKeyHandled: (String) -> Unit
    ): Boolean {
        val isShift = event.isShiftPressed

        if (keyCode == 68 || (keyCode == KeyEvent.KEYCODE_BACKSLASH && event.scanCode == 86)) {
            val symbol = if (isShift) ">" else "<"
            inputConnection.commitText(symbol, 1)
            onKeyHandled(symbol)
            return true
        }

        val rawChar = event.getUnicodeChar(event.metaState).toChar()
        if (pendingDeadKey != null) {
            val dead = pendingDeadKey!!
            pendingDeadKey = null
            val combined = combineDeadKey(dead, rawChar) ?: "$dead$rawChar"
            inputConnection.commitText(combined.toString(), 1)
            onKeyHandled(rawChar.toString())
            return true
        }

        if (rawChar in listOf('´', '`', '¨', '^', '~')) {
            pendingDeadKey = rawChar
            return true
        }

        if (rawChar.code != 0) {
            inputConnection.commitText(rawChar.toString(), 1)
            onKeyHandled(rawChar.toString())
            return true
        }

        return false
    }

    private fun combineDeadKey(dead: Char, letter: Char): Char? {
        val isUpper = letter.isUpperCase()
        val lower = letter.lowercaseChar()

        val combinedLower = when (dead) {
            '\'', '´' -> when (lower) {
                'a' -> 'á'
                'e' -> 'é'
                'i' -> 'í'
                'o' -> 'ó'
                'u' -> 'ú'
                'c' -> 'ç'
                'y' -> 'ý'
                else -> null
            }
            '"', '¨' -> when (lower) {
                'a' -> 'ä'
                'e' -> 'ë'
                'i' -> 'ï'
                'o' -> 'ö'
                'u' -> 'ü'
                'y' -> 'ÿ'
                else -> null
            }
            '~' -> when (lower) {
                'a' -> 'ã'
                'o' -> 'õ'
                'n' -> 'ñ'
                else -> null
            }
            '^' -> when (lower) {
                'a' -> 'â'
                'e' -> 'ê'
                'i' -> 'î'
                'o' -> 'ô'
                'u' -> 'û'
                else -> null
            }
            '`' -> when (lower) {
                'a' -> 'à'
                'e' -> 'è'
                'i' -> 'ì'
                'o' -> 'ò'
                'u' -> 'ù'
                else -> null
            }
            else -> null
        }

        return if (combinedLower != null) {
            if (isUpper) combinedLower.uppercaseChar() else combinedLower
        } else null
    }

    private fun flushDeadKey(inputConnection: InputConnection, onKeyHandled: (String) -> Unit) {
        pendingDeadKey?.let {
            inputConnection.commitText(it.toString(), 1)
            onKeyHandled(it.toString())
            pendingDeadKey = null
        }
    }
}
