package com.example.model

import androidx.compose.ui.graphics.Color

enum class KeyboardThemePalette(
    val id: String,
    val displayName: String,
    val description: String,
    val baseBackground: Color,
    val keyBackground: Color,
    val keySurfaceTop: Color,
    val keyText: Color,
    val functionKeyBackground: Color,
    val functionKeyText: Color,
    val accentColor: Color,
    val previewBorderColor: Color
) {
    BEGE_LATTE(
        "bege_latte", "Bege / Latte", "Industrial Vintage clássico",
        Color(0xFFD7CCC8), Color(0xFFEFEBE9), Color(0xFFFBF9F8), Color(0xFF3E2723),
        Color(0xFFBCAAA4), Color(0xFF3E2723), Color(0xFF8D6E63), Color(0xFFA1887F)
    ),
    BRANCO_CLEAN(
        "branco_clean", "Branco Clean", "Minimalismo puro em tons nevados",
        Color(0xFFECEFF1), Color(0xFFFFFFFF), Color(0xFFFAFAFA), Color(0xFF1E293B),
        Color(0xFFE2E8F0), Color(0xFF0F172A), Color(0xFF3B82F6), Color(0xFFCBD5E1)
    ),
    PRETO_AMOLED(
        "preto_amoled", "Preto Puro AMOLED", "Negro absoluto para economia de bateria e contraste",
        Color(0xFF000000), Color(0xFF0D0D11), Color(0xFF16161D), Color(0xFFF8FAFC),
        Color(0xFF1E1E26), Color(0xFFE2E8F0), Color(0xFF00E5FF), Color(0xFF27272A)
    ),
    AZUL_DEEP_TECH(
        "azul_deep_tech", "Azul Deep Tech", "Profundidade oceânica com ciano elétrico",
        Color(0xFF0A1128), Color(0xFF101F42), Color(0xFF192F60), Color(0xFFE0F2FE),
        Color(0xFF1E3A8A), Color(0xFF38BDF8), Color(0xFF06B6D4), Color(0xFF0284C7)
    ),
    VERMELHO_CRIMSON(
        "vermelho_crimson", "Vermelho Crimson", "Intensidade gamer de alta octanagem",
        Color(0xFF1A070B), Color(0xFF2E0D14), Color(0xFF451520), Color(0xFFFFE4E6),
        Color(0xFF5C1B29), Color(0xFFFDA4AF), Color(0xFFF43F5E), Color(0xFFBE123C)
    ),
    VERDE_ESMERALDA(
        "verde_esmeralda", "Verde Esmeralda", "Tons nobres com elegância refinada",
        Color(0xFF062018), Color(0xFF0D3327), Color(0xFF154838), Color(0xFFD1FAE5),
        Color(0xFF1A5844), Color(0xFF6EE7B7), Color(0xFF10B981), Color(0xFF059669)
    ),
    CARBONO(
        "carbono", "Carbono Brams", "Cinza grafite com detalhes em laranja industrial",
        Color(0xFF1E2024), Color(0xFF2C2F36), Color(0xFF393D45), Color(0xFFF1F5F9),
        Color(0xFF474C56), Color(0xFFFB923C), Color(0xFFF97316), Color(0xFF52525B)
    ),
    TERMINAL(
        "terminal", "Terminal Hacker", "Verde fósforo monocromático sobre preto puro",
        Color(0xFF050A05), Color(0xFF0C160C), Color(0xFF142414), Color(0xFF22C55E),
        Color(0xFF1A331A), Color(0xFF4ADE80), Color(0xFF16A34A), Color(0xFF15803D)
    );

    companion object {
        fun fromId(id: String?): KeyboardThemePalette {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: PRETO_AMOLED
        }
    }
}

enum class ThemeMode(val displayName: String) {
    SYSTEM("Sistema"),
    LIGHT("Claro"),
    DARK("Escuro"),
    AMOLED("Preto AMOLED")
}
