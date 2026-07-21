package com.gaspersoft.unilab.domain.calculator

import com.gaspersoft.unilab.domain.model.ConversionCalculation
import com.gaspersoft.unilab.domain.model.ConverterType
import com.gaspersoft.unilab.domain.model.OhmsLawCalculation
import com.gaspersoft.unilab.domain.model.OhmsLawTarget
import com.gaspersoft.unilab.domain.model.OperationResult
import com.gaspersoft.unilab.domain.model.OperationResult.Failure
import com.gaspersoft.unilab.domain.model.OperationResult.Success
import com.gaspersoft.unilab.domain.model.ResistanceCalculation
import com.gaspersoft.unilab.domain.model.ResistorBandColor
import com.gaspersoft.unilab.domain.model.ResistorBandCount
import com.gaspersoft.unilab.domain.model.ResistorColorCalculation
import com.gaspersoft.unilab.domain.model.ResistanceInput
import com.gaspersoft.unilab.domain.model.ResistanceUnit
import com.gaspersoft.unilab.domain.model.UnitOption
import com.gaspersoft.unilab.utils.formatNumber
import kotlin.math.abs
import kotlin.math.pow

fun calculateOhmsLaw(
    target: OhmsLawTarget,
    voltageInput: String,
    currentInput: String,
    resistanceInput: String,
): OperationResult<OhmsLawCalculation> =
    when (target) {
        OhmsLawTarget.CURRENT -> {
            val voltage = when (val result = parseRequiredValue(voltageInput, "el voltaje", allowZero = true)) {
                is Success -> result.data
                is Failure -> return result
            }
            val resistance = when (val result = parseRequiredValue(resistanceInput, "la resistencia", allowZero = false)) {
                is Success -> result.data
                is Failure -> return result
            }
            val current = voltage / resistance
            Success(
                OhmsLawCalculation(
                    resultLabel = target.resultLabel,
                    value = current,
                    unit = target.unit,
                    alternateDisplay = "${formatNumber(current * 1_000)} mA",
                    steps = listOf(
                        "I = V / R",
                        "I = ${formatNumber(voltage)} / ${formatNumber(resistance)}",
                        "I = ${formatNumber(current)} A",
                        "I = ${formatNumber(current * 1_000)} mA",
                    ),
                ),
            )
        }

        OhmsLawTarget.VOLTAGE -> {
            val current = when (val result = parseRequiredValue(currentInput, "la corriente", allowZero = true)) {
                is Success -> result.data
                is Failure -> return result
            }
            val resistance = when (val result = parseRequiredValue(resistanceInput, "la resistencia", allowZero = true)) {
                is Success -> result.data
                is Failure -> return result
            }
            val voltage = current * resistance
            Success(
                OhmsLawCalculation(
                    resultLabel = target.resultLabel,
                    value = voltage,
                    unit = target.unit,
                    alternateDisplay = if (abs(voltage) < 1.0) "${formatNumber(voltage * 1_000)} mV" else null,
                    steps = buildList {
                        add("V = I × R")
                        add("V = ${formatNumber(current)} × ${formatNumber(resistance)}")
                        add("V = ${formatNumber(voltage)} V")
                        if (abs(voltage) < 1.0) {
                            add("V = ${formatNumber(voltage * 1_000)} mV")
                        }
                    },
                ),
            )
        }

        OhmsLawTarget.RESISTANCE -> {
            val voltage = when (val result = parseRequiredValue(voltageInput, "el voltaje", allowZero = true)) {
                is Success -> result.data
                is Failure -> return result
            }
            val current = when (val result = parseRequiredValue(currentInput, "la corriente", allowZero = false)) {
                is Success -> result.data
                is Failure -> return result
            }
            val resistance = voltage / current
            Success(
                OhmsLawCalculation(
                    resultLabel = target.resultLabel,
                    value = resistance,
                    unit = target.unit,
                    alternateDisplay = buildResistanceAlternateDisplay(resistance),
                    steps = buildList {
                        add("R = V / I")
                        add("R = ${formatNumber(voltage)} / ${formatNumber(current)}")
                        add("R = ${formatNumber(resistance)} Ω")
                        buildResistanceAlternateDisplay(resistance)?.let { add("R = $it") }
                    },
                ),
            )
        }
    }

fun calculateSeriesResistance(inputs: List<ResistanceInput>): OperationResult<ResistanceCalculation> {
    if (inputs.size < 2) {
        return Failure("Agrega al menos dos resistencias.")
    }

    val parsedValues = mutableListOf<Double>()
    inputs.forEachIndexed { index, input ->
        val parsed = when (
            val result = parseRequiredValue(
                input = input.value,
                fieldLabel = "R${index + 1}",
                allowZero = true,
            )
        ) {
            is Success -> result.data
            is Failure -> return result
        }
        parsedValues += parsed
    }

    val valuesInOhms = parsedValues.mapIndexed { index, value ->
        value * inputs[index].unit.factorToOhms
    }
    val totalOhms = valuesInOhms.sum()
    val displayUnit = selectResistanceUnit(totalOhms)
    val displayValue = totalOhms / displayUnit.factorToOhms

    return Success(
        ResistanceCalculation(
            totalOhms = totalOhms,
            displayValue = displayValue,
            displayUnit = displayUnit,
            alternateDisplay = if (displayUnit == ResistanceUnit.OHM) {
                buildResistanceAlternateDisplay(totalOhms)
            } else {
                "${formatNumber(totalOhms)} Ω"
            },
            steps = buildList {
                parsedValues.forEachIndexed { index, value ->
                    add("R${index + 1} = ${formatNumber(value)} ${inputs[index].unit.symbol}")
                }
                if (inputs.any { it.unit != ResistanceUnit.OHM }) {
                    add(
                        "En ohmios: " + valuesInOhms.mapIndexed { index, value ->
                            "R${index + 1} = ${formatNumber(value)} Ω"
                        }.joinToString(", "),
                    )
                }
                add("Rtotal = ${valuesInOhms.joinToString(" + ") { formatNumber(it) }}")
                add("Rtotal = ${formatNumber(totalOhms)} Ω")
                if (displayUnit != ResistanceUnit.OHM) {
                    add("Rtotal = ${formatNumber(displayValue)} ${displayUnit.symbol}")
                }
            },
        ),
    )
}

fun calculateParallelResistance(inputs: List<ResistanceInput>): OperationResult<ResistanceCalculation> {
    if (inputs.size < 2) {
        return Failure("Agrega al menos dos resistencias.")
    }

    val parsedValues = mutableListOf<Double>()
    inputs.forEachIndexed { index, input ->
        val parsed = when (
            val result = parseRequiredValue(
                input = input.value,
                fieldLabel = "R${index + 1}",
                allowZero = false,
            )
        ) {
            is Success -> result.data
            is Failure -> return result
        }
        parsedValues += parsed
    }

    val valuesInOhms = parsedValues.mapIndexed { index, value ->
        value * inputs[index].unit.factorToOhms
    }
    val totalOhms = if (valuesInOhms.size == 2) {
        (valuesInOhms[0] * valuesInOhms[1]) / (valuesInOhms[0] + valuesInOhms[1])
    } else {
        val inverseSum = valuesInOhms.sumOf { 1.0 / it }
        1.0 / inverseSum
    }
    val displayUnit = selectResistanceUnit(totalOhms)
    val displayValue = totalOhms / displayUnit.factorToOhms

    return Success(
        ResistanceCalculation(
            totalOhms = totalOhms,
            displayValue = displayValue,
            displayUnit = displayUnit,
            alternateDisplay = if (displayUnit == ResistanceUnit.OHM) {
                buildResistanceAlternateDisplay(totalOhms)
            } else {
                "${formatNumber(totalOhms)} Ω"
            },
            steps = buildList {
                parsedValues.forEachIndexed { index, value ->
                    add("R${index + 1} = ${formatNumber(value)} ${inputs[index].unit.symbol}")
                }
                if (inputs.any { it.unit != ResistanceUnit.OHM }) {
                    add(
                        "En ohmios: " + valuesInOhms.mapIndexed { index, value ->
                            "R${index + 1} = ${formatNumber(value)} Ω"
                        }.joinToString(", "),
                    )
                }
                if (valuesInOhms.size == 2) {
                    val first = valuesInOhms[0]
                    val second = valuesInOhms[1]
                    add("Rtotal = (R1 × R2) / (R1 + R2)")
                    add(
                        "Rtotal = (${formatNumber(first)} × ${formatNumber(second)}) / " +
                            "(${formatNumber(first)} + ${formatNumber(second)})",
                    )
                    add("Rtotal = ${formatNumber(totalOhms)} Ω")
                } else {
                    val inverseSum = valuesInOhms.sumOf { 1.0 / it }
                    add(
                        "1 / Rtotal = " +
                            valuesInOhms.joinToString(" + ") { "1 / ${formatNumber(it)}" },
                    )
                    add(
                        "1 / Rtotal = " +
                            valuesInOhms.joinToString(" + ") { formatNumber(1.0 / it) },
                    )
                    add("1 / Rtotal = ${formatNumber(inverseSum)}")
                    add("Rtotal = 1 / ${formatNumber(inverseSum)}")
                    add("Rtotal = ${formatNumber(totalOhms)} Ω")
                }
                if (displayUnit != ResistanceUnit.OHM) {
                    add("Rtotal = ${formatNumber(displayValue)} ${displayUnit.symbol}")
                }
            },
        ),
    )
}

fun convertVoltage(
    valueInput: String,
    fromUnit: UnitOption,
    toUnit: UnitOption,
): OperationResult<ConversionCalculation> = convertMeasurement(
    type = ConverterType.VOLTAGE,
    valueInput = valueInput,
    fromUnit = fromUnit,
    toUnit = toUnit,
)

fun convertCurrent(
    valueInput: String,
    fromUnit: UnitOption,
    toUnit: UnitOption,
): OperationResult<ConversionCalculation> = convertMeasurement(
    type = ConverterType.CURRENT,
    valueInput = valueInput,
    fromUnit = fromUnit,
    toUnit = toUnit,
)

fun convertResistance(
    valueInput: String,
    fromUnit: UnitOption,
    toUnit: UnitOption,
): OperationResult<ConversionCalculation> = convertMeasurement(
    type = ConverterType.RESISTANCE,
    valueInput = valueInput,
    fromUnit = fromUnit,
    toUnit = toUnit,
)

fun convertFrequency(
    valueInput: String,
    fromUnit: UnitOption,
    toUnit: UnitOption,
): OperationResult<ConversionCalculation> = convertMeasurement(
    type = ConverterType.FREQUENCY,
    valueInput = valueInput,
    fromUnit = fromUnit,
    toUnit = toUnit,
)

fun convertPower(
    valueInput: String,
    fromUnit: UnitOption,
    toUnit: UnitOption,
): OperationResult<ConversionCalculation> = convertMeasurement(
    type = ConverterType.POWER,
    valueInput = valueInput,
    fromUnit = fromUnit,
    toUnit = toUnit,
)

fun calculateResistorColorCode(
    bandCount: ResistorBandCount,
    colors: List<ResistorBandColor>,
): OperationResult<ResistorColorCalculation> {
    if (colors.size != bandCount.totalBands) {
        return Failure("Selecciona un color para cada banda.")
    }

    val digitCount = when (bandCount) {
        ResistorBandCount.FOUR -> 2
        ResistorBandCount.FIVE -> 3
    }

    val significantColors = colors.take(digitCount)
    significantColors.forEachIndexed { index, color ->
        if (color.digit == null) {
            return Failure("El color ${color.label.lowercase()} no es válido para la banda ${index + 1}.")
        }
    }

    val multiplierColor = colors[digitCount]
    if (multiplierColor.multiplierExponent == null) {
        return Failure("El color ${multiplierColor.label.lowercase()} no es válido como multiplicador.")
    }

    val toleranceColor = colors.last()
    if (toleranceColor.tolerancePercent == null) {
        return Failure("El color ${toleranceColor.label.lowercase()} no es válido como tolerancia.")
    }

    val significantValue = significantColors.fold(0) { accumulator, color ->
        accumulator * 10 + (color.digit ?: 0)
    }
    val multiplierValue = 10.0.pow(multiplierColor.multiplierExponent.toDouble())
    val totalOhms = significantValue * multiplierValue
    val displayUnit = selectResistanceUnit(totalOhms)
    val displayValue = totalOhms / displayUnit.factorToOhms
    val tolerancePercent = toleranceColor.tolerancePercent

    return Success(
        ResistorColorCalculation(
            totalOhms = totalOhms,
            displayValue = displayValue,
            displayUnit = displayUnit,
            tolerancePercent = tolerancePercent,
            alternateDisplay = if (displayUnit == ResistanceUnit.OHM) {
                buildResistanceAlternateDisplay(totalOhms)
            } else {
                "${formatNumber(totalOhms)} Ω"
            },
            steps = buildList {
                add("Bandas = ${colors.joinToString(" - ") { it.label }}")
                add("Cifras significativas = ${significantColors.joinToString("") { "${it.digit}" }}")
                add("Multiplicador = 10^${multiplierColor.multiplierExponent}")
                add("R = ${significantValue} × 10^${multiplierColor.multiplierExponent}")
                add("R = ${formatNumber(totalOhms)} Ω")
                if (displayUnit != ResistanceUnit.OHM) {
                    add("R = ${formatNumber(displayValue)} ${displayUnit.symbol}")
                }
                add("Tolerancia = ±${formatNumber(tolerancePercent)} %")
            },
        ),
    )
}

