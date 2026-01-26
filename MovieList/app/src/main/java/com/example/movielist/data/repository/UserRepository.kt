package com.example.movielist.data.repository

import com.example.movielist.data.local.UserDao
import com.example.movielist.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    // Simple session management in memory
    var currentUser: UserEntity? = null
        private set

    suspend fun login(username: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userDao.getUser(username)
            if (user != null) {
                currentUser = user
                true
            } else {
                false
            }
        }
    }

    suspend fun signup(username: String): Boolean {
        return withContext(Dispatchers.IO) {
            val existing = userDao.getUser(username)
            if (existing == null) {
                val newUser = UserEntity(username = username, interests = "")
                userDao.insertUser(newUser)
                login(username) // Auto login
                true
            } else {
                false
            }
        }
    }

    suspend fun updateInterests(interests: String) {
        currentUser?.let {
            val updated = it.copy(interests = interests)
            withContext(Dispatchers.IO) {
                userDao.updateUser(updated)
            }
            currentUser = updated
        }
    }

    fun logout() {
        currentUser = null
    }
}