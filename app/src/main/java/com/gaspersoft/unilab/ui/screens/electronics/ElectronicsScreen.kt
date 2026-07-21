package com.gaspersoft.unilab.ui.screens.electronics

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gaspersoft.unilab.ui.components.FeatureCard
import com.gaspersoft.unilab.ui.components.ScreenContainer
import com.gaspersoft.unilab.ui.components.SectionHeadline

@Composable
fun ElectronicsScreen(
    onOhmsLawClick: () -> Unit,
    onSeriesClick: () -> Unit,
    onParallelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenContainer(modifier = modifier) {
        SectionHeadline(
            title = "Electrónica",
            description = "Selecciona una herramienta para resolver ejercicios básicos y revisar el procedimiento.",
        )
        FeatureCard(
            title = "Ley de Ohm",
            description = "Calcula voltaje, corriente o resistencia con V = I × R.",
            onClick = onOhmsLawClick,
        )
        FeatureCard(
            title = "Resistencias en serie",
            description = "Suma múltiples resistencias y observa la conversión de unidades paso a paso.",
            onClick = onSeriesClick,
        )
        FeatureCard(
            title = "Resistencias en paralelo",
            description = "Calcula el equivalente para dos o más resistencias con el desarrollo matemático.",
            onClick = onParallelClick,
        )
    }
}
