// app/kotlin+java/com/example/movielist/navigation/Routes.kt
package com.example.movielist.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SPLASH = "splash"

    // Bottom nav destinations
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"

    // Details (keep for later)
    const val DETAILS = "details/{movieId}"

    fun details(movieId: Int) = "details/$movieId"
}