package com.gaspersoft.unilab.ui.screens.electronics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gaspersoft.unilab.domain.calculator.calculateParallelResistance
import com.gaspersoft.unilab.domain.calculator.calculateSeriesResistance
import com.gaspersoft.unilab.domain.model.OperationResult
import com.gaspersoft.unilab.domain.model.ResistanceCalculation
import com.gaspersoft.unilab.domain.model.ResistanceInput
import com.gaspersoft.unilab.domain.model.ResistanceUnit
import com.gaspersoft.unilab.ui.components.ActionButtons
import com.gaspersoft.unilab.ui.components.DecimalInputField
import com.gaspersoft.unilab.ui.components.ErrorCard
import com.gaspersoft.unilab.ui.components.FormulaCard
import com.gaspersoft.unilab.ui.components.ProcedureCard
import com.gaspersoft.unilab.ui.components.ResultCard
import com.gaspersoft.unilab.ui.components.ScreenContainer
import com.gaspersoft.unilab.ui.components.SectionHeadline
import com.gaspersoft.unilab.ui.components.SelectorField
import com.gaspersoft.unilab.utils.formatNumber

private data class ResistanceFieldState(
    val id: Int,
    val value: String,
    val unit: ResistanceUnit,
)

@Composable
fun SeriesResistanceScreen(
    modifier: Modifier = Modifier,
) {
    ResistanceCalculatorScreen(
        title = "Resistencias en serie",
        description = "Agrega las resistencias del circuito y obtén la suma total con el procedimiento.",
        formulaTitle = "Fórmula de serie",
        formula = "Rtotal = R1 + R2 + R3 + ...",
        formulaDescription = "En serie, la resistencia equivalente es la suma directa de cada componente.",
        calculate = ::calculateSeriesResistance,
        modifier = modifier,
    )
}

@Composable
fun ParallelResistanceScreen(
    modifier: Modifier = Modifier,
) {
    ResistanceCalculatorScreen(
        title = "Resistencias en paralelo",
        description = "Usa dos o más resistencias y revisa el cálculo equivalente paso a paso.",
        formulaTitle = "Fórmula de paralelo",
        formula = "1/Rtotal = 1/R1 + 1/R2 + ...",
        formulaDescription = "Con dos resistencias, UniLab aplica también la forma (R1 × R2) / (R1 + R2).",
        calculate = ::calculateParallelResistance,
        modifier = modifier,
    )
}

@Composable
private fun ResistanceCalculatorScreen(
    title: String,
    description: String,
    formulaTitle: String,
    formula: String,
    formulaDescription: String,
    calculate: (List<ResistanceInput>) -> OperationResult<ResistanceCalculation>,
    modifier: Modifier = Modifier,
) {
    val fields = remember {
        mutableStateListOf(
            ResistanceFieldState(id = 1, value = "", unit = ResistanceUnit.OHM),
            ResistanceFieldState(id = 2, value = "", unit = ResistanceUnit.OHM),
        )
    }
    var nextId by remember { mutableIntStateOf(3) }
    var calculation by remember { mutableStateOf<ResistanceCalculation?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun clearAll() {
        fields.clear()
        fields.add(ResistanceFieldState(id = 1, value = "", unit = ResistanceUnit.OHM))
        fields.add(ResistanceFieldState(id = 2, value = "", unit = ResistanceUnit.OHM))
        nextId = 3
        calculation = null
        errorMessage = null
    }

    ScreenContainer(modifier = modifier) {
        SectionHeadline(
            title = title,
            description = description,
        )
        FormulaCard(
            title = formulaTitle,
            formula = formula,
            description = formulaDescription,
        )
        fields.forEachIndexed { index, field ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                ),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 8.dp, top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "R${index + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        if (fields.size > 2) {
                            TextButton(
                                onClick = {
                                    fields.remove(field)
                                    calculation = null
                                    errorMessage = null
                                },
                            ) {
                                Text(text = "Eliminar")
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        DecimalInputField(
                            value = field.value,
                            onValueChange = { newValue ->
                                fields[index] = field.copy(value = newValue)
                                calculation = null
                                errorMessage = null
                            },
                            label = "Valor",
                        )
                        SelectorField(
                            label = "Unidad",
                            selectedText = field.unit.symbol,
                            options = ResistanceUnit.entries.map { it.symbol },
                            onOptionSelected = { symbol ->
                                fields[index] = field.copy(
                                    unit = ResistanceUnit.entries.first { it.symbol == symbol },
                                )
                                calculation = null
                                errorMessage = null
                            },
                        )
                    }
                }
            }
        }

        TextButton(
            onClick = {
                fields.add(
                    ResistanceFieldState(
                        id = nextId,
                        value = "",
                        unit = ResistanceUnit.OHM,
                    ),
                )
                nextId += 1
                calculation = null
                errorMessage = null
            },
        ) {
            Text(text = "Agregar resistencia")
        }

        ActionButtons(
            onCalculate = {
                val result = calculate(
                    fields.map {
                        ResistanceInput(
                            value = it.value,
                            unit = it.unit,
                        )
                    },
                )
                when (result) {
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
            onClear = { clearAll() },
        )

        errorMessage?.let { ErrorCard(message = it) }
        calculation?.let { result ->
            ResultCard(
                title = "Resistencia total",
                value = "${formatNumber(result.displayValue)} ${result.displayUnit.symbol}",
                supportingText = result.alternateDisplay,
            )
            ProcedureCard(steps = result.steps)
        }
    }
}
