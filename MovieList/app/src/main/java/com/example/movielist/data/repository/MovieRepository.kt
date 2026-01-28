package com.example.movielist.repository

import com.example.movielist.data.local.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun addToFavorites(movie: MovieEntity)
    suspend fun removeFromFavorites(movie: MovieEntity)
    fun getFavoriteMovies(): Flow<List<MovieEntity>>
    fun isFavorite(movieId: Int): Flow<Boolean>
    suspend fun isFavoriteOnce(movieId: Int): Boolean
    suspend fun currentUserId(): String
}
