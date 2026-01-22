package com.example.movielist.ui.profile

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProfileScreen(
    onLogout: () -> Unit
) {
    Button(onClick = onLogout) {
        Text("Logout")
    }
}

