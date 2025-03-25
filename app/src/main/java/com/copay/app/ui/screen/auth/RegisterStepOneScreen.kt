package com.copay.app.ui.screen.auth

import AuthViewModelFactory
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

// TODO: ADD THE CALLBACK TO REGISTERSTEPTWO REDIRECTION
@Composable
fun RegisterStepOneScreen(
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
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordMatchError by remember { mutableStateOf<String?>(null) }

    var apiErrorMessage by remember { mutableStateOf<String?>(null) }
    
    // Effect triggered when the authentication state changes.
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // Redirection to RegisterStepTwoScreen.
                onRegisterSuccess();
            }
            is AuthState.Error -> {
                apiErrorMessage = (authState as AuthState.Error).message
            }
            else -> {}
        }
    }

    // Function to validate all inputs.
    fun validateInputs() {
        usernameError = UserValidation.validateRegisterUsername(username).errorMessage
        emailError = UserValidation.validateEmail(email).errorMessage
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

            PrimaryButton(
                text = "Continue",
                onClick = {
                    validateInputs()
                    if (listOf(usernameError, emailError, passwordError, passwordMatchError).all { it == null }) {
                        authViewModel.registerStepOne(context, username, email, password, confirmPassword)
                    }
                }
            )

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
