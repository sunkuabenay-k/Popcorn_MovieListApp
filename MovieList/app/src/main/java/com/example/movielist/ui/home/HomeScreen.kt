package com.example.movielist.ui.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movielist.BuildConfig
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.data.remote.MovieDto
import com.example.movielist.data.remote.RetrofitInstance
import com.example.movielist.navigation.BottomNavItem
import com.example.movielist.navigation.Routes
import com.example.movielist.repository.MovieRepositoryImpl
import com.example.movielist.ui.components.BottomNavigationBar
import kotlinx.coroutines.launch

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun HomeScreen(
    navController: NavHostController,
    movieRepository: MovieRepositoryImpl
) {
    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )
    var userId by remember { mutableStateOf<String?>(null) }


    var trendingMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }
    var topRatedMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }

    LaunchedEffect(Unit) {
        userId = movieRepository.currentUserId()
        try {
            trendingMovies = RetrofitInstance.api
                .getTrendingMovies(BuildConfig.TMDB_API_KEY)
                .results

            topRatedMovies = RetrofitInstance.api
                .getTopRatedMovies(BuildConfig.TMDB_API_KEY)
                .results
        } catch (e: Exception) {
            Log.e("TMDB", "API ERROR", e)
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = bottomItems
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Good morning, Cinephile!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Time to discover new stories",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                SectionTitle("What's Trending")
                MovieRow(trendingMovies, navController, movieRepository,userId)
            }

            item {
                SectionTitle("Critically Acclaimed")
                MovieRow(topRatedMovies, navController, movieRepository,userId)
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun MovieRow(
    movies: List<MovieDto>,
    navController: NavHostController,
    movieRepository: MovieRepositoryImpl,
    userId: String?
) {
    LazyRow {
        items(movies) { movie ->
            MovieCard(movie,navController, movieRepository,userId)
        }
    }
}

@Composable
private fun MovieCard(
    movie: MovieDto,
    navController: NavHostController,
    movieRepository: MovieRepositoryImpl,
    userId: String?
) {
    val scope = rememberCoroutineScope()
    val isFavorite by movieRepository
        .isFavorite(movie.id)
        .collectAsState(initial = false)

    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(end = 12.dp)
            .clickable {
                navController.navigate(
                    Routes.details(movie.id)
                )
            },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {

            if (movie.poster_path != null) {
                AsyncImage(
                    model = IMAGE_BASE_URL + movie.poster_path,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .height(68.dp)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = movie.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    lineHeight = 16.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", movie.vote_average),
                            fontSize = 12.sp
                        )
                    }

                    IconButton(
                        onClick = {
                            val uid = userId ?: run {
                                Log.d("FAV_DEBUG", "userId is NULL, ignoring click")
                                return@IconButton
                            }
                            Log.d("FAV_DEBUG", "Clicked favorite for movie=${movie.id}, user=$uid")
                            scope.launch {
                                if (isFavorite) {
                                    movieRepository.removeFromFavorites(movie.toEntity(uid))
                                } else {
                                    movieRepository.addToFavorites(movie.toEntity(uid))
                                }
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (userId != null && isFavorite)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

private fun MovieDto.toEntity(userId: String): MovieEntity {
    return MovieEntity(
        id = id,
        userId = userId,
        title = title,
        posterPath = poster_path,
        rating = vote_average
    )
}
