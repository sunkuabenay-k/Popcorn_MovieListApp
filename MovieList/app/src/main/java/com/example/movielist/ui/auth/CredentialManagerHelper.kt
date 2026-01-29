package com.example.movielist.ui.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialException

class CredentialManagerHelper(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun saveLoginCredentials(email: String, password: String) {
        val request = CreatePasswordRequest(
            id = email,
            password = password
        )

        try {
            credentialManager.createCredential(
                context = context,
                request = request
            )
            Log.d("CredManager", "Credentials saved successfully")
        } catch (e: CreateCredentialException) {
            Log.e("CredManager", "Failed to save credentials", e)
        } catch (e: Exception) {
            Log.e("CredManager", "Unexpected error", e)
        }
    }
}
