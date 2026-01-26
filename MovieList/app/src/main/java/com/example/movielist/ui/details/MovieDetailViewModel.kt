package com.example.movielist.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.UserRepository
import com.example.movielist.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepo: MovieRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<UiState<MovieEntity>>(UiState.Loading)
    val movie: StateFlow<UiState<MovieEntity>> = _movie

    private val _suggestions = MutableStateFlow<List<MovieEntity>>(emptyList())
    val suggestions: StateFlow<List<MovieEntity>> = _suggestions

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _movie.value = UiState.Loading
            val movie = movieRepo.getMovieDetail(movieId)
            if (movie != null) {
                _movie.value = UiState.Success(movie)
                // Load suggestions
                _suggestions.value = movieRepo.getSuggestions(movieId)
                // Check favorite status
                userRepo.currentUser?.let { user ->
                    val favs = movieRepo.getFavorites(user.id).first()
                    _isFavorite.value = favs.any { it.id == movieId }
                }
            } else {
                _movie.value = UiState.Error("Movie not found")
            }
        }
    }

    fun toggleFavorite() {
        val currentMovie = (_movie.value as? UiState.Success)?.data ?: return
        val user = userRepo.currentUser ?: return

        viewModelScope.launch {
            val newStatus = !_isFavorite.value
            movieRepo.toggleFavorite(user.id, currentMovie, newStatus)
            _isFavorite.value = newStatus
        }
    }
}