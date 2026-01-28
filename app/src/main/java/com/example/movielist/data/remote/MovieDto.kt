package com.example.movielist.data.remote

data class MovieDto(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val vote_average: Double
)

