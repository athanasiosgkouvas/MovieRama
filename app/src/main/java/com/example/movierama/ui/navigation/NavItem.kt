package com.example.movierama.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    internal val baseRoute: String,
    private val navArgs: List<NavArg> = emptyList()
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute)
            .plus(argKeys)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }

    object Splash : NavItem("splash")
    object Home : NavItem("home")
    object MovieDetails : NavItem("movieDetails", listOf(NavArg.MovieId)){
        fun createNavRoute(movieId: Int) = "$baseRoute/$movieId"
    }
}

enum class NavArg(val key: String, val navType: NavType<*>) {
    MovieId("id", NavType.IntType)
}