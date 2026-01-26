package com.example.movielist.data.repository
import com.example.movielist.data.local.FavoriteDao
import com.example.movielist.data.local.FavoriteEntity
import com.example.movielist.data.local.MovieDao
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.data.remote.MovieApi
import com.example.movielist.data.remote.NetworkMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepository(
    private val api: MovieApi,
    private val movieDao: MovieDao,
    private val favoriteDao: FavoriteDao
) {
    private val apiKey = "YOUR_TMDB_API_KEY_HERE" // REPLACE THIS!

    suspend fun getPopularMovies(): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPopularMovies(apiKey)
                val entities = response.results.map { it.toEntity() }
                movieDao.insertMovies(entities) // Cache
                entities
            } catch (e: Exception) {
                // If offline, you might query DB here, simplified for now
                emptyList()
            }
        }
    }

    suspend fun getMovieDetail(id: Int): MovieEntity? {
        return withContext(Dispatchers.IO) {
            try {
                val networkMovie = api.getMovieDetail(id, apiKey)
                val entity = networkMovie.toEntity()
                movieDao.insertMovie(entity)
                entity
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getSuggestions(id: Int): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getSimilarMovies(id, apiKey)
                response.results.map { it.toEntity() }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun getFavorites(userId: Int): Flow<List<MovieEntity>> {
        return favoriteDao.getUserFavorites(userId)
    }

    suspend fun toggleFavorite(userId: Int, movie: MovieEntity, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            if (isFavorite) {
                movieDao.insertMovie(movie) // Ensure movie exists in DB
                favoriteDao.addFavorite(FavoriteEntity(userId, movie.id))
            } else {
                favoriteDao.removeFavorite(FavoriteEntity(userId, movie.id))
            }
        }
    }
    // Mapper extension
    private fun NetworkMovie.toEntity() = MovieEntity(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        voteAverage = voteAverage
    )
}