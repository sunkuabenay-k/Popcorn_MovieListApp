@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movielist.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun MovieDetailsScreen(
    navController: NavHostController,
    viewModel: MovieDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.movie == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Failed to load movie details")
            }
        }

        else -> {
            val movie = uiState.movie!!  // ✅ SAFE UNWRAP

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
                                    contentDescription = "Favorite",
                                    tint = if (uiState.isFavorite)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
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
                        Text(
                            buildMetaText(movie),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("▶ Play Now")
                        }
                    }

                    item {
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = viewModel::toggleFavorite,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                if (uiState.isFavorite)
                                    "Remove from My List"
                                else
                                    "Add to My List"
                            )
                        }
                    }

                    item {
                        Spacer(Modifier.height(20.dp))
                        Text("Description", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }

                    item {
                        Spacer(Modifier.height(6.dp))
                        Text(movie.overview, fontSize = 14.sp, lineHeight = 20.sp)
                    }

                    item { Spacer(Modifier.height(24.dp)) }
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
