package com.copay.app.ui.screen.auth

import AuthViewModelFactory
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
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.AuthState
import com.copay.app.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    userRepository: UserRepository,
    onRegisterSuccess: () -> Unit = {}
) {
    val viewModelFactory = remember { AuthViewModelFactory(userRepository) }
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordMatchError by remember { mutableStateOf<String?>(null) }

    var apiErrorMessage by remember { mutableStateOf<String?>(null) }

    // Effect triggered when the authentication state changes.
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // Redirection to HubScreen if the authentication is successful.
                onRegisterSuccess()
            }
            is AuthState.Error -> {
                apiErrorMessage = (authState as AuthState.Error).message
            }
            else -> {
                apiErrorMessage = null
            }
        }
    }

    // Function to validate all inputs.
    fun validateInputs() {
        usernameError = UserValidation.validateRegisterUsername(username).errorMessage
        emailError = UserValidation.validateEmail(email).errorMessage
        phoneNumberError = UserValidation.validateRegisterPhoneNumber(phoneNumber).errorMessage
        passwordError = UserValidation.validateRegisterPassword(password).errorMessage
        passwordMatchError = UserValidation.validatePasswordMatch(password, confirmPassword).errorMessage
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
            Spacer(modifier = Modifier.height(64.dp))

            Text("Welcome to Copay!", style = MaterialTheme.typography.titleLarge)
            Text("Let's set up your account.", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(32.dp))

            InputField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = UserValidation.validateRegisterUsername(it).errorMessage
                },
                label = "Username",
                isRequired = true,
                isError = usernameError != null,
                errorMessage = usernameError
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = UserValidation.validateEmail(it).errorMessage
                },
                label = "Email Address",
                isRequired = true,
                isError = emailError != null,
                errorMessage = emailError
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    phoneNumberError = UserValidation.validateRegisterPhoneNumber(it).errorMessage
                },
                label = "Phone Number",
                isRequired = true,
                isError = phoneNumberError != null,
                errorMessage = phoneNumberError
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = UserValidation.validateRegisterPassword(it).errorMessage
                },
                label = "Password",
                isRequired = true,
                isPassword = true,
                isError = passwordError != null,
                errorMessage = passwordError
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordMatchError = UserValidation.validatePasswordMatch(password, it).errorMessage
                },
                label = "Confirm Password",
                isRequired = true,
                isPassword = true,
                isError = passwordMatchError != null,
                errorMessage = passwordMatchError
            )
            Spacer(modifier = Modifier.height(24.dp))

            val isLoading = authState is AuthState.Loading

            PrimaryButton(
                text = if (isLoading) "Registering..." else "Done",
                enabled = !isLoading,
                onClick = {
                    validateInputs()
                    if (listOf(usernameError, emailError, phoneNumberError, passwordError, passwordMatchError).all { it == null }) {
                        authViewModel.register(context, username, email, phoneNumber, password, confirmPassword)
                    }
                }
            )

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
