package com.example.movielist.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val movieRepo: MovieRepository,
    private val userRepo: UserRepository
) : ViewModel() {
    private val _favorites = MutableStateFlow<List<MovieEntity>>(emptyList())
    val favorites: StateFlow<List<MovieEntity>> = _favorites

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        userRepo.currentUser?.let { user ->
            viewModelScope.launch {
                movieRepo.getFavorites(user.id).collect {
                    _favorites.value = it
                }
            }
        }
    }

    fun removeFavorite(movie: MovieEntity) {
        userRepo.currentUser?.let { user ->
            viewModelScope.launch {
                movieRepo.toggleFavorite(user.id, movie, false)
            }
        }
    }
}