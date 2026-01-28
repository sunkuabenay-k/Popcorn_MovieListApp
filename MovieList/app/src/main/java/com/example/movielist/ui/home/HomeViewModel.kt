package com.example.movielist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.BuildConfig
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.data.remote.MovieDto
import com.example.movielist.data.remote.RetrofitInstance
import com.example.movielist.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                val userId = movieRepository.currentUserId()

                val trending = RetrofitInstance.api
                    .getTrendingMovies(BuildConfig.TMDB_API_KEY)
                    .results

                val topRated = RetrofitInstance.api
                    .getTopRatedMovies(BuildConfig.TMDB_API_KEY)
                    .results

                _uiState.value = HomeUiState(
                    trendingMovies = trending,
                    topRatedMovies = topRated,
                    userId = userId,
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> {
        return movieRepository.isFavorite(movieId)
    }

    fun toggleFavorite(movie: MovieDto) {
        val uid = _uiState.value.userId ?: return

        viewModelScope.launch {
            val entity = movie.toEntity(uid)
            if (movieRepository.isFavoriteOnce(movie.id)) {
                movieRepository.removeFromFavorites(entity)
            } else {
                movieRepository.addToFavorites(entity)
            }
        }
    }

    private fun MovieDto.toEntity(userId: String): MovieEntity {
        return MovieEntity(
            id = id,
            userId = userId,
            title = title,
            posterPath = poster_path,
            rating = vote_average
        )
    }
}
