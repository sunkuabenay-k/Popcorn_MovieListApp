package com.example.movielist.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.local.UserEntity
import com.example.movielist.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
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

    init {
        refreshAuth()
    }

    private fun refreshAuth() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            _currentUser.value = user
            _authState.value = if (user != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(
                isLoading = true,
                error = null
            )
            _uiState.value = AuthUiState.Loading

            val result = userRepository.login(email, password)

            if (result.isSuccess) {
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
                _uiState.value = AuthUiState.Success
                refreshAuth()
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
                _uiState.value = AuthUiState.Error(errorMessage)
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _registerState.value = _registerState.value.copy(
                error = "Passwords don't match"
            )
            return
        }

        viewModelScope.launch {
            _registerState.value = _registerState.value.copy(
                isLoading = true,
                error = null
            )
            _uiState.value = AuthUiState.Loading

            val result = userRepository.register(name, email, password)

            if (result.isSuccess) {
                _registerState.value = _registerState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
                _uiState.value = AuthUiState.Success
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Registration failed"
                _registerState.value = _registerState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
                _uiState.value = AuthUiState.Error(errorMessage)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _currentUser.value = null
            _authState.value = AuthState.Unauthenticated
            resetLoginState()
            resetRegisterState()
        }
    }

    fun onLoginEmailChange(email: String) {
        _loginState.value = _loginState.value.copy(email = email)
    }

    fun onLoginPasswordChange(password: String) {
        _loginState.value = _loginState.value.copy(password = password)
    }

    fun onRegisterNameChange(name: String) {
        _registerState.value = _registerState.value.copy(name = name)
    }

    fun onRegisterEmailChange(email: String) {
        _registerState.value = _registerState.value.copy(email = email)
    }

    fun onRegisterPasswordChange(password: String) {
        _registerState.value = _registerState.value.copy(password = password)
    }

    fun onRegisterConfirmPasswordChange(confirmPassword: String) {
        _registerState.value = _registerState.value.copy(confirmPassword = confirmPassword)
    }

    fun resetLoginState() {
        _loginState.value = LoginState()
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState()
    }
}