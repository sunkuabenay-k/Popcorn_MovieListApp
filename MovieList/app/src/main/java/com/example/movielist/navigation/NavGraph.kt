package com.example.movielist.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movielist.repository.MovieRepositoryImpl
import com.example.movielist.repository.UserRepository
import com.example.movielist.ui.auth.*
import com.example.movielist.ui.details.MovieDetailsScreen
import com.example.movielist.ui.details.MovieDetailsViewModel
import com.example.movielist.ui.details.MovieDetailsViewModelFactory
import com.example.movielist.ui.favourites.FavoritesScreen
import com.example.movielist.ui.favourites.FavoritesViewModel
import com.example.movielist.ui.favourites.FavoritesViewModelFactory
import com.example.movielist.ui.home.HomeScreen
import com.example.movielist.ui.home.HomeViewModel
import com.example.movielist.ui.home.HomeViewModelFactory
import com.example.movielist.ui.profile.ProfileScreen

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

    // ✅ NAV HOST
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(navController, authViewModel)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController, authViewModel)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController, authViewModel)
        }

        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(movieRepository, userRepository)
            )

            HomeScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }



        composable(Routes.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModelFactory(movieRepository)
            )

            FavoritesScreen(
                navController = navController,
                viewModel = favoritesViewModel
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
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->

            val movieId = backStackEntry.arguments?.getInt("movieId")
                ?: return@composable

            val detailsViewModel: MovieDetailsViewModel = viewModel(
                factory = MovieDetailsViewModelFactory(
                    movieId = movieId,
                    movieRepository = movieRepository
                )
            )

            MovieDetailsScreen(
                navController = navController,
                viewModel = detailsViewModel
            )
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated -> {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }

            AuthState.Unauthenticated -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }

            AuthState.Loading -> Unit
        }
    }
}
