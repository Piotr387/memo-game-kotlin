package com.pp.masterand.nav

const val LOGIN_ROUTE = "login_profile"
const val GAME_ROUTE = "game_screen"
const val PROFILE_ROUTE = "profile_screen"

sealed class Screen(val route: String) {

    object LoginScreen : Screen(route = LOGIN_ROUTE)
    object GameScreen : Screen(route = GAME_ROUTE)
    object ProfileScreen : Screen(route = PROFILE_ROUTE)

}