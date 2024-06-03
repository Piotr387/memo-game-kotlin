package com.pp.masterand.nav

sealed class Screen(val route: String) {

    object LoginScreen: Screen(route = "login_profile")
    object GameScreen: Screen(route = "game_screen")
    object ProfileScreen: Screen(route = "profile_screen")

}