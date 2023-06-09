package com.example.movierama.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.movierama.ui.screens.homeScreen.HomeScreen
import com.example.movierama.ui.screens.movieDetailsScreen.MovieDetailsScreen
import com.example.movierama.ui.screens.splashScreen.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavItem.Splash.route) {
        composable(NavItem.Splash) {
            SplashScreen {
                navController.navigate(
                    NavItem.Home.route,
                    navOptions = navOptions {
                        popUpTo(NavItem.Splash.route) {
                            this.inclusive = true
                        }
                    }
                )
            }
        }
        composable(NavItem.Home) {
            HomeScreen(navigateToMovieDetails = {
                navController.navigate(
                    NavItem.MovieDetails.createNavRoute(
                        it
                    )
                )
            })
        }
        composable(NavItem.MovieDetails) {
            MovieDetailsScreen(movieId = it.findArg(NavArg.MovieId.key), navHostController = navController)
        }
    }
}


private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args
    ) {
        content(it)
    }
}

private inline fun <reified T> NavBackStackEntry.findArg(key: String): T {
    val value = arguments?.get(key)
    requireNotNull(value)
    return value as T
}