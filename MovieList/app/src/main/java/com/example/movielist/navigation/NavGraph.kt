package com.example.movielist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movielist.ui.auth.AuthState
import com.example.movielist.ui.auth.AuthViewModel
import com.example.movielist.ui.auth.AuthViewModelFactory
import com.example.movielist.ui.auth.LoginScreen
import com.example.movielist.ui.auth.RegisterScreen
import com.example.movielist.ui.auth.SplashScreen
import com.example.movielist.ui.home.HomeScreen
import com.example.movielist.ui.profile.ProfileScreen
import com.example.movielist.repository.UserRepository

@Composable
fun AppNavGraph(
    navController: NavHostController,
    userRepository: UserRepository
) {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userRepository)
    )
    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(navController, authViewModel)
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
    }

    // Handle navigation based on auth state
    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated -> {
                // Navigate to home if not already there
                if (navController.currentDestination?.route != Routes.HOME) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
            AuthState.Unauthenticated -> {
                // Navigate to login if not already there
                if (navController.currentDestination?.route != Routes.LOGIN) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
            AuthState.Loading -> {
                // Stay on splash
            }
        }
    }
}