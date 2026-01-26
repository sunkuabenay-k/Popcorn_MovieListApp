package com.example.movielist.data.remote
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// DTOs
data class MovieResponse(val results: List<NetworkMovie>)
data class NetworkMovie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String,
    @SerializedName("vote_average") val voteAverage: Double
)

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): NetworkMovie

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse
}