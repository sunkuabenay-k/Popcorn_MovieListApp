package com.example.movielist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.movielist.data.local.AppDatabase
import com.example.movielist.navigation.AppNavGraph
import com.example.movielist.ui.theme.MovieListTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create DB ONCE
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "movie_db"
        ).build()

        setContent {
            val navController = rememberNavController()

            // TEMP: replace with real auth check later
            val isLoggedIn by remember {
                mutableStateOf(false)
            }

            MovieListTheme {
                AppNavGraph(
                    navController = navController,
                    isLoggedIn = isLoggedIn,
                    database = database
                )
            }
        }
    }
}

