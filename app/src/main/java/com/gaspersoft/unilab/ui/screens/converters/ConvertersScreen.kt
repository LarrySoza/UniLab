package com.gaspersoft.unilab.ui.screens.converters

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gaspersoft.unilab.ui.components.ConverterPanel
import com.gaspersoft.unilab.ui.components.ScreenContainer

@Composable
fun ConvertersScreen(
    modifier: Modifier = Modifier,
) {
    ScreenContainer(modifier = modifier) {
        ConverterPanel(
            title = "Conversores",
            description = "Elige la magnitud, las unidades y obtiene el resultado con el procedimiento intermedio.",
        )
    }
}
