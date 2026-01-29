package com.example.movielist.repository

import com.example.movielist.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    suspend fun getUser(): UserEntity?
    suspend fun getCurrentUser(): UserEntity?
    suspend fun logout()
    suspend fun isEmailTaken(email: String): Boolean
    fun currentUserFlow(): Flow<UserEntity>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun updateUserInterests(userId: String, interests: List<String>)
    suspend fun getUserInterests(userId: String): List<String>

}