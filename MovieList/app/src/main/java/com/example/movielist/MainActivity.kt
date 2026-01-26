package com.example.movielist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.movielist.data.local.AppDatabase
import com.example.movielist.data.remote.MovieApi
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.UserRepository
import com.example.movielist.navigation.NavGraph
import com.example.movielist.ui.theme.MovieListTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    // Simple Manual Dependency Injection Container
    class AppContainer(context: android.content.Context) {
        private val database = Room.databaseBuilder(context, AppDatabase::class.java,
            "movie_db").build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val movieApi = retrofit.create(MovieApi::class.java)

        val userRepository = UserRepository(database.userDao())
        val movieRepository = MovieRepository(movieApi, database.movieDao(),
            database.favoriteDao())
    }

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainer(applicationContext)

        setContent {
            MovieListTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, appContainer = appContainer)
            }
        }
    }
}