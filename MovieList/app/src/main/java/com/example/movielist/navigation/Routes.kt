package com.example.movielist.navigation

object Routes {
    const val LOGIN = "login"

    // Bottom nav destinations
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"

    // Details (keep for later)
    const val DETAILS = "details/{movieId}"

    fun details(movieId: Int) = "details/$movieId"
}
