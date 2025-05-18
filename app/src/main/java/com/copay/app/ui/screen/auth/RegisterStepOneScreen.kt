package com.copay.app.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.components.button.primaryButton
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.AuthState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.AuthViewModel

@Composable
fun registerStepOneScreen(
    navController: NavController,
    onRegisterSuccess: () -> Unit = {}
) {

    // Use hiltViewModel to obtain the injected AuthViewModel with userRepository and userService.
    val authViewModel: AuthViewModel = hiltViewModel()
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
                onRegisterSuccess()
            }

            is AuthState.Error -> {
                apiErrorMessage = (authState as AuthState.Error).message
            }

            else -> {}
        }
    }

    // Function to validate all inputs.
    fun validateInputs() {
        usernameError = UserValidation.validateUsername(username).errorMessage
        emailError = UserValidation.validateEmail(email).errorMessage
        passwordError = UserValidation.validatePassword(password).errorMessage
        passwordMatchError =
            UserValidation.validatePasswordMatch(password, confirmPassword).errorMessage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CopayColors.background)
    ) {
        // Back button in the top-left corner
        Box(modifier = Modifier.padding(top = 16.dp)) {
            backButtonTop(navController)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                "Welcome to Copay!",
                style = CopayTypography.title,
                color = CopayColors.primary
            )
            Text(
                "Let's set up your account.",
                style = CopayTypography.subtitle,
                color = CopayColors.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            inputField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = UserValidation.validateUsername(it).errorMessage
                },
                label = "Username",
                isRequired = true,
                isError = usernameError != null,
                errorMessage = usernameError
            )
            Spacer(modifier = Modifier.height(12.dp))

            inputField(
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

            inputField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = UserValidation.validatePassword(it).errorMessage
                },
                label = "Password",
                isRequired = true,
                isPassword = true,
                isError = passwordError != null,
                errorMessage = passwordError
            )
            Spacer(modifier = Modifier.height(12.dp))

            inputField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordMatchError =
                        UserValidation.validatePasswordMatch(password, it).errorMessage
                },
                label = "Confirm Password",
                isRequired = true,
                isPassword = true,
                isError = passwordMatchError != null,
                errorMessage = passwordMatchError
            )
            Spacer(modifier = Modifier.height(24.dp))

            primaryButton(
                text = "Continue",
                onClick = {
                    validateInputs()
                    if (listOf(
                            usernameError,
                            emailError,
                            passwordError,
                            passwordMatchError
                        ).all { it == null }
                    ) {
                        authViewModel.registerStepOne(
                            context,
                            username,
                            email,
                            password,
                            confirmPassword
                        )
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
