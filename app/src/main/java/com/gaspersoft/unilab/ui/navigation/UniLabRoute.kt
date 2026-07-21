package com.gaspersoft.unilab.ui.navigation

sealed class UniLabRoute(
    val route: String,
    val title: String,
) {
    data object Home : UniLabRoute(route = "home", title = "UniLab")

    data object Electronics : UniLabRoute(route = "electronics", title = "Electrónica")

    data object OhmsLaw : UniLabRoute(route = "ohms-law", title = "Ley de Ohm")

    data object SeriesResistance : UniLabRoute(
        route = "series-resistance",
        title = "Resistencias en serie",
    )

    data object ParallelResistance : UniLabRoute(
        route = "parallel-resistance",
        title = "Resistencias en paralelo",
    )

    data object Converters : UniLabRoute(route = "converters", title = "Conversores")

    data object Tools : UniLabRoute(route = "tools", title = "Herramientas")

    companion object {
        fun fromRoute(route: String?): UniLabRoute =
            when (route) {
                Home.route -> Home
                Electronics.route -> Electronics
                OhmsLaw.route -> OhmsLaw
                SeriesResistance.route -> SeriesResistance
                ParallelResistance.route -> ParallelResistance
                Converters.route -> Converters
                Tools.route -> Tools
                else -> Home
            }
    }
}
