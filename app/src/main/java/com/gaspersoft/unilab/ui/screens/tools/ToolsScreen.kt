package com.gaspersoft.unilab.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gaspersoft.unilab.domain.model.siPrefixes
import com.gaspersoft.unilab.ui.components.ConverterPanel
import com.gaspersoft.unilab.ui.components.ScreenContainer
import com.gaspersoft.unilab.ui.components.SectionHeadline

@Composable
fun ToolsScreen(
    modifier: Modifier = Modifier,
) {
    ScreenContainer(modifier = modifier) {
        SectionHeadline(
            title = "Herramientas",
            description = "Consulta prefijos SI y realiza una conversión rápida desde la misma pantalla.",
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                Text(
                    text = "Prefijos SI",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                siPrefixes.forEachIndexed { index, prefix ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "${prefix.name} (${prefix.symbol}) = 10^${prefix.exponent}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (index < siPrefixes.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
        ConverterPanel(
            title = "Conversión rápida",
            description = "Utiliza el mismo motor de conversión para verificar unidades durante clase o laboratorio.",
        )
    }
}
