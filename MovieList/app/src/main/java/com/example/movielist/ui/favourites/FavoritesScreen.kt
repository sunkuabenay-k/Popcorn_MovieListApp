@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.movielist.ui.favourites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.navigation.BottomNavItem
import com.example.movielist.ui.components.BottomNavigationBar

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesViewModel
) {
    // ✅ Type is now known
    val favorites: List<MovieEntity> by viewModel
        .favoriteMovies
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
                Text("No favourites yet ❤️", fontSize = 16.sp)
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
                items(
                    items = favorites,
                    key = { movie -> movie.id } // ✅ movie is MovieEntity
                ) { movie ->
                    FavoriteMovieCard(
                        movie = movie,
                        navController = navController,
                        onDeleteClick = {
                            viewModel.removeFromFavorites(it)
                        }
                    )
                }
            }
        }
    }
}
