package com.example.movielist.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.BuildConfig
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.data.remote.MovieDetailsDto
import com.example.movielist.data.remote.RetrofitInstance
import com.example.movielist.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieId: Int,
    private val movieRepository: MovieRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    private var userId: String? = null

    init {
        loadMovie()
    }

    private fun loadMovie() {
        viewModelScope.launch {
            try {
                userId = movieRepository.currentUserId()

                val movie = RetrofitInstance.api.getMovieDetails(
                    movieId = movieId,
                    apiKey = BuildConfig.TMDB_API_KEY
                )

                val favorite = movieRepository.isFavoriteOnce(movieId)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movie = movie,
                    isFavorite = favorite
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun toggleFavorite() {
        val movie = _uiState.value.movie ?: return
        val uid = userId ?: return

        viewModelScope.launch {
            val entity = movie.toEntity(uid)

            if (_uiState.value.isFavorite) {
                movieRepository.removeFromFavorites(entity)
            } else {
                movieRepository.addToFavorites(entity)
            }

            _uiState.value = _uiState.value.copy(
                isFavorite = !_uiState.value.isFavorite
            )
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }
}

/** Mapper */
private fun MovieDetailsDto.toEntity(userId: String): MovieEntity {
    return MovieEntity(
        id = id,
        userId = userId,
        title = title,
        posterPath = poster_path,
        rating = vote_average
    )
}
