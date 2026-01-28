package com.example.movielist.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(movie: MovieEntity)

    @Delete
    suspend fun removeFavorite(movie: MovieEntity)

    @Query("SELECT * FROM favorite_movies WHERE userId = :userId")
    fun getFavorites(userId: String): Flow<List<MovieEntity>>

    @Query(
        "SELECT EXISTS(" +
                "SELECT 1 FROM favorite_movies WHERE id = :movieId AND userId = :userId)"
    )
    fun isFavorite(movieId: Int, userId: String): Flow<Boolean>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId AND userId = :userId LIMIT 1")
    suspend fun getMovieById(movieId: Int, userId: String): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)
}
