package com.example.model

enum class SwitchSoundProfile(
    val displayName: String,
    val description: String,
    val baseFrequency: Float,
    val pingFrequency: Float,
    val attackMs: Float,
    val decayMs: Float,
    val noiseRatio: Float,
    val harmonicsRatio: Float,
    val stemColorHex: Long
) {
    MECANICO("Mecânico", "Estalo nítido clássico com clique duplo audível (Blue Clicky)", 720f, 2400f, 3f, 45f, 0.35f, 0.70f, 0xFF0284C7),
    GRAVE("Grave", "Batida profunda encorpada e abafada (Thocky Black)", 260f, 900f, 5f, 70f, 0.20f, 0.40f, 0xFF18181B),
    PEDRINHA("Pedrinha", "Acústica cristalina marmorizada e oca (Marbly Clack)", 580f, 1850f, 2f, 55f, 0.25f, 0.60f, 0xFF14B8A6),
    TESOURA("Tesoura", "Mecanismo scissor switch curto e instantâneo", 880f, 2800f, 1.5f, 25f, 0.30f, 0.50f, 0xFFE2E8F0),
    CHERRY("Cherry", "Toque tátil com resposta média balanceada (MX Brown)", 490f, 1500f, 4f, 50f, 0.32f, 0.55f, 0xFF92400E),
    COM_MOLA("Com mola", "Ressonância harmônica metálica estendida de mola", 640f, 3200f, 2.5f, 95f, 0.28f, 0.85f, 0xFFF59E0B),
    RAPIDO("Rápido", "Curso ultra curto para digitação ágil (Speed Silver)", 680f, 2100f, 1.8f, 30f, 0.22f, 0.45f, 0xFF94A3B8),
    ACOLCHOADO("Acolchoado", "Toque aveludado suave com impacto abafado", 310f, 750f, 8f, 60f, 0.15f, 0.25f, 0xFFF472B6),
    MAC("Mac", "Design de alumínio limpo e batida chiclet metálica", 920f, 3100f, 1.2f, 28f, 0.38f, 0.65f, 0xFFCBD5E1),
    BAIXO("Baixo", "Perfil mecânico rebaixado com batida seca e precisa", 520f, 1600f, 2.2f, 35f, 0.26f, 0.48f, 0xFF6366F1),
    ALUMINIO("Alumínio", "Gabinete usinado CNC pesado com ressonância de placa", 440f, 1950f, 3f, 80f, 0.30f, 0.78f, 0xFF64748B),
    MAGNETICO("Magnético", "Sensor Hall effect com transição contínua suave", 380f, 1100f, 3.5f, 45f, 0.12f, 0.35f, 0xFFA855F7),
    MEDIANO("Mediano", "Acústica cremosa intermediária lubrificada (Creamy)", 410f, 1350f, 4.2f, 58f, 0.18f, 0.52f, 0xFFFBBF24),
    SILENCIOSO("Silencioso", "Amortecedor de fundo duplo com absorção quase total", 220f, 500f, 10f, 35f, 0.08f, 0.15f, 0xFFEF4444);

    companion object {
        fun fromName(name: String?): SwitchSoundProfile {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: GRAVE
        }
    }
}
