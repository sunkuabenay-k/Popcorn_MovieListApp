package com.example.movielist.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(movie: MovieEntity)

    @Delete
    suspend fun removeFavorite(movie: MovieEntity)

    @Query("SELECT * FROM favorite_movies")
    fun getFavorites(): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    fun isFavorite(movieId: Int): Flow<Boolean>
}
