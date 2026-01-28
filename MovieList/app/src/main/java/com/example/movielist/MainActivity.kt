package com.example.movielist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.movielist.data.local.AppDatabase
import com.example.movielist.navigation.AppNavGraph
import com.example.movielist.repository.MovieRepositoryImpl
import com.example.movielist.repository.UserRepositoryImpl
import com.example.movielist.ui.theme.MovieListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val userRepository = UserRepositoryImpl(database.userDao())
        val movieRepository = MovieRepositoryImpl(
            database.movieDao(),
            userRepository = userRepository
        )

        setContent {
            MovieListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        userRepository = userRepository,
                        movieRepository = movieRepository
                    )
                }
            }
        }
    }
}