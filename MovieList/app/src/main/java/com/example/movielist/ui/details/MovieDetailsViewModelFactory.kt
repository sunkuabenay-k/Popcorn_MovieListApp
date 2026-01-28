package com.example.movielist.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movielist.repository.MovieRepositoryImpl

class MovieDetailsViewModelFactory(
    private val movieId: Int,
    private val movieRepository: MovieRepositoryImpl
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(movieId, movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
