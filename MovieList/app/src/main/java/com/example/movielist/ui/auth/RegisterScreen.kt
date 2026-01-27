package com.example.movielist.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val registerState by viewModel.registerState.collectAsState()
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Handle registration success
    LaunchedEffect(registerState.isSuccess) {
        if (registerState.isSuccess) {
            showSuccessDialog = true
        }
    }

    // Show success dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                // Navigate to login after dismissing dialog
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
                viewModel.resetRegisterState()
            },
            title = {
                Text("Registration Successful!")
            },
            text = {
                Text("Your account has been created successfully. Please login to continue.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                        viewModel.resetRegisterState()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(screenHeight * 0.05f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = calculateHorizontalPadding(screenWidth))
                .weight(1f, fill = false),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                fontSize = calculateFontSize(screenHeight, 0.03f),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = calculateSpacing(screenHeight, 0.04f))
            )

            OutlinedTextField(
                value = registerState.name,
                onValueChange = viewModel::onRegisterNameChange,
                placeholder = {
                    Text(
                        "Full Name",
                        fontSize = calculateFontSize(screenHeight, 0.016f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 60.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                isError = registerState.error != null
            )

            Spacer(modifier = Modifier.height(calculateSpacing(screenHeight, 0.02f)))

            OutlinedTextField(
                value = registerState.email,
                onValueChange = viewModel::onRegisterEmailChange,
                placeholder = {
                    Text(
                        "Email",
                        fontSize = calculateFontSize(screenHeight, 0.016f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 60.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = registerState.error != null
            )

            Spacer(modifier = Modifier.height(calculateSpacing(screenHeight, 0.02f)))

            OutlinedTextField(
                value = registerState.password,
                onValueChange = viewModel::onRegisterPasswordChange,
                placeholder = {
                    Text(
                        "Password",
                        fontSize = calculateFontSize(screenHeight, 0.016f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 60.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                isError = registerState.error != null,
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = { showPassword = !showPassword }
                    ) {
                        Icon(
                            imageVector = if (showPassword) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = if (showPassword) {
                                "Hide password"
                            } else {
                                "Show password"
                            },
                            tint = Color.Gray
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(calculateSpacing(screenHeight, 0.02f)))

            OutlinedTextField(
                value = registerState.confirmPassword,
                onValueChange = viewModel::onRegisterConfirmPasswordChange,
                placeholder = {
                    Text(
                        "Confirm Password",
                        fontSize = calculateFontSize(screenHeight, 0.016f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 60.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = registerState.error != null,
                visualTransformation = if (showConfirmPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = { showConfirmPassword = !showConfirmPassword }
                    ) {
                        Icon(
                            imageVector = if (showConfirmPassword) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = if (showConfirmPassword) {
                                "Hide password"
                            } else {
                                "Show password"
                            },
                            tint = Color.Gray
                        )
                    }
                }
            )

            if (registerState.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = registerState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = calculateFontSize(screenHeight, 0.015f),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(calculateSpacing(screenHeight, 0.02f)))
            PasswordRequirementsHint(screenHeight)

            Spacer(modifier = Modifier.height(calculateSpacing(screenHeight, 0.04f)))

            Button(
                onClick = {
                    keyboardController?.hide()
                    if (registerState.password != registerState.confirmPassword) {
                        return@Button
                    }
                    viewModel.register(
                        registerState.name,
                        registerState.email,
                        registerState.password,
                        registerState.confirmPassword
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 60.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = !registerState.isLoading &&
                        registerState.name.isNotBlank() &&
                        registerState.email.isNotBlank() &&
                        registerState.password.isNotBlank() &&
                        registerState.confirmPassword.isNotBlank()
            ) {
                if (registerState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = calculateFontSize(screenHeight, 0.018f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(calculateSpacing(screenHeight, 0.02f)))

            OutlinedButton(
                onClick = {
                    keyboardController?.hide()
                    viewModel.resetRegisterState()
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 60.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Already have an account? Login",
                    fontSize = calculateFontSize(screenHeight, 0.017f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.05f))
    }
}

@Composable
private fun PasswordRequirementsHint(screenHeight: Dp) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Password should contain:",
            fontSize = calculateFontSize(screenHeight, 0.014f),
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "• At least 8 characters",
            fontSize = calculateFontSize(screenHeight, 0.013f),
            color = Color.Gray
        )

        Text(
            text = "• Mix of letters and numbers",
            fontSize = calculateFontSize(screenHeight, 0.013f),
            color = Color.Gray
        )
    }
}

@Composable
private fun calculateHorizontalPadding(screenWidth: Dp): Dp {
    return when {
        screenWidth < 360.dp -> 16.dp
        screenWidth > 600.dp -> 48.dp
        else -> 24.dp
    }
}

@Composable
private fun calculateSpacing(screenHeight: Dp, fraction: Float): Dp {
    val baseSpacing = screenHeight * fraction
    return when {
        screenHeight < 600.dp -> baseSpacing * 0.8f
        screenHeight > 1000.dp -> baseSpacing * 1.2f
        else -> baseSpacing
    }
}

@Composable
private fun calculateFontSize(screenHeight: Dp, fraction: Float): androidx.compose.ui.unit.TextUnit {
    val baseSize = (screenHeight.value * fraction).sp
    return when {
        screenHeight < 600.dp -> baseSize * 0.9f
        screenHeight > 1000.dp -> baseSize * 1.1f
        else -> baseSize
    }
}