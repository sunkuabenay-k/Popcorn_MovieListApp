@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movielist.ui.favourites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.navigation.BottomNavItem
import com.example.movielist.repository.MovieRepositoryImpl
import com.example.movielist.ui.components.BottomNavigationBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import com.example.movielist.navigation.Routes


private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    movieRepository: MovieRepositoryImpl
) {
    val favorites by movieRepository
        .getFavoriteMovies()
        .collectAsState(initial = emptyList())

    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Favorites",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* optional future action */ }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = bottomItems
            )
        }
    ) { padding ->

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No favourites yet ❤️", fontSize = 18.sp)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites) { movie ->
                    FavoriteMovieCard(movie,navController)
                }
            }
        }
    }
}

@Composable
private fun FavoriteMovieCard(movie: MovieEntity,navController: NavHostController,) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                navController.navigate(
                    Routes.details(movie.id)
                )
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {

            if (movie.posterPath != null) {
                AsyncImage(
                    model = IMAGE_BASE_URL + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = movie.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = movie.rating.toString(),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

