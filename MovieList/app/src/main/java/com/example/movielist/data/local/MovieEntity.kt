package com.example.movielist.data.local

import androidx.room.Entity

@Entity(
    tableName = "favorite_movies",
    primaryKeys = ["id", "userId"]
)
data class MovieEntity(
    val id: Int,
    val userId: String,
    val title: String,
    val posterPath: String?,
    val rating: Double
)
