package com.example.movielist.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movielist.R
import com.example.movielist.ui.components.AnimatedEyes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val loginState by viewModel.loginState.collectAsState()

    // This tracks where the user is touching or moving their finger
    var pointerOffset by remember { mutableStateOf<Offset?>(null) }

    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(loginState.isSuccess) {
        if (loginState.isSuccess) {
            viewModel.resetLoginState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background to make white eyes pop
            .pointerInput(Unit) {
                // Detects movements across the entire screen
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val position = event.changes.first().position

                        // Update offset if pointer is pressed, otherwise reset to center
                        pointerOffset = if (event.changes.any { it.pressed }) {
                            // We center the coordinate system relative to the screen
                            // to make the eyes look "towards" the touch
                            position - Offset(size.width / 2f, size.height / 2f)
                        } else {
                            null
                        }
                    }
                }
            }
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // App Logo
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(shape = CircleShape, color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.movie_list_icon3),
                contentDescription = "App Logo",
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // THE FIXED COMPONENT: Passing the pointerOffset
        AnimatedEyes(
            modifier = Modifier.padding(vertical = 10.dp),
            pointerOffset = pointerOffset
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your Cinema\nJourney Starts Here",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Discover, track, and share your favorite\nfilms with CineList.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Email Field
        OutlinedTextField(
            value = loginState.email,
            onValueChange = viewModel::onLoginEmailChange,
            label = { Text("Email or Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = loginState.error != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = loginState.password,
            onValueChange = viewModel::onLoginPasswordChange,
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = loginState.error != null
        )

        if (loginState.error != null) {
            Text(
                text = loginState.error!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Login Button
        Button(
            onClick = {
                keyboardController?.hide()
                viewModel.login(loginState.email, loginState.password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !loginState.isLoading && loginState.email.isNotBlank() && loginState.password.isNotBlank()
        ) {
            if (loginState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                viewModel.resetLoginState()
                navController.navigate("register")
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Create New Account", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}