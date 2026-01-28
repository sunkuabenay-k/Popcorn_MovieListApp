package com.example.movielist.navigation

import androidx.annotation.DrawableRes
import com.example.movielist.R

sealed class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int
) {
    object Home : BottomNavItem(
        route = Routes.HOME,
        label = "Home",
        icon = R.drawable.home_24px
    )

    object Favorites : BottomNavItem(
        route = Routes.FAVORITES,
        label = "Favorites",
        icon = R.drawable.favorite_24px
    )

    object Profile : BottomNavItem(
        route = Routes.PROFILE,
        label = "Profile",
        icon = R.drawable.account_circle_24px
    )
}
