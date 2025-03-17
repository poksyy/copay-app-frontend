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
import com.copay.app.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, userRepository: UserRepository) {
    val viewModelFactory = remember { AuthViewModelFactory(userRepository) }
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Back button in the top-left corner
        Box(modifier = Modifier.padding(top = 16.dp)) {
            BackButtonTop(navController)
        }

        // Content with padding
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

            var username by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var phoneNumber by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var usernameError by remember { mutableStateOf<String?>(null) }
            var emailError by remember { mutableStateOf<String?>(null) }
            var phoneNumberError by remember { mutableStateOf<String?>(null) }
            var passwordError by remember { mutableStateOf<String?>(null) }
            var passwordMatch by remember { mutableStateOf<String?>(null) }

            // Function to validate all inputs
            fun validateInputs() {
                usernameError = UserValidation.validateRegisterUsername(username).errorMessage
                emailError = UserValidation.validateEmail(email).errorMessage
                phoneNumberError = UserValidation.validateRegisterPhoneNumber(phoneNumber).errorMessage
                passwordError = UserValidation.validateRegisterPassword(password).errorMessage
                passwordMatch = UserValidation.validatePasswordMatch(password, confirmPassword).errorMessage
            }

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

            /*
            We will implement Phone Number in a single screen.
            */

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
                    passwordMatch = UserValidation.validatePasswordMatch(password, it).errorMessage
                },
                label = "Confirm Password",
                isRequired = true,
                isPassword = true,
                isError = passwordMatch != null,
                errorMessage = passwordMatch
            )

            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current

            PrimaryButton(
                text = "Done",
                onClick = {
                    validateInputs()

                    if (listOf(usernameError, emailError, passwordError, passwordMatch).all { it == null }) {
                        Log.d("RegisterScreen", "Login button clicked. Sending data -> Phone: $username, Password: $password")
                        authViewModel.register(context, username, email, phoneNumber, password, confirmPassword)
                    } else {
                        Log.e("RegisterScreen", "Validation errors: PhoneError=$usernameError, PasswordError=$passwordError")
                    }
                }
            )
        }
    }
}