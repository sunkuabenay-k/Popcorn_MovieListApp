package com.example.movielist.ui.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.movielist.data.remote.MovieDto
import com.example.movielist.ui.home.HomeViewModel

@Composable
fun MovieRow(
    movies: List<MovieDto>,
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    LazyRow {
        items(movies) { movie ->
            MovieCard(
                movie = movie,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
