package com.gaspersoft.unilab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gaspersoft.unilab.domain.calculator.calculateResistorColorCode
import com.gaspersoft.unilab.domain.model.OperationResult
import com.gaspersoft.unilab.domain.model.ResistorBandColor
import com.gaspersoft.unilab.domain.model.ResistorBandCount
import com.gaspersoft.unilab.domain.model.ResistorColorCalculation
import com.gaspersoft.unilab.utils.formatNumber

@Composable
fun ResistorColorCodePanel(
    modifier: Modifier = Modifier,
) {
    var bandCount by remember { mutableStateOf(ResistorBandCount.FOUR) }
    var selectedColors by remember { mutableStateOf(defaultBandsFor(ResistorBandCount.FOUR)) }
    var calculation by remember { mutableStateOf<ResistorColorCalculation?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun resetForBandCount(newBandCount: ResistorBandCount) {
        bandCount = newBandCount
        selectedColors = defaultBandsFor(newBandCount)
        calculation = null
        errorMessage = null
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SectionHeadline(
                title = "Código de colores de resistencias",
                description = "Selecciona las bandas para obtener el valor nominal y la tolerancia.",
            )
            FormulaCard(
                title = "Lectura rápida",
                formula = "Cifras × 10^n ± tolerancia",
                description = "Soporta resistencias de 4 y 5 bandas con una lectura educativa del procedimiento.",
            )
            ChoiceChipRow(
                options = ResistorBandCount.entries.map { it.label },
                selectedOption = bandCount.label,
                onOptionSelected = { option ->
                    resetForBandCount(ResistorBandCount.entries.first { it.label == option })
                },
            )
            BandPreview(colors = selectedColors)
            bandLabelsFor(bandCount).forEachIndexed { index, label ->
                val availableColors = availableColorsForBand(bandCount, index)
                SelectorField(
                    label = label,
                    selectedText = selectedColors[index].label,
                    options = availableColors.map { it.label },
                    onOptionSelected = { selectedLabel ->
                        val updatedColors = selectedColors.toMutableList()
                        updatedColors[index] = availableColors.first { it.label == selectedLabel }
                        selectedColors = updatedColors
                        calculation = null
                        errorMessage = null
                    },
                )
            }
            ActionButtons(
                onCalculate = {
                    when (val result = calculateResistorColorCode(bandCount, selectedColors)) {
                        is OperationResult.Success -> {
                            calculation = result.data
                            errorMessage = null
                        }

                        is OperationResult.Failure -> {
                            calculation = null
                            errorMessage = result.message
                        }
                    }
                },
                onClear = { resetForBandCount(bandCount) },
            )

            errorMessage?.let { ErrorCard(message = it) }
            calculation?.let { result ->
                ResultCard(
                    title = "Valor calculado",
                    value = "${formatNumber(result.displayValue)} ${result.displayUnit.symbol}",
                    supportingText = buildString {
                        append("Tolerancia: ±${formatNumber(result.tolerancePercent)} %")
                        result.alternateDisplay?.let {
                            append(" • ")
                            append(it)
                        }
                    },
                )
                ProcedureCard(steps = result.steps)
            }
        }
    }
}

@Composable
private fun BandPreview(
    colors: List<ResistorBandColor>,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            colors.forEach { color ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(72.dp)
                            .background(
                                color = Color(color.hexColor),
                                shape = MaterialTheme.shapes.medium,
                            ),
                    )
                    Text(
                        text = color.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

private fun defaultBandsFor(bandCount: ResistorBandCount): List<ResistorBandColor> =
    when (bandCount) {
        ResistorBandCount.FOUR -> listOf(
            ResistorBandColor.BROWN,
            ResistorBandColor.BLACK,
            ResistorBandColor.RED,
            ResistorBandColor.GOLD,
        )

        ResistorBandCount.FIVE -> listOf(
            ResistorBandColor.BROWN,
            ResistorBandColor.BLACK,
            ResistorBandColor.BLACK,
            ResistorBandColor.RED,
            ResistorBandColor.BROWN,
        )
    }

private fun bandLabelsFor(bandCount: ResistorBandCount): List<String> =
    when (bandCount) {
        ResistorBandCount.FOUR -> listOf(
            "Primera banda",
            "Segunda banda",
            "Multiplicador",
            "Tolerancia",
        )

        ResistorBandCount.FIVE -> listOf(
            "Primera banda",
            "Segunda banda",
            "Tercera banda",
            "Multiplicador",
            "Tolerancia",
        )
    }

private fun availableColorsForBand(
    bandCount: ResistorBandCount,
    index: Int,
): List<ResistorBandColor> {
    val digitCount = when (bandCount) {
        ResistorBandCount.FOUR -> 2
        ResistorBandCount.FIVE -> 3
    }

    return when {
        index < digitCount -> ResistorBandColor.digitColors
        index == digitCount -> ResistorBandColor.multiplierColors
        else -> ResistorBandColor.toleranceColors
    }
}
