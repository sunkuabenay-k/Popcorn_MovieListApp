package com.example.movielist.ui.profile
import androidx.lifecycle.ViewModel
import com.example.movielist.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(
    private val authViewModel: AuthViewModel
) : ViewModel() {

    val currentUser: StateFlow<Any?> = authViewModel.currentUser

    fun logout() {
        authViewModel.logout()
    }
}
