package com.gaspersoft.unilab.domain.model

enum class OhmsLawTarget(
    val label: String,
    val resultLabel: String,
    val unit: String,
    val formula: String,
) {
    VOLTAGE(
        label = "Voltaje",
        resultLabel = "Voltaje",
        unit = "V",
        formula = "V = I × R",
    ),
    CURRENT(
        label = "Corriente",
        resultLabel = "Corriente",
        unit = "A",
        formula = "I = V / R",
    ),
    RESISTANCE(
        label = "Resistencia",
        resultLabel = "Resistencia",
        unit = "Ω",
        formula = "R = V / I",
    ),
}

data class ResistanceInput(
    val value: String,
    val unit: ResistanceUnit,
)

data class OhmsLawCalculation(
    val resultLabel: String,
    val value: Double,
    val unit: String,
    val alternateDisplay: String?,
    val steps: List<String>,
)

data class ResistanceCalculation(
    val totalOhms: Double,
    val displayValue: Double,
    val displayUnit: ResistanceUnit,
    val alternateDisplay: String?,
    val steps: List<String>,
)

data class ConversionCalculation(
    val resultValue: Double,
    val resultUnit: String,
    val steps: List<String>,
)

data class ResistorColorCalculation(
    val totalOhms: Double,
    val displayValue: Double,
    val displayUnit: ResistanceUnit,
    val tolerancePercent: Double,
    val alternateDisplay: String?,
    val steps: List<String>,
)
