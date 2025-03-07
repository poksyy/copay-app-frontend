package com.copay.app.ui.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.ui.components.InputField
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.ui.theme.CopayTheme
import com.copay.app.validation.UserValidation

@Composable
fun RegisterScreen(navController: NavController) {
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

//          var phone by remember { mutableStateOf("") }
            var username by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var passwordConfirmation by remember { mutableStateOf("") }
            var usernameError by remember { mutableStateOf<String?>(null) }
            var emailError by remember { mutableStateOf<String?>(null) }
            var passwordError by remember { mutableStateOf<String?>(null) }
            var passwordMatch by remember { mutableStateOf<String?>(null) }

            // Function to validate all inputs
            fun validateInputs() {
                usernameError = UserValidation.validateRegisterUsername(username).errorMessage
                emailError = UserValidation.validateEmail(email).errorMessage
                passwordError = UserValidation.validateRegisterPassword(password).errorMessage
                passwordMatch = UserValidation.validatePasswordMatch(password, passwordConfirmation).errorMessage
            }

            /*
            We will implement this into a screen. The first time that the users
            enters in the application.
             */

//            InputField(
//                value = phone,
//                onValueChange = { phone = it },
//                label = "Phone Number",
//                isPassword = false
//            )

            InputField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = UserValidation.validateRegisterUsername(it).errorMessage
                },
                label = "Username",
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
                isPassword = true,
                isError = passwordError != null,
                errorMessage = passwordError
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = passwordConfirmation,
                onValueChange = {
                    passwordConfirmation = it
                    passwordMatch = UserValidation.validatePasswordMatch(password, it).errorMessage
                },
                label = "Confirm Password",
                isPassword = true,
                isError = passwordMatch != null,
                errorMessage = passwordMatch
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Done",
                onClick = {
                    validateInputs()

                    if (listOf(usernameError, emailError, passwordError, passwordMatch).all { it == null }) {
                        // Handle register action here
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    CopayTheme {
        val fakeNavController = rememberNavController()
        RegisterScreen(navController = fakeNavController)
    }
}
