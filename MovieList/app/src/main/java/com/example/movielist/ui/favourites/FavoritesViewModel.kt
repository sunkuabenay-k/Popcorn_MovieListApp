package com.example.movielist.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val movieRepository: MovieRepositoryImpl
) : ViewModel() {

    // ✅ MUST be Flow<List<MovieEntity>>
    val favoriteMovies: Flow<List<MovieEntity>> =
        movieRepository.getFavoriteMovies()

    fun removeFromFavorites(movie: MovieEntity) {
        viewModelScope.launch {
            movieRepository.removeFromFavorites(movie)
        }
    }
}
