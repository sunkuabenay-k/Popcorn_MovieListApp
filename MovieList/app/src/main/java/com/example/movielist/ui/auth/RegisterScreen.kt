// app/kotlin+java/com/example/movielist/ui/auth/RegisterScreen.kt
package com.example.movielist.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState.isSuccess) {
        if (registerState.isSuccess) {
            // After successful registration, navigate to login
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
            viewModel.resetRegisterState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = registerState.name,
            onValueChange = viewModel::onRegisterNameChange,
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = registerState.error != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registerState.email,
            onValueChange = viewModel::onRegisterEmailChange,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = registerState.error != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registerState.password,
            onValueChange = viewModel::onRegisterPasswordChange,
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = registerState.error != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registerState.confirmPassword,
            onValueChange = viewModel::onRegisterConfirmPasswordChange,
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = registerState.error != null
        )

        if (registerState.error != null) {
            Text(
                text = registerState.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.register(
                    registerState.name,
                    registerState.email,
                    registerState.password,
                    registerState.confirmPassword
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !registerState.isLoading
        ) {
            if (registerState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Register")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                viewModel.resetRegisterState()
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }
        ) {
            Text("Already have an account? Login")
        }
    }
}