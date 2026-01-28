package com.example.movielist.data.remote

data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String,
    val runtime: Int?,
    val release_date: String?,
    val vote_average: Double,
    val backdrop_path: String?,
    val poster_path: String?,
    val genres: List<GenreDto>
)

data class GenreDto(
    val id: Int,
    val name: String
)
