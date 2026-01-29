package com.example.movielist.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.local.UserEntity
import com.example.movielist.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// --- Login State ---
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

// --- Register State with validation flags ---
data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val passwordError: Boolean = false,
    val confirmPasswordError: Boolean = false
)

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _userInterests = MutableStateFlow<List<String>>(emptyList())
    val userInterests: StateFlow<List<String>> = _userInterests


    init {
        refreshAuth()
        viewModelScope.launch {
            currentUser.collect { user ->
                if (user != null) {
                    _userInterests.value =
                        user.interests
                            ?.split(",")
                            ?.map { it.trim() }
                            ?.filter { it.isNotEmpty() }
                            ?: emptyList()
                } else {
                    _userInterests.value = emptyList()
                }
            }
        }
    }


    // --- Save credentials locally ---
    fun saveCredentials(context: Context, email: String, pass: String) {
        viewModelScope.launch {
            val helper = CredentialManagerHelper(context)
            helper.saveLoginCredentials(email, pass)
        }
    }

    // --- Refresh current user ---
    private fun refreshAuth() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            _currentUser.value = user
            _authState.value = if (user != null) AuthState.Authenticated
            else AuthState.Unauthenticated
        }
    }

    // --- LOGIN ---
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, error = null)
            _uiState.value = AuthUiState.Loading

            val result = userRepository.login(email, password)
            if (result.isSuccess) {
                _loginState.value = _loginState.value.copy(isLoading = false, isSuccess = true)
                _uiState.value = AuthUiState.Success
                refreshAuth()
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Login failed"
                _loginState.value = _loginState.value.copy(isLoading = false, error = errorMsg)
                _uiState.value = AuthUiState.Error(errorMsg)
            }
        }
    }

    // --- REGISTER with password validation ---
    fun register(name: String, email: String, pass: String, confirm: String) {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}$".toRegex()
        val isPasswordValid = passwordPattern.matches(pass)
        val passwordsMatch = pass == confirm

        if (!isPasswordValid || !passwordsMatch) {
            _registerState.value = _registerState.value.copy(
                passwordError = !isPasswordValid,
                confirmPasswordError = !passwordsMatch,
                error = when {
                    !isPasswordValid -> "Password must be 8+ chars, with 1 uppercase, 1 number, and 1 special character."
                    !passwordsMatch -> "Passwords do not match."
                    else -> null
                }
            )
            return
        }

        _registerState.value = _registerState.value.copy(
            passwordError = false,
            confirmPasswordError = false,
            error = null,
            isLoading = true
        )
        _uiState.value = AuthUiState.Loading

        viewModelScope.launch {
            val result = userRepository.register(name, email, pass)
            if (result.isSuccess) {
                _registerState.value = _registerState.value.copy(isLoading = false, isSuccess = true)
                _uiState.value = AuthUiState.Success
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Registration failed"
                _registerState.value = _registerState.value.copy(isLoading = false, error = errorMsg)
                _uiState.value = AuthUiState.Error(errorMsg)
            }
        }
    }

    // --- LOGOUT ---
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _currentUser.value = null
            _authState.value = AuthState.Unauthenticated
            resetLoginState()
            resetRegisterState()
        }
    }

    // --- DELETE ACCOUNT ---
    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = userRepository.deleteAccount()
            if (result.isSuccess) {
                _currentUser.value = null
                _authState.value = AuthState.Unauthenticated
                _uiState.value = AuthUiState.Success
                resetLoginState()
                resetRegisterState()
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Delete failed"
                _uiState.value = AuthUiState.Error(errorMsg)
            }
        }
    }

    // --- LOGIN STATE UPDATES ---
    fun onLoginEmailChange(email: String) { _loginState.value = _loginState.value.copy(email = email) }
    fun onLoginPasswordChange(password: String) { _loginState.value = _loginState.value.copy(password = password) }

    // --- REGISTER STATE UPDATES ---
    fun onRegisterNameChange(name: String) { _registerState.value = _registerState.value.copy(name = name) }
    fun onRegisterEmailChange(email: String) { _registerState.value = _registerState.value.copy(email = email) }
    fun onRegisterPasswordChange(password: String) { _registerState.value = _registerState.value.copy(password = password) }
    fun onRegisterConfirmPasswordChange(confirmPassword: String) { _registerState.value = _registerState.value.copy(confirmPassword = confirmPassword) }

    // --- RESET STATES ---
    fun resetLoginState() { _loginState.value = LoginState() }
    fun resetRegisterState() { _registerState.value = RegisterState() }
    // ADD THIS METHOD AT THE BOTTOM OF AuthViewModel

    fun updateInterests(interests: List<String>) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            userRepository.updateUserInterests(
                userId = user.id,
                interests = interests
            )
            // refresh user so UI updates
            _currentUser.value = userRepository.getUser()
        }
    }


}
