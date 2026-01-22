package com.example.movielist.data.repository

import com.example.movielist.data.local.UserEntity

interface UserRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun getUser(): UserEntity?
    suspend fun logout()
}

