package com.example.movielist.repository

import com.example.movielist.data.local.MovieDao
import com.example.movielist.data.local.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(
    private val movieDao: MovieDao,
    private val userRepository: UserRepository
) : MovieRepository {
    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return userRepository.currentUserFlow()
            .flatMapLatest { user ->
                movieDao.isFavorite(
                    movieId = movieId,
                    userId = user.email
                )
            }
    }

    override suspend fun currentUserId(): String {
        return userRepository.getCurrentUser()?.id
            ?: throw IllegalStateException("User not logged in")
    }

    override fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return userRepository.currentUserFlow()
            .flatMapLatest { user ->
                movieDao.getFavorites(userId = user.email)
            }
    }


    override suspend fun isFavoriteOnce(movieId: Int): Boolean {
        return movieDao.getMovieById(movieId, currentUserId()) != null
    }

    override suspend fun addToFavorites(movie: MovieEntity) {
        movieDao.insert(movie.copy(userId = currentUserId()))
    }

    override suspend fun removeFromFavorites(movie: MovieEntity) {
        movieDao.delete(movie.copy(userId = currentUserId()))
    }
}
