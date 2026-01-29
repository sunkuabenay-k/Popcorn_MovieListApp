package com.example.movielist.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.BuildConfig
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.data.remote.GenreMapper
import com.example.movielist.data.remote.MovieDto
import com.example.movielist.data.remote.RetrofitInstance
import com.example.movielist.repository.MovieRepositoryImpl
import com.example.movielist.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepositoryImpl,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                val userId = movieRepository.currentUserId()
                val interests = userRepository.getUserInterests(userId)

                val genreIds =
                    interests
                        .map { it.trim().lowercase() }
                        .mapNotNull { GenreMapper.map[it] }

                val trending = RetrofitInstance.api
                    .getTrendingMovies(BuildConfig.TMDB_API_KEY)
                    .results

                val topRated = RetrofitInstance.api
                    .getTopRatedMovies(BuildConfig.TMDB_API_KEY)
                    .results



                val recommended =
                    if (genreIds.isNotEmpty()) {
                        RetrofitInstance.api
                            .discoverMovies(
                                apiKey = BuildConfig.TMDB_API_KEY,
                                withGenres = genreIds.joinToString(",")
                            )
                            .results
                            .shuffled()
                            .take(10)


                    } else emptyList()
                Log.d("HOME_DEBUG", "UserId = $userId")
                Log.d("HOME_DEBUG", "Interests = $interests")
                Log.d("HOME_DEBUG", "GenreIds = $genreIds")
                Log.d("HOME_DEBUG", "Recommended size = ${recommended.size}")
                Log.d("HOME_DEBUG", "Mapped genreIds = $genreIds")


                _uiState.value = HomeUiState(
                    trendingMovies = trending,
                    topRatedMovies = topRated,
                    recommendedMovies = recommended,
                    filteredTrending = trending,
                    filteredTopRated = topRated,
                    userId = userId,
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onRatingFilterChange(rating: Double) {
        _uiState.update { it.copy(minRatingFilter = rating) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val query = state.searchQuery.lowercase()
        val minRating = state.minRatingFilter

        fun filter(list: List<MovieDto>) =
            list.filter {
                it.title.lowercase().contains(query) &&
                        it.vote_average >= minRating
            }

        _uiState.update {
            it.copy(
                filteredTrending = filter(it.trendingMovies),
                filteredTopRated = filter(it.topRatedMovies)
            )
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> =
        movieRepository.isFavorite(movieId).distinctUntilChanged()

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

    private fun MovieDto.toEntity(userId: String): MovieEntity =
        MovieEntity(
            id = id,
            userId = userId,
            title = title,
            posterPath = poster_path,
            rating = vote_average
        )
}
