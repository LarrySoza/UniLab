package com.gaspersoft.unilab.domain.model

enum class ResistorBandCount(
    val totalBands: Int,
    val label: String,
) {
    FOUR(totalBands = 4, label = "4 bandas"),
    FIVE(totalBands = 5, label = "5 bandas"),
}

enum class ResistorBandColor(
    val label: String,
    val hexColor: Long,
    val digit: Int?,
    val multiplierExponent: Int?,
    val tolerancePercent: Double?,
) {
    BLACK(
        label = "Negro",
        hexColor = 0xFF202124,
        digit = 0,
        multiplierExponent = 0,
        tolerancePercent = null,
    ),
    BROWN(
        label = "Marrón",
        hexColor = 0xFF6D4C41,
        digit = 1,
        multiplierExponent = 1,
        tolerancePercent = 1.0,
    ),
    RED(
        label = "Rojo",
        hexColor = 0xFFC62828,
        digit = 2,
        multiplierExponent = 2,
        tolerancePercent = 2.0,
    ),
    ORANGE(
        label = "Naranja",
        hexColor = 0xFFEF6C00,
        digit = 3,
        multiplierExponent = 3,
        tolerancePercent = null,
    ),
    YELLOW(
        label = "Amarillo",
        hexColor = 0xFFF9A825,
        digit = 4,
        multiplierExponent = 4,
        tolerancePercent = null,
    ),
    GREEN(
        label = "Verde",
        hexColor = 0xFF2E7D32,
        digit = 5,
        multiplierExponent = 5,
        tolerancePercent = 0.5,
    ),
    BLUE(
        label = "Azul",
        hexColor = 0xFF1565C0,
        digit = 6,
        multiplierExponent = 6,
        tolerancePercent = 0.25,
    ),
    VIOLET(
        label = "Violeta",
        hexColor = 0xFF6A1B9A,
        digit = 7,
        multiplierExponent = 7,
        tolerancePercent = 0.1,
    ),
    GRAY(
        label = "Gris",
        hexColor = 0xFF757575,
        digit = 8,
        multiplierExponent = 8,
        tolerancePercent = 0.05,
    ),
    WHITE(
        label = "Blanco",
        hexColor = 0xFFF5F5F5,
        digit = 9,
        multiplierExponent = 9,
        tolerancePercent = null,
    ),
    GOLD(
        label = "Dorado",
        hexColor = 0xFFC9A227,
        digit = null,
        multiplierExponent = -1,
        tolerancePercent = 5.0,
    ),
    SILVER(
        label = "Plateado",
        hexColor = 0xFFB0BEC5,
        digit = null,
        multiplierExponent = -2,
        tolerancePercent = 10.0,
    ),
    ;

    companion object {
        val digitColors: List<ResistorBandColor> = entries.filter { it.digit != null }
        val multiplierColors: List<ResistorBandColor> = entries.filter { it.multiplierExponent != null }
        val toleranceColors: List<ResistorBandColor> = entries.filter { it.tolerancePercent != null }
    }
}
