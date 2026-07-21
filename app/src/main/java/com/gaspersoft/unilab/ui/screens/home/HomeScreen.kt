package com.gaspersoft.unilab.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gaspersoft.unilab.ui.components.FeatureCard
import com.gaspersoft.unilab.ui.components.InfoBadge
import com.gaspersoft.unilab.ui.components.ScreenContainer
import com.gaspersoft.unilab.ui.components.SectionHeadline

@Composable
fun HomeScreen(
    onElectronicsClick: () -> Unit,
    onConvertersClick: () -> Unit,
    onToolsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenContainer(modifier = modifier) {
        SectionHeadline(
            title = "UniLab",
            description = "Herramientas para estudiantes de ingeniería",
        )
        InfoBadge(text = "Funciona sin conexión")
        FeatureCard(
            title = "Electrónica",
            description = "Ley de Ohm, resistencias en serie y resistencias en paralelo.",
            onClick = onElectronicsClick,
        )
        FeatureCard(
            title = "Conversores",
            description = "Convierte voltaje, corriente, resistencia, frecuencia y potencia.",
            onClick = onConvertersClick,
        )
        FeatureCard(
            title = "Herramientas",
            description = "Consulta prefijos SI y usa una conversión rápida para estudio o laboratorio.",
            onClick = onToolsClick,
        )
    }
}
