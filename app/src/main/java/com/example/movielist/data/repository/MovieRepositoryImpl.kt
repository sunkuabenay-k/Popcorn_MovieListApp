package com.example.movielist.repository

import com.example.movielist.data.local.MovieDao
import com.example.movielist.data.local.MovieEntity

class MovieRepositoryImpl(
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun addToFavorites(movie: MovieEntity) {
        movieDao.addFavorite(movie)
    }

    override suspend fun removeFromFavorites(movie: MovieEntity) {
        movieDao.removeFavorite(movie)
    }

    override fun getFavoriteMovies() =
        movieDao.getFavorites()

    override fun isFavorite(movieId: Int) =
        movieDao.isFavorite(movieId)
}
