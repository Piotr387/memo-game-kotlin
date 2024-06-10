package com.pp.masterand.nav

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pp.masterand.data.*
import com.pp.masterand.game.GameScreen
import com.pp.masterand.login.LoginActivity
import com.pp.masterand.profile.ProfileWithScoreTable

@Composable
fun SetupNavGraph(
    navController: NavHostController //
//    , appContainer: AppContainer
) {

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val gameViewModel: GameViewModel = hiltViewModel()

//    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(appContainer.playersRepository, appContainer.scoresRepository))
//    val gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory(appContainer.scoresRepository))

    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
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
            LoginActivity(navController = navController, profileViewModel = profileViewModel)
        }

        composable(
            route = Screen.GameScreen.route + "/{numberOfColorsInPool}/{playerId}",
            arguments = listOf(
                navArgument("numberOfColorsInPool") { type = NavType.IntType },
                navArgument("playerId") { type = NavType.LongType }
            ),
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) { backStackEntry ->

            val numberOfColorsInPoolFromLoginActivity = backStackEntry.arguments?.getInt("numberOfColorsInPool")!!
            val playerId = backStackEntry.arguments?.getLong("playerId")!!

            GameScreen(
                navController = navController,
                numberOfColorsInPool = numberOfColorsInPoolFromLoginActivity,
                onLogoutButtonAction = onLogoutButtonClick,
                gameViewModel = gameViewModel,
                playerId = playerId
            )
        }

        composable(
            route = Screen.ProfileScreen.route + "/{scoreNumber}/{playerId}",
            arguments = listOf(
                navArgument("scoreNumber") { type = NavType.IntType },
                navArgument("playerId") { type = NavType.LongType }
            ),
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) { backStackEntry ->

            val scoreNumber = backStackEntry.arguments?.getInt("scoreNumber")!!
            val playerId = backStackEntry.arguments?.getLong("playerId")!!

            profileViewModel.getScoresByPlayer(playerId)
            profileViewModel.getPlayerDetails(playerId)
            val scores by profileViewModel.scores.collectAsState()
            val player by profileViewModel.player.collectAsState()

            player?.let {
                ProfileWithScoreTable(
                    navController = navController,
                    scoreNumber = scoreNumber,
                    onButtonClicked = { navController.navigateUp() },
                    onLogoutButtonAction = onLogoutButtonClick,
                    scores = scores,
                    name = it.name,
                    email = it.email,
                    imageUri = it.imageUri
                )
            }
        }
    }
}