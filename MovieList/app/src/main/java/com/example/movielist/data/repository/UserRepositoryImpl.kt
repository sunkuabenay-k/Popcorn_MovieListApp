package com.example.movielist.repository

import com.example.movielist.data.local.UserDao
import com.example.movielist.data.local.UserEntity


class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val user = userDao.getUserByCredentials(email, password)
            if (user != null) {

                userDao.logoutAllUsers()

                val updatedUser = user.copy(
                    isLoggedIn = true,
                    lastLogin = System.currentTimeMillis()
                )
                userDao.updateUser(updatedUser)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            if (isEmailTaken(email)) {
                return Result.failure(Exception("Email already registered"))
            }

            val user = UserEntity(
                id = email,
                email = email,
                name = name,
                password = password
            )
            userDao.insertUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(): UserEntity? =
        userDao.getLoggedInUser()

    override suspend fun getCurrentUser(): UserEntity? =
        userDao.getLoggedInUser()

    override suspend fun logout() {
        userDao.logoutAllUsers()
    }

    override suspend fun isEmailTaken(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }
}