package com.gaspersoft.unilab.domain.model

data class UnitOption(
    val symbol: String,
    val factorToBase: Double,
)

enum class ResistanceUnit(
    val symbol: String,
    val factorToOhms: Double,
) {
    OHM(symbol = "Ω", factorToOhms = 1.0),
    KILOOHM(symbol = "kΩ", factorToOhms = 1_000.0),
    MEGAOHM(symbol = "MΩ", factorToOhms = 1_000_000.0),
    ;

    fun toUnitOption(): UnitOption = UnitOption(symbol = symbol, factorToBase = factorToOhms)
}

private val voltageUnits = listOf(
    UnitOption(symbol = "mV", factorToBase = 0.001),
    UnitOption(symbol = "V", factorToBase = 1.0),
    UnitOption(symbol = "kV", factorToBase = 1_000.0),
)

private val currentUnits = listOf(
    UnitOption(symbol = "µA", factorToBase = 0.000001),
    UnitOption(symbol = "mA", factorToBase = 0.001),
    UnitOption(symbol = "A", factorToBase = 1.0),
)

private val resistanceUnits = ResistanceUnit.entries.map { it.toUnitOption() }

private val frequencyUnits = listOf(
    UnitOption(symbol = "Hz", factorToBase = 1.0),
    UnitOption(symbol = "kHz", factorToBase = 1_000.0),
    UnitOption(symbol = "MHz", factorToBase = 1_000_000.0),
    UnitOption(symbol = "GHz", factorToBase = 1_000_000_000.0),
)

private val powerUnits = listOf(
    UnitOption(symbol = "mW", factorToBase = 0.001),
    UnitOption(symbol = "W", factorToBase = 1.0),
    UnitOption(symbol = "kW", factorToBase = 1_000.0),
)

enum class ConverterType(
    val label: String,
    val unitLabel: String,
    val units: List<UnitOption>,
    val baseUnit: UnitOption,
    val allowNegative: Boolean,
) {
    VOLTAGE(
        label = "Voltaje",
        unitLabel = "voltaje",
        units = voltageUnits,
        baseUnit = UnitOption(symbol = "V", factorToBase = 1.0),
        allowNegative = true,
    ),
    CURRENT(
        label = "Corriente",
        unitLabel = "corriente",
        units = currentUnits,
        baseUnit = UnitOption(symbol = "A", factorToBase = 1.0),
        allowNegative = true,
    ),
    RESISTANCE(
        label = "Resistencia",
        unitLabel = "resistencia",
        units = resistanceUnits,
        baseUnit = UnitOption(symbol = "Ω", factorToBase = 1.0),
        allowNegative = false,
    ),
    FREQUENCY(
        label = "Frecuencia",
        unitLabel = "frecuencia",
        units = frequencyUnits,
        baseUnit = UnitOption(symbol = "Hz", factorToBase = 1.0),
        allowNegative = false,
    ),
    POWER(
        label = "Potencia",
        unitLabel = "potencia",
        units = powerUnits,
        baseUnit = UnitOption(symbol = "W", factorToBase = 1.0),
        allowNegative = false,
    ),
    ;
}

data class SiPrefix(
    val name: String,
    val symbol: String,
    val exponent: Int,
)

val siPrefixes = listOf(
    SiPrefix(name = "pico", symbol = "p", exponent = -12),
    SiPrefix(name = "nano", symbol = "n", exponent = -9),
    SiPrefix(name = "micro", symbol = "µ", exponent = -6),
    SiPrefix(name = "mili", symbol = "m", exponent = -3),
    SiPrefix(name = "unidad", symbol = "1", exponent = 0),
    SiPrefix(name = "kilo", symbol = "k", exponent = 3),
    SiPrefix(name = "mega", symbol = "M", exponent = 6),
    SiPrefix(name = "giga", symbol = "G", exponent = 9),
)
