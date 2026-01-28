package com.example.movielist.ui.details

import com.example.movielist.data.remote.MovieDetailsDto

data class MovieDetailsUiState(
    val isLoading: Boolean = true,
    val movie: MovieDetailsDto? = null,
    val isFavorite: Boolean = false,
    val selectedTab: Int = 0,
    val error: String? = null
)
