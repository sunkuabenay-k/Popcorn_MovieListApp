package com.example.movielist.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movielist.ui.auth.LoginScreen
import com.example.movielist.ui.auth.AuthViewModel
import com.example.movielist.ui.auth.AuthViewModelFactory
import com.example.movielist.data.repository.UserRepositoryImpl
import com.example.movielist.data.local.AppDatabase
import com.example.movielist.ui.home.HomeScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    database: AppDatabase
) {
    val startDestination =
        if (isLoggedIn) Routes.HOME else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.LOGIN) {

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(
                    userRepository = UserRepositoryImpl(
                        userDao = database.userDao()
                    )
                )
            )

            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(Routes.FAVORITES) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Favorites")
            }
        }

        composable(Routes.PROFILE) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Profile")
            }
        }
    }
}
