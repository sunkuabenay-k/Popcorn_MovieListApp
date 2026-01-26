package com.example.movielist.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movielist.ui.components.ErrorView
import com.example.movielist.ui.components.LoadingView
import com.example.movielist.ui.components.MovieCard
import com.example.movielist.util.UiState

@Composable
fun MovieDetailScreen(viewModel: MovieDetailViewModel, movieId: Int) {
    val uiState by viewModel.movie.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    when (val state = uiState) {
        is UiState.Loading -> LoadingView()
        is UiState.Error -> ErrorView(state.message)
        is UiState.Success -> {
            val movie = state.data
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w780${movie.posterPath}",
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier =
                        Modifier.fillMaxWidth()) {
                        Text(movie.title, style = MaterialTheme.typography.headlineMedium, modifier =
                            Modifier.weight(1f))
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text("Rating: ${movie.voteAverage}", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(movie.overview, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("You might also like:", style = MaterialTheme.typography.titleMedium)
                    LazyRow {
                        items(suggestions) { suggestion ->
                            // Simplified card for row
                            Card(modifier = Modifier.padding(8.dp).width(120.dp)) {
                                AsyncImage(model =
                                    "https://image.tmdb.org/t/p/w300${suggestion.posterPath}", contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}