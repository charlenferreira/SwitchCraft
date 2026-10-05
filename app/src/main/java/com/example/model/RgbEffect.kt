package com.example.model

enum class RgbEffect(
    val displayName: String,
    val description: String,
    val isAnimated: Boolean,
    val isInteractive: Boolean
) {
    NORMAL("Normal", "Luz suave constante sem animação de fundo", false, false),
    ESTATICO("Estático", "Cor sólida fixa em todas as teclas", false, false),
    RESPIRACAO("Respiração", "Pulsação senoidal suave da iluminação", true, false),
    ONDA("Onda", "Gradiente contínuo correndo horizontalmente", true, false),
    CASCATA("Cascata", "Ondas de luz verticais descendo pelas fileiras", true, false),
    CICLO_CORES("Ciclo de Cores", "Transição suave contínua através do arco-íris RGB", true, false),
    REATIVO("Reativo", "Onda de luz radial expandindo a partir da tecla tocada", true, true),
    PINGO_DAGUA("Pingo d'Água", "Dispersão circular concêntrica a cada toque", true, true),
    STARLIGHT("Starlight", "Pequenos brilhos aleatórios nas teclas estilo céu estrelado", true, false),
    ESPIRAL("Espiral", "Vórtice giratório de cores a partir do centro", true, false),
    CHUVA("Chuva", "Gotas de luz caindo aleatoriamente pelas colunas", true, false),
    VISOR("Visor", "Feixe de radar escaneando de um lado para o outro", true, false),
    FOGO("Fogo", "Gradiente térmico alaranjado pulsando do fundo", true, false);

    companion object {
        fun fromName(name: String?): RgbEffect {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: ONDA
        }
    }
}
