package com.example.movielist.ui.home


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movielist.navigation.BottomNavItem
import com.example.movielist.ui.components.BottomNavigationBar

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = items
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Home",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
