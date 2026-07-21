package com.gaspersoft.unilab.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gaspersoft.unilab.ui.screens.converters.ConvertersScreen
import com.gaspersoft.unilab.ui.screens.electronics.ElectronicsScreen
import com.gaspersoft.unilab.ui.screens.electronics.OhmsLawScreen
import com.gaspersoft.unilab.ui.screens.electronics.ParallelResistanceScreen
import com.gaspersoft.unilab.ui.screens.electronics.SeriesResistanceScreen
import com.gaspersoft.unilab.ui.screens.home.HomeScreen
import com.gaspersoft.unilab.ui.screens.tools.ToolsScreen
import com.gaspersoft.unilab.ui.theme.UnilabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniLabApp() {
    val backStack = remember {
        mutableStateListOf(UniLabRoute.Home.route)
    }
    val currentRoute = UniLabRoute.fromRoute(backStack.lastOrNull())
    val canNavigateBack = backStack.size > 1

    fun navigateTo(route: UniLabRoute) {
        if (backStack.lastOrNull() != route.route) {
            backStack.add(route.route)
        }
    }

    fun navigateUp() {
        if (canNavigateBack) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    BackHandler(enabled = canNavigateBack) {
        navigateUp()
    }

    UnilabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = currentRoute.title) },
                    navigationIcon = {
                        if (canNavigateBack) {
                            TextButton(onClick = { navigateUp() }) {
                                Text(text = "Atrás")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { innerPadding ->
            when (currentRoute) {
                UniLabRoute.Home -> HomeScreen(
                    onElectronicsClick = { navigateTo(UniLabRoute.Electronics) },
                    onConvertersClick = { navigateTo(UniLabRoute.Converters) },
                    onToolsClick = { navigateTo(UniLabRoute.Tools) },
                    modifier = Modifier.padding(innerPadding),
                )

                UniLabRoute.Electronics -> ElectronicsScreen(
                    onOhmsLawClick = { navigateTo(UniLabRoute.OhmsLaw) },
                    onSeriesClick = { navigateTo(UniLabRoute.SeriesResistance) },
                    onParallelClick = { navigateTo(UniLabRoute.ParallelResistance) },
                    modifier = Modifier.padding(innerPadding),
                )

                UniLabRoute.OhmsLaw -> OhmsLawScreen(
                    modifier = Modifier.padding(innerPadding),
                )

                UniLabRoute.SeriesResistance -> SeriesResistanceScreen(
                    modifier = Modifier.padding(innerPadding),
                )

                UniLabRoute.ParallelResistance -> ParallelResistanceScreen(
                    modifier = Modifier.padding(innerPadding),
                )

                UniLabRoute.Converters -> ConvertersScreen(
                    modifier = Modifier.padding(innerPadding),
                )

                UniLabRoute.Tools -> ToolsScreen(
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}
