package com.example.movielist.data.local
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)
    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)
    // Inner Join to get actual Movie objects for a user
    @Query(""" 
            SELECT * FROM movies  
            INNER JOIN favorites ON movies.id = favorites.movieId  
            WHERE favorites.userId = :userId 
            """)
    fun getUserFavorites(userId: Int): Flow<List<MovieEntity>>
}