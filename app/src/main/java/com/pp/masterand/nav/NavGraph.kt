package com.pp.masterand.nav

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pp.masterand.game.GameScreen
import com.pp.masterand.login.LoginActivity
import com.pp.masterand.profile.ProfileWithScoreTable

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = LOGIN_ROUTE
    ) {

        val onLogoutButtonClick: () -> Unit = { navController.navigate(route = Screen.LoginScreen.route) }
        val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
            fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300, easing = LinearOutSlowInEasing)
            )
        }

        val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
            fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300, easing = FastOutLinearInEasing)
            )
        }

        composable(
            route = Screen.LoginScreen.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {
            LoginActivity(navController = navController)
        }

        composable(
            route = Screen.GameScreen.route + "/{numberOfColorsInPool}",
            arguments = listOf(navArgument("numberOfColorsInPool") { type = NavType.IntType }),
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) { backStackEntry ->

            val numberOfColorsInPoolFromLoginActivity = backStackEntry.arguments?.getInt("numberOfColorsInPool")!!

            GameScreen(
                navController = navController,
                numberOfColorsInPool = numberOfColorsInPoolFromLoginActivity,
                onLogoutButtonAction = onLogoutButtonClick
            )
        }

        composable(
            route = Screen.ProfileScreen.route + "/{scoreNumber}",
            arguments = listOf(navArgument("scoreNumber") { type = NavType.IntType }),
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) { backStackEntry ->

            val scoreNumber = backStackEntry.arguments?.getInt("scoreNumber")!!

            ProfileWithScoreTable(
                navController = navController,
                scoreNumber = scoreNumber,
                onButtonClicked = { navController.navigateUp() },
                onLogoutButtonAction = onLogoutButtonClick
            )
        }
    }
}
