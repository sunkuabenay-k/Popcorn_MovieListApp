package com.yourapp.movielist.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.yourapp.movielist.navigation.Routes
import com.yourapp.movielist.ui.components.ErrorView
import com.yourapp.movielist.ui.components.LoadingView
import com.yourapp.movielist.ui.components.MovieCard
import com.yourapp.movielist.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val uiState by viewModel.movies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Popular Movies") },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.Favorites) }) {
                        Icon(Icons.Default.Favorite, "Favorites")
                    }
                    IconButton(onClick = { navController.navigate(Routes.Profile) }) {
                        Icon(Icons.Default.Person, "Profile")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = uiState) {
                is UiState.Loading -> LoadingView()
                is UiState.Error -> ErrorView(state.message)
                is UiState.Success -> {
                    LazyColumn {
                        items(state.data) { movie ->
                            MovieCard(movie) {
                                navController.navigate(Routes.Detail.replace("{movieId}", movie.id.toString()))
                            }
                        }
                    }
                }
            }
        }
    }
}