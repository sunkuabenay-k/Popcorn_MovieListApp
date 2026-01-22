package com.example.movielist.data.repository

import com.example.movielist.data.local.UserDao
import com.example.movielist.data.local.UserEntity

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.length < 6) {
            return Result.failure(Exception("Invalid credentials"))
        }

        val user = UserEntity(
            id = email,
            email = email,
            name = email.substringBefore("@")
        )

        userDao.clear()
        userDao.insertUser(user)

        return Result.success(Unit)
    }

    override suspend fun getUser(): UserEntity? =
        userDao.getUser()

    override suspend fun logout() {
        userDao.clear()
    }
}