private fun convertMeasurement(
    type: ConverterType,
    valueInput: String,
    fromUnit: UnitOption,
    toUnit: UnitOption,
): OperationResult<ConversionCalculation> {
    val value = when (
        val result = parseRequiredValue(
            input = valueInput,
            fieldLabel = "el valor a convertir",
            allowZero = true,
            allowNegative = type.allowNegative,
        )
    ) {
        is Success -> result.data
        is Failure -> return result
    }

    val baseValue = value * fromUnit.factorToBase
    val convertedValue = baseValue / toUnit.factorToBase

    return Success(
        ConversionCalculation(
            resultValue = convertedValue,
            resultUnit = toUnit.symbol,
            steps = buildList {
                add("Valor inicial = ${formatNumber(value)} ${fromUnit.symbol}")
                if (fromUnit != type.baseUnit) {
                    add(
                        "${formatNumber(value)} ${fromUnit.symbol} = " +
                            "${formatNumber(baseValue)} ${type.baseUnit.symbol}",
                    )
                } else {
                    add("Valor base = ${formatNumber(baseValue)} ${type.baseUnit.symbol}")
                }
                if (toUnit != type.baseUnit) {
                    add(
                        "${formatNumber(baseValue)} ${type.baseUnit.symbol} = " +
                            "${formatNumber(convertedValue)} ${toUnit.symbol}",
                    )
                } else {
                    add("Resultado = ${formatNumber(convertedValue)} ${toUnit.symbol}")
                }
            },
        ),
    )
}

private fun parseRequiredValue(
    input: String,
    fieldLabel: String,
    allowZero: Boolean,
    allowNegative: Boolean = false,
): OperationResult<Double> {
    val normalized = input.trim().replace(',', '.')
    if (normalized.isBlank()) {
        return Failure("Ingresa $fieldLabel.")
    }

    val parsedValue = normalized.toDoubleOrNull()
        ?: return Failure("Ingresa un número válido para $fieldLabel.")

    if (!allowNegative && parsedValue < 0) {
        return Failure("${fieldLabel.replaceFirstChar { it.uppercase() }} no puede ser negativo.")
    }

    if (!allowZero && parsedValue == 0.0) {
        return Failure("${fieldLabel.replaceFirstChar { it.uppercase() }} debe ser mayor que cero.")
    }

    return Success(parsedValue)
}

private fun selectResistanceUnit(valueInOhms: Double): ResistanceUnit =
    when {
        abs(valueInOhms) >= ResistanceUnit.MEGAOHM.factorToOhms -> ResistanceUnit.MEGAOHM
        abs(valueInOhms) >= ResistanceUnit.KILOOHM.factorToOhms -> ResistanceUnit.KILOOHM
        else -> ResistanceUnit.OHM
    }

private fun buildResistanceAlternateDisplay(valueInOhms: Double): String? =
    when {
        abs(valueInOhms) >= ResistanceUnit.MEGAOHM.factorToOhms ->
            "${formatNumber(valueInOhms / ResistanceUnit.MEGAOHM.factorToOhms)} MΩ"

        abs(valueInOhms) >= ResistanceUnit.KILOOHM.factorToOhms ->
            "${formatNumber(valueInOhms / ResistanceUnit.KILOOHM.factorToOhms)} kΩ"

        else -> null
    }
