// app/kotlin+java/com/example/movielist/data/local/UserEntity.kt
package com.example.movielist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val password: String, // Add password field
    val isLoggedIn: Boolean = false, // Add login state
    val createdAt: Long = System.currentTimeMillis(),
    val lastLogin: Long? = null
)