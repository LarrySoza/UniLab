package com.gaspersoft.unilab.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gaspersoft.unilab.domain.calculator.convertCurrent
import com.gaspersoft.unilab.domain.calculator.convertFrequency
import com.gaspersoft.unilab.domain.calculator.convertPower
import com.gaspersoft.unilab.domain.calculator.convertResistance
import com.gaspersoft.unilab.domain.calculator.convertVoltage
import com.gaspersoft.unilab.domain.model.ConversionCalculation
import com.gaspersoft.unilab.domain.model.ConverterType
import com.gaspersoft.unilab.domain.model.OperationResult
import com.gaspersoft.unilab.utils.formatNumber

@Composable
fun ConverterPanel(
    title: String,
    description: String,
    initialType: ConverterType = ConverterType.VOLTAGE,
    modifier: Modifier = Modifier,
) {
    var selectedType by remember { mutableStateOf(initialType) }
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(selectedType.units.first()) }
    var toUnit by remember { mutableStateOf(selectedType.units[1]) }
    var calculation by remember { mutableStateOf<ConversionCalculation?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun resetForType(newType: ConverterType) {
        selectedType = newType
        inputValue = ""
        fromUnit = newType.units.first()
        toUnit = newType.units[1]
        calculation = null
        errorMessage = null
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionHeadline(
            title = title,
            description = description,
        )
        ChoiceChipRow(
            options = ConverterType.entries.map { it.label },
            selectedOption = selectedType.label,
            onOptionSelected = { option ->
                val newType = ConverterType.entries.first { it.label == option }
                resetForType(newType)
            },
        )
        DecimalInputField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                calculation = null
                errorMessage = null
            },
            label = "Valor",
        )
        SelectorField(
            label = "Unidad de origen",
            selectedText = fromUnit.symbol,
            options = selectedType.units.map { it.symbol },
            onOptionSelected = { symbol ->
                fromUnit = selectedType.units.first { it.symbol == symbol }
                calculation = null
                errorMessage = null
            },
        )
        SelectorField(
            label = "Unidad de destino",
            selectedText = toUnit.symbol,
            options = selectedType.units.map { it.symbol },
            onOptionSelected = { symbol ->
                toUnit = selectedType.units.first { it.symbol == symbol }
                calculation = null
                errorMessage = null
            },
        )
        TextButton(
            onClick = {
                val currentFrom = fromUnit
                fromUnit = toUnit
                toUnit = currentFrom
                calculation = null
                errorMessage = null
            },
        ) {
            Text(text = "Intercambiar unidades")
        }
        ActionButtons(
            onCalculate = {
                val result = when (selectedType) {
                    ConverterType.VOLTAGE -> convertVoltage(inputValue, fromUnit, toUnit)
                    ConverterType.CURRENT -> convertCurrent(inputValue, fromUnit, toUnit)
                    ConverterType.RESISTANCE -> convertResistance(inputValue, fromUnit, toUnit)
                    ConverterType.FREQUENCY -> convertFrequency(inputValue, fromUnit, toUnit)
                    ConverterType.POWER -> convertPower(inputValue, fromUnit, toUnit)
                }
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
            onClear = { resetForType(selectedType) },
        )

        errorMessage?.let { ErrorCard(message = it) }
        calculation?.let { conversion ->
            ResultCard(
                title = "Resultado",
                value = "${formatNumber(conversion.resultValue)} ${conversion.resultUnit}",
                supportingText = "Conversión de ${selectedType.label.lowercase()} realizada sin conexión.",
            )
            ProcedureCard(steps = conversion.steps)
        }
    }
}
