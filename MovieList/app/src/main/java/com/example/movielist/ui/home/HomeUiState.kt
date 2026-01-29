package com.example.movielist.ui.home

import com.example.movielist.data.remote.MovieDto

data class HomeUiState(
    val isLoading: Boolean = true,
    val trendingMovies: List<MovieDto> = emptyList(),
    val topRatedMovies: List<MovieDto> = emptyList(),

    val filteredTrending: List<MovieDto> = emptyList(),
    val filteredTopRated: List<MovieDto> = emptyList(),

    val searchQuery: String = "",
    val minRatingFilter: Double = 0.0,

    val userId: String? = null,
    val error: String? = null,

    val recommendedMovies: List<MovieDto> = emptyList()
)
