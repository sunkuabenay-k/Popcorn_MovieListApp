package com.example.movielist.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SPLASH = "splash"

    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"

    const val DETAILS = "details/{movieId}"

    fun details(movieId: Int) = "details/$movieId"
}