package com.copay.app.ui.screen.auth

import AuthViewModelFactory
import UserService
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.copay.app.repository.UserRepository
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.ui.components.InputField
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.utils.state.AuthState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    userRepository: UserRepository,
    userService: UserService,
    onLoginSuccess: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    val viewModelFactory = remember { AuthViewModelFactory(userRepository, userService) }
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Back button in the top-left corner
        Box(modifier = Modifier.padding(top = 16.dp)) {
            BackButtonTop(navController)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(128.dp))

            Text(text = "Log in", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(32.dp))

            var phone by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var phoneError by remember { mutableStateOf<String?>(null) }
            var passwordError by remember { mutableStateOf<String?>(null) }

            fun validateInputs() {
                phoneError = UserValidation.validateLoginPhoneNumber(phone).errorMessage
                passwordError = UserValidation.validateLoginPassword(password).errorMessage
            }

            InputField(
                value = phone,
                onValueChange = {
                    phone = it
                    phoneError = UserValidation.validateLoginPhoneNumber(it).errorMessage
                },
                label = "Phone Number",
                isError = phoneError != null,
                errorMessage = phoneError
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = UserValidation.validateLoginPassword(it).errorMessage
                },
                label = "Password",
                isPassword = true,
                isError = passwordError != null,
                errorMessage = passwordError
            )
            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current

            PrimaryButton(
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
                Text(text = "Forgot your password?")
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
