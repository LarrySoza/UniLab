package com.gaspersoft.unilab.ui.screens.electronics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gaspersoft.unilab.domain.calculator.calculateOhmsLaw
import com.gaspersoft.unilab.domain.model.OhmsLawCalculation
import com.gaspersoft.unilab.domain.model.OhmsLawTarget
import com.gaspersoft.unilab.domain.model.OperationResult
import com.gaspersoft.unilab.ui.components.ActionButtons
import com.gaspersoft.unilab.ui.components.ChoiceChipRow
import com.gaspersoft.unilab.ui.components.DecimalInputField
import com.gaspersoft.unilab.ui.components.ErrorCard
import com.gaspersoft.unilab.ui.components.FormulaCard
import com.gaspersoft.unilab.ui.components.ProcedureCard
import com.gaspersoft.unilab.ui.components.ResultCard
import com.gaspersoft.unilab.ui.components.ScreenContainer
import com.gaspersoft.unilab.ui.components.SectionHeadline
import com.gaspersoft.unilab.utils.formatNumber

@Composable
fun OhmsLawScreen(
    modifier: Modifier = Modifier,
) {
    var target by remember { mutableStateOf(OhmsLawTarget.CURRENT) }
    var voltage by remember { mutableStateOf("") }
    var current by remember { mutableStateOf("") }
    var resistance by remember { mutableStateOf("") }
    var calculation by remember { mutableStateOf<OhmsLawCalculation?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun clearAll() {
        voltage = ""
        current = ""
        resistance = ""
        calculation = null
        errorMessage = null
    }

    ScreenContainer(modifier = modifier) {
        SectionHeadline(
            title = "Ley de Ohm",
            description = "Selecciona la variable a calcular y completa los valores conocidos.",
        )
        FormulaCard(
            title = "Relación fundamental",
            formula = "V = I × R",
            description = "UniLab muestra el resultado y el desarrollo para reforzar el aprendizaje.",
        )
        ChoiceChipRow(
            options = OhmsLawTarget.entries.map { "Calcular ${it.label.lowercase()}" },
            selectedOption = "Calcular ${target.label.lowercase()}",
            onOptionSelected = { option ->
                target = OhmsLawTarget.entries.first {
                    "Calcular ${it.label.lowercase()}" == option
                }
                calculation = null
                errorMessage = null
            },
        )

        if (target != OhmsLawTarget.VOLTAGE) {
            DecimalInputField(
                value = voltage,
                onValueChange = {
                    voltage = it
                    calculation = null
                    errorMessage = null
                },
                label = "Voltaje (V)",
            )
        }
        if (target != OhmsLawTarget.CURRENT) {
            DecimalInputField(
                value = current,
                onValueChange = {
                    current = it
                    calculation = null
                    errorMessage = null
                },
                label = "Corriente (A)",
            )
        }
        if (target != OhmsLawTarget.RESISTANCE) {
            DecimalInputField(
                value = resistance,
                onValueChange = {
                    resistance = it
                    calculation = null
                    errorMessage = null
                },
                label = "Resistencia (Ω)",
            )
        }

        ActionButtons(
            onCalculate = {
                when (val result = calculateOhmsLaw(target, voltage, current, resistance)) {
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
                title = result.resultLabel,
                value = "${formatNumber(result.value)} ${result.unit}",
                supportingText = result.alternateDisplay,
            )
            ProcedureCard(steps = result.steps)
        }
    }
}
