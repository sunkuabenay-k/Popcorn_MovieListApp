package com.yourapp.movielist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.movielist.data.local.MovieEntity
import com.yourapp.movielist.data.repository.MovieRepository
import com.yourapp.movielist.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableStateFlow<UiState<List<MovieEntity>>>(UiState.Loading)
    val movies: StateFlow<UiState<List<MovieEntity>>> = _movies

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _movies.value = UiState.Loading
            try {
                val list = repository.getPopularMovies()
                _movies.value = UiState.Success(list)
            } catch (e: Exception) {
                _movies.value = UiState.Error("Failed to load movies")
            }
        }
    }
}