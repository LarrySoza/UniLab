package com.gaspersoft.unilab.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun formatNumber(
    value: Double,
    scale: Int = 6,
): String {
    val sanitizedValue = if (value == -0.0) 0.0 else value
    return BigDecimal.valueOf(sanitizedValue)
        .setScale(scale, RoundingMode.HALF_UP)
        .stripTrailingZeros()
        .toPlainString()
}

fun sanitizeDecimalInput(input: String): String {
    val builder = StringBuilder()
    var hasDecimalSeparator = false
    input.forEachIndexed { index, character ->
        when {
            character.isDigit() -> builder.append(character)
            character == '-' && index == 0 && builder.isEmpty() -> builder.append(character)
            (character == '.' || character == ',') && !hasDecimalSeparator -> {
                builder.append('.')
                hasDecimalSeparator = true
            }
        }
    }
    return builder.toString()
}
