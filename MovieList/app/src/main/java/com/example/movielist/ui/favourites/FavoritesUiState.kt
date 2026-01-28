package com.example.movielist.ui.favourites

import com.example.movielist.data.local.MovieEntity

data class FavoritesUiState(
    val favorites: List<MovieEntity> = emptyList(),
    val isLoading: Boolean = false
)
