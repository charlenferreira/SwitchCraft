package com.example.ui.keyboard

import com.example.model.KeyType
import com.example.model.KeyboardKey

object KeyboardLayoutProvider {

    fun getNumberRow(): List<KeyboardKey> {
        return listOf(
            KeyboardKey("1", "1", "!"),
            KeyboardKey("2", "2", "@"),
            KeyboardKey("3", "3", "#"),
            KeyboardKey("4", "4", "$"),
            KeyboardKey("5", "5", "%"),
            KeyboardKey("6", "6", "&"),
            KeyboardKey("7", "7", "*"),
            KeyboardKey("8", "8", "("),
            KeyboardKey("9", "9", ")"),
            KeyboardKey("0", "0", "=")
        )
    }

    fun getAlphabetRows(locale: String, isSymbolsMode: Boolean, isSymbolsPage2: Boolean): List<List<KeyboardKey>> {
        if (isSymbolsMode) {
            return if (!isSymbolsPage2) getSymbolsPage1() else getSymbolsPage2()
        }

        val hasCedilla = locale.startsWith("pt", ignoreCase = true)
        val hasEnhe = locale.startsWith("es", ignoreCase = true)

        val row1 = listOf(
            KeyboardKey("q", "q", "1"),
            KeyboardKey("w", "w", "2"),
            KeyboardKey("e", "e", "3"),
            KeyboardKey("r", "r", "4"),
            KeyboardKey("t", "t", "5"),
            KeyboardKey("y", "y", "6"),
            KeyboardKey("u", "u", "7"),
            KeyboardKey("i", "i", "8"),
            KeyboardKey("o", "o", "9"),
            KeyboardKey("p", "p", "0")
        )

        val row2 = mutableListOf(
            KeyboardKey("a", "a", "@"),
            KeyboardKey("s", "s", "#"),
            KeyboardKey("d", "d", "$"),
            KeyboardKey("f", "f", "%"),
            KeyboardKey("g", "g", "&"),
            KeyboardKey("h", "h", "-"),
            KeyboardKey("j", "j", "+"),
            KeyboardKey("k", "k", "("),
            KeyboardKey("l", "l", ")")
        ).apply {
            if (hasCedilla) {
                add(KeyboardKey("ç", "ç", "ç"))
            } else if (hasEnhe) {
                add(KeyboardKey("ñ", "ñ", "ñ"))
            }
        }

        val row3 = mutableListOf<KeyboardKey>().apply {
            add(KeyboardKey("SHIFT", "⇧", weight = 1.35f, type = KeyType.SHIFT))
            add(KeyboardKey("z", "z", "*"))
            add(KeyboardKey("x", "x", "\""))
            add(KeyboardKey("c", "c", "'"))
            add(KeyboardKey("v", "v", ":"))
            add(KeyboardKey("b", "b", ";"))
            add(KeyboardKey("n", "n", "!"))
            add(KeyboardKey("m", "m", "?"))
            add(KeyboardKey("BACKSPACE", "⌫", weight = 1.35f, type = KeyType.BACKSPACE))
        }

        val row4 = listOf(
            KeyboardKey("?123", "?123", weight = 1.3f, type = KeyType.SYMBOLS_TOGGLE),
            KeyboardKey(",", ",", weight = 0.9f),
            KeyboardKey("SPACE", "SwitchCraft", weight = 4.0f, type = KeyType.SPACE),
            KeyboardKey(".", ".", weight = 0.9f),
            KeyboardKey("ENTER", "↵", weight = 1.3f, type = KeyType.ENTER)
        )

        return listOf(row1, row2, row3, row4)
    }

    private fun getSymbolsPage1(): List<List<KeyboardKey>> {
        val row1 = listOf(
            KeyboardKey("1", "1"), KeyboardKey("2", "2"), KeyboardKey("3", "3"), KeyboardKey("4", "4"), KeyboardKey("5", "5"),
            KeyboardKey("6", "6"), KeyboardKey("7", "7"), KeyboardKey("8", "8"), KeyboardKey("9", "9"), KeyboardKey("0", "0")
        )
        val row2 = listOf(
            KeyboardKey("@", "@"), KeyboardKey("#", "#"), KeyboardKey("$", "$"), KeyboardKey("_", "_"), KeyboardKey("&", "&"),
            KeyboardKey("-", "-"), KeyboardKey("+", "+"), KeyboardKey("(", "("), KeyboardKey(")", ")"), KeyboardKey("/", "/")
        )
        val row3 = listOf(
            KeyboardKey("=\\<", "=\\<", weight = 1.35f, type = KeyType.SYMBOLS_TOGGLE),
            KeyboardKey("*", "*"), KeyboardKey("\"", "\""), KeyboardKey("'", "'"), KeyboardKey(":", ":"),
            KeyboardKey(";", ";"), KeyboardKey("!", "!"), KeyboardKey("?", "?"),
            KeyboardKey("BACKSPACE", "⌫", weight = 1.35f, type = KeyType.BACKSPACE)
        )
        val row4 = listOf(
            KeyboardKey("ABC", "ABC", weight = 1.3f, type = KeyType.ABC_TOGGLE),
            KeyboardKey(",", ",", weight = 0.9f),
            KeyboardKey("SPACE", "Espaço", weight = 4.0f, type = KeyType.SPACE),
            KeyboardKey(".", ".", weight = 0.9f),
            KeyboardKey("ENTER", "↵", weight = 1.3f, type = KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }

    private fun getSymbolsPage2(): List<List<KeyboardKey>> {
        val row1 = listOf(
            KeyboardKey("~", "~"), KeyboardKey("`", "`"), KeyboardKey("|", "|"), KeyboardKey("•", "•"), KeyboardKey("√", "√"),
            KeyboardKey("π", "π"), KeyboardKey("÷", "÷"), KeyboardKey("×", "×"), KeyboardKey("¶", "¶"), KeyboardKey("∆", "∆")
        )
        val row2 = listOf(
            KeyboardKey("£", "£"), KeyboardKey("¢", "¢"), KeyboardKey("€", "€"), KeyboardKey("¥", "¥"), KeyboardKey("^", "^"),
            KeyboardKey("°", "°"), KeyboardKey("=", "="), KeyboardKey("{", "{"), KeyboardKey("}", "}"), KeyboardKey("\\", "\\")
        )
        val row3 = listOf(
            KeyboardKey("?123", "?123", weight = 1.35f, type = KeyType.SYMBOLS_TOGGLE),
            KeyboardKey("%", "%"), KeyboardKey("©", "©"), KeyboardKey("®", "®"), KeyboardKey("™", "™"),
            KeyboardKey("✓", "✓"), KeyboardKey("[", "["), KeyboardKey("]", "]"),
            KeyboardKey("BACKSPACE", "⌫", weight = 1.35f, type = KeyType.BACKSPACE)
        )
        val row4 = listOf(
            KeyboardKey("ABC", "ABC", weight = 1.3f, type = KeyType.ABC_TOGGLE),
            KeyboardKey("<", "<", weight = 0.9f),
            KeyboardKey("SPACE", "Espaço", weight = 4.0f, type = KeyType.SPACE),
            KeyboardKey(">", ">", weight = 0.9f),
            KeyboardKey("ENTER", "↵", weight = 1.3f, type = KeyType.ENTER)
        )
        return listOf(row1, row2, row3, row4)
    }
}
