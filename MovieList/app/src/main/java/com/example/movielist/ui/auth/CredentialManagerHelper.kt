package com.example.movielist.ui.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialException

class CredentialManagerHelper(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun saveLoginCredentials(email: String, password: String) {
        // 1. Create the request
        val request = CreatePasswordRequest(
            id = email, // The unique ID for the user (usually email)
            password = password
        )

        try {
            // 2. Trigger the system prompt
            // This will show the "Save Password?" bottom sheet to the user
            credentialManager.createCredential(
                context = context,
                request = request
            )
            Log.d("CredManager", "Credentials saved successfully")
        } catch (e: CreateCredentialException) {
            // Handle specific failures (e.g., user cancelled, or already saved)
            Log.e("CredManager", "Failed to save credentials", e)
        } catch (e: Exception) {
            Log.e("CredManager", "Unexpected error", e)
        }
    }
}