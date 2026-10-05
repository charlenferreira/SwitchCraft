package com.example.model

enum class KeycapProfile(
    val displayName: String,
    val description: String,
    val topCurvature: Float,
    val bevelRatio: Float,
    val heightRatio: Float,
    val cornerRoundness: Float,
    val isSpherical: Boolean,
    val isPudding: Boolean = false
) {
    BRAMS("Brams", "Design icônico com topo côncavo e chanfro acentuado", 0.35f, 0.22f, 1.0f, 0.28f, true),
    ESCULPIDA("Esculpida", "Perfil clássico com ângulo ergonômico diferenciado por fileira", 0.40f, 0.25f, 1.05f, 0.22f, false),
    REDONDA("Redonda", "Estilo máquina de escrever clássica circular", 0.20f, 0.15f, 0.95f, 0.90f, true),
    BAIXA("Baixa", "Low-profile moderno para acionamento ultra-rápido", 0.10f, 0.10f, 0.60f, 0.25f, false),
    CHICLET("Chiclet", "Teclas ultra-finas e discretas estilo laptop premium", 0.05f, 0.08f, 0.45f, 0.20f, false),
    ESFERICA("Esférica", "Recuo esférico profundo para encaixe perfeito dos dedos", 0.55f, 0.20f, 1.15f, 0.35f, true),
    PLANA("Plana", "Superfície 100% nivelada estilo terminal industrial", 0.0f, 0.12f, 0.85f, 0.15f, false),
    RETRO("Retrô", "Estilo IBM Model M dos anos 80 com corpo robusto", 0.30f, 0.30f, 1.25f, 0.10f, false),
    CHERRY("Cherry", "O padrão mundial dos entusiastas com altura balanceada", 0.25f, 0.18f, 0.90f, 0.20f, false),
    SA("SA", "Perfil alto esférico e imponente com acústica profunda", 0.60f, 0.28f, 1.35f, 0.30f, true),
    DSA("DSA", "Perfil uniforme de altura média com topo esférico centrado", 0.45f, 0.16f, 0.80f, 0.32f, true),
    MT3("MT3", "Curvatura vintage agressiva inspirada nos terminais IBM", 0.65f, 0.26f, 1.30f, 0.38f, true),
    PILULA("Pílula", "Formato oval alongado com cantos arredondados", 0.20f, 0.14f, 0.85f, 0.75f, false),
    BRILHANTE("Brilhante", "Pudding keycaps translúcidas com difusor RGB nas laterais", 0.25f, 0.18f, 0.95f, 0.22f, false, true),
    XDA("XDA", "Perfil plano de topo amplo uniforme com área de toque aumentada", 0.20f, 0.12f, 0.82f, 0.25f, true),
    OEM("OEM", "Perfil padrão de teclados mecânicos comerciais pré-montados", 0.30f, 0.20f, 1.00f, 0.18f, false),
    KAT("KAT", "Transição suave entre Cherry e SA, suave e aerodinâmico", 0.42f, 0.20f, 1.10f, 0.28f, true);

    companion object {
        fun fromName(name: String?): KeycapProfile {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: BRAMS
        }
    }
}
