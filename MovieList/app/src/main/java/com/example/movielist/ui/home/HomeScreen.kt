package com.example.movielist.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movielist.navigation.BottomNavItem
import com.example.movielist.ui.components.BottomNavigationBar
import com.example.movielist.ui.components.MovieCard
import com.example.movielist.ui.components.MovieRow

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, bottomItems)
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Text("Discover Movies", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            /* 🔍 SEARCH BAR */
            item {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search movies...") },
                    singleLine = true
                )
            }

            /* ⭐ FILTER CHIPS */
            item {
                RatingFilterRow(
                    selected = uiState.minRatingFilter,
                    onSelected = viewModel::onRatingFilterChange
                )
            }

            /* 🔥 TRENDING */
            if (uiState.filteredTrending.isNotEmpty()) {
                item {
                    SectionTitle("Trending")
                }
                item {
                    LazyRow {
                        items(uiState.filteredTrending) { movie ->
                            MovieCard(movie, navController, viewModel)
                        }
                    }
                }
            }

            if (uiState.recommendedMovies.isNotEmpty()) {
                item{
                    SectionTitle("Recommended for You")
                }

                item{
                    MovieRow(
                        movies = uiState.recommendedMovies,
                        navController = navController,
                        viewModel = viewModel
                    )

                }
                item{
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }


            /* 🎯 TOP RATED */
            if (uiState.filteredTopRated.isNotEmpty()) {
                item {
                    SectionTitle("Top Rated")
                }
                item {
                    LazyRow {
                        items(uiState.filteredTopRated) { movie ->
                            MovieCard(movie, navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- UI HELPERS ---------------- */

@Composable
private fun SectionTitle(text: String) {
    Text(text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun RatingFilterRow(
    selected: Double,
    onSelected: (Double) -> Unit
) {
    val ratings = listOf(0.0, 6.0, 7.0, 8.0)

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(ratings) { rating ->
            FilterChip(
                selected = selected == rating,
                onClick = { onSelected(rating) },
                label = {
                    Text(
                        if (rating == 0.0) "All"
                        else "⭐ $rating+"
                    )
                }
            )
        }
    }
}
