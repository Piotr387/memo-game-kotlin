package com.pp.masterand.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pp.masterand.game.GameScreen
import com.pp.masterand.login.LoginActivity
import com.pp.masterand.profile.ProfileWithScoreTable

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login_profile"
    ) {

        composable(route = Screen.LoginScreen.route) {
            //Co ma się zdarzyć po przejściu do tego ekranu
            LoginActivity(navController = navController)
        }

        composable(route = Screen.GameScreen.route) {
            GameScreen(navController = navController)
        }

        composable(route = Screen.ProfileScreen.route) {
            ProfileWithScoreTable(navController = navController)
        }
    }
}