package com.example.movielist.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.movielist.ui.auth.AuthState
import com.example.movielist.ui.home.HomeScreen
import com.example.movielist.ui.profile.ProfileScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    authState: AuthState
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(viewModel = authViewModel)
        }

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(onLogout = authViewModel::logout)
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated ->
                navController.navigate(Routes.HOME) {
                    popUpTo(0)
                }

            AuthState.Unauthenticated ->
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0)
                }

            else -> Unit
        }
    }
}
