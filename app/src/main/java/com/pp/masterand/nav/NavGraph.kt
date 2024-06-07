package com.pp.masterand.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

        composable(route = Screen.LoginScreen.route) {
            LoginActivity(navController = navController)
        }

        composable(
            route = Screen.GameScreen.route + "/{numberOfColorsInPool}",
            arguments = listOf(navArgument("numberOfColorsInPool") { type = NavType.IntType })
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
            arguments = listOf(navArgument("scoreNumber") { type = NavType.IntType })
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
