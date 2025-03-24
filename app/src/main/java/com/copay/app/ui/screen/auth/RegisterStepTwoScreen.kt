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
import com.copay.app.repository.UserRepository
import com.copay.app.ui.components.InputField
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.AuthState
import com.copay.app.viewmodel.AuthViewModel

@Composable
fun RegisterStepTwoScreen(userRepository: UserRepository) {
    val viewModelFactory = remember { AuthViewModelFactory(userRepository) }
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
    val authState by authViewModel.authState.collectAsState()

    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Function to validate inputs.
    fun validateInputs() {
        phoneNumberError = UserValidation.validateRegisterPhoneNumber(phoneNumber).errorMessage
    }

    // Effect triggered when the authentication state changes.
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // TODO: Handle success (navigate to home screen)
            }
            is AuthState.Error -> {
                apiErrorMessage = (authState as AuthState.Error).message
                isLoading = false
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Your Phone Number", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
                phoneNumberError = UserValidation.validateRegisterPhoneNumber(it).errorMessage
            },
            label = "Phone Number",
            isError = phoneNumberError != null,
            errorMessage = phoneNumberError
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isLoading) "Loading..." else "Submit",
            enabled = !isLoading,
            onClick = {
                validateInputs()
                if (phoneNumberError == null) {
                    isLoading = true
                    authViewModel.registerStepTwo(context, phoneNumber)
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
