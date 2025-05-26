package com.copay.app.ui.screen.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.components.button.primaryButton
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.AuthState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.AuthViewModel


@Composable
fun loginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    // Use hiltViewModel to obtain the injected AuthViewModel with userRepository and userService.
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    // Display errors on UI.
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }

    // Tracks the state
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // Redirection to HubScreen if the authentication is successful.
                onLoginSuccess()
            }
            is AuthState.Error -> {
                apiErrorMessage = (authState as AuthState.Error).message
            }
            else -> {}
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CopayColors.background)
            .verticalScroll(scrollState)
            .imePadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Back button in the top-left corner
        Box(modifier = Modifier.padding(top = 16.dp)) {
            backButtonTop(navController)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(100.dp))

            Text(text = "Log in", style = CopayTypography.title, color = CopayColors.primary)

            Spacer(modifier = Modifier.height(32.dp))

            var phone by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var phoneError by remember { mutableStateOf<String?>(null) }
            var passwordError by remember { mutableStateOf<String?>(null) }

            fun validateInputs() {
                phoneError = UserValidation.validateNotEmpty(phone, "Phone number").errorMessage
                passwordError = UserValidation.validateNotEmpty(password, "Password").errorMessage
            }

            inputField(
                value = phone,
                onValueChange = {
                    phone = it
                    phoneError = UserValidation.validateNotEmpty(it, "Phone number").errorMessage
                },
                label = "Phone Number",
                isError = phoneError != null,
                errorMessage = phoneError,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                )
            Spacer(modifier = Modifier.height(12.dp))

            inputField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = UserValidation.validateNotEmpty(it, "Password").errorMessage
                },
                label = "Password",
                isPassword = true,
                isError = passwordError != null,
                errorMessage = passwordError
            )
            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current

            primaryButton(
                text = "Log in",
                onClick = {
                    validateInputs()

                    if (listOf(phoneError, passwordError).all { it == null }) {
                        Log.d("LoginScreen", "Login button clicked. Sending data -> Phone: $phone, Password: $password")
                        authViewModel.login(context, phone, password)
                    } else {
                        Log.e("LoginScreen", "Validation errors: PhoneError=$phoneError, PasswordError=$passwordError")
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(
                onClick = {
                    onForgotPasswordClick()
                    Log.d("LoginScreen", "Forgot Password clicked")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Forgot your password?", style = CopayTypography.body, color = CopayColors.primary)
            }

            // Show error message if API fails.
            apiErrorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
