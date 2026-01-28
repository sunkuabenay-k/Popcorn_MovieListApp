package com.example.movielist.ui.home

import com.example.movielist.data.remote.MovieDto

data class HomeUiState(
    val isLoading: Boolean = true,
    val trendingMovies: List<MovieDto> = emptyList(),
    val topRatedMovies: List<MovieDto> = emptyList(),
    val userId: String? = null,
    val error: String? = null
)
