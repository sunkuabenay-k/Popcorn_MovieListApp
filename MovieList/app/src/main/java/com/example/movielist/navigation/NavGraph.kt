package com.example.movielist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movielist.repository.MovieRepositoryImpl
import com.example.movielist.repository.UserRepository
import com.example.movielist.ui.auth.*
import com.example.movielist.ui.favourites.FavoritesScreen
import com.example.movielist.ui.home.HomeScreen
import com.example.movielist.ui.profile.ProfileScreen
import com.example.movielist.ui.details.MovieDetailsScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    userRepository: UserRepository,
    movieRepository: MovieRepositoryImpl
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
            HomeScreen(
                navController = navController,
                movieRepository = movieRepository
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                navController = navController,
                movieRepository = movieRepository
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        composable(
            route = Routes.DETAILS,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable

            MovieDetailsScreen(
                navController = navController,
                movieId = movieId,
                movieRepository = movieRepository
            )
        }


    }

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated -> {
                if (navController.currentDestination?.route != Routes.HOME) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
            AuthState.Unauthenticated -> {
                if (navController.currentDestination?.route != Routes.LOGIN) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
            AuthState.Loading -> Unit
        }
    }
}