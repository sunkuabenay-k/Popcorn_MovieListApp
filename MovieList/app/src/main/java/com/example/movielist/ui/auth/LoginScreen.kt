package com.example.movielist.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movielist.R
import com.example.movielist.ui.components.AnimatedEyes

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val loginState by viewModel.loginState.collectAsState()
    var showPassword by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordFocusRequester = remember { FocusRequester() }

    LaunchedEffect(loginState.isSuccess) {
        if (loginState.isSuccess) {
            viewModel.resetLoginState()
            // Navigate to Home here
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AuthUiUtils.horizontalPadding()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(AuthUiUtils.responsiveDp(0.08f)))

        // Logo Header
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.movie_list_icon3),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxSize(0.7f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedEyes(
            modifier = Modifier.size(120.dp),
            pointerOffset = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your Cinema\nJourney Starts Here",
            fontSize = AuthUiUtils.responsiveSp(0.032f),
            fontWeight = FontWeight.ExtraBold,
            lineHeight = AuthUiUtils.responsiveSp(0.04f),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Discover, track, and share your favorite films.",
            fontSize = AuthUiUtils.responsiveSp(0.018f),
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        // Input Fields
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = loginState.email,
                onValueChange = viewModel::onLoginEmailChange,
                label = { Text("Email or Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() })
            )

            OutlinedTextField(
                value = loginState.password,
                onValueChange = viewModel::onLoginPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth().focusRequester(passwordFocusRequester),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }

        if (loginState.error != null) {
            Text(
                text = loginState.error!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = AuthUiUtils.responsiveSp(0.015f),
                modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                keyboardController?.hide()
                viewModel.login(loginState.email, loginState.password)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !loginState.isLoading && loginState.email.isNotBlank() && loginState.password.isNotBlank()
        ) {
            if (loginState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Login", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        TextButton(
            onClick = {
                viewModel.resetLoginState()
                navController.navigate("register")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Don't have an account? Sign Up", fontWeight = FontWeight.Medium)
        }
    }
}