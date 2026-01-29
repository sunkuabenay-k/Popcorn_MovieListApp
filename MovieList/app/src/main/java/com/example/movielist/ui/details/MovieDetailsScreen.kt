@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movielist.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movielist.ui.components.SimilarMovieCard

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun MovieDetailsScreen(
    navController: NavHostController,
    viewModel: MovieDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.movie == null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Failed to load movie details")
            }
        }

        else -> {
            val movie = uiState.movie!!

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(movie.title, maxLines = 1) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        actions = {
                            IconButton(onClick = viewModel::toggleFavorite) {
                                Icon(
                                    imageVector = if (uiState.isFavorite)
                                        Icons.Filled.Favorite
                                    else
                                        Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite"
                                )
                            }
                        }
                    )
                }
            ) { padding ->

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                ) {

                    item {
                        Spacer(Modifier.height(8.dp))
                        AsyncImage(
                            model = IMAGE_BASE_URL + movie.backdrop_path,
                            contentDescription = movie.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                        Text(movie.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    item {
                        Spacer(Modifier.height(6.dp))
                        Text(buildMetaText(movie), fontSize = 13.sp)
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                            Text("▶ Play Now")
                        }
                    }

                    item {
                        Spacer(Modifier.height(20.dp))
                        Text("Description", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }

                    item {
                        Spacer(Modifier.height(6.dp))
                        Text(movie.overview, fontSize = 14.sp)
                    }

                    if (uiState.similarMovies.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(24.dp))
                            Text(
                                "More Like This",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        item {
                            Spacer(Modifier.height(12.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(uiState.similarMovies) { similar ->
                                    SimilarMovieCard(
                                        movie = similar,
                                        navController = navController
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(Modifier.height(32.dp)) }
                }
            }
        }
    }
}

private fun buildMetaText(movie: com.example.movielist.data.remote.MovieDetailsDto): String {
    val year = movie.release_date?.take(4) ?: "—"
    val runtime = movie.runtime?.let { "${it / 60}h ${it % 60}m" } ?: ""
    val genre = movie.genres.firstOrNull()?.name ?: ""
    val match = (movie.vote_average * 10).toInt()

    return "$match% Match · $year · $genre · $runtime"
}
