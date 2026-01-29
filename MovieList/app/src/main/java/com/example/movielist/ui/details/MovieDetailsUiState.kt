package com.example.movielist.ui.details

import com.example.movielist.data.remote.MovieDetailsDto
import com.example.movielist.data.remote.MovieDto

data class MovieDetailsUiState(
    val isLoading: Boolean = true,
    val movie: MovieDetailsDto? = null,
    val isFavorite: Boolean = false,
    val selectedTab: Int = 0,
    val similarMovies: List<MovieDto> = emptyList(),
    val error: String? = null
)
