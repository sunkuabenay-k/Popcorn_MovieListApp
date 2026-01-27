package com.example.movielist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String?,
    val voteAverage: Double,
    val releaseDate: String = "",
    val backdropPath: String? = null,
    val popularity: Double = 0.0,
    val voteCount: Int = 0,
    val adult: Boolean = false,
    val originalLanguage: String = "",
    val originalTitle: String = ""
)