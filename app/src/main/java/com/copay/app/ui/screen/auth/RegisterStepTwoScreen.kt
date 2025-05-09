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
import com.copay.app.ui.components.input.PhoneNumberField
import com.copay.app.ui.components.button.PrimaryButton
import com.copay.app.ui.components.input.countriesList
import com.copay.app.ui.theme.CopayColors
import com.copay.app.utils.state.AuthState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.AuthViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun RegisterStepTwoScreen(
    onRegisterSuccess: () -> Unit = {}
) {
    // Use hiltViewModel to obtain the injected AuthViewModel with userRepository and userService.
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    val context = LocalContext.current

    // Keep state of phone number and selected country separately.
    var phoneNumber by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf(countriesList.first { it.code == "ES" }) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Function to validate the number in international format.
    fun validateInputs() {
        val validationResult = UserValidation.validatePhoneNumber(phoneNumber, selectedCountry.dialCode)
        phoneNumberError = validationResult.errorMessage
    }

    // Effect triggered when the authentication state changes.
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // Redirection to HubScreen
                onRegisterSuccess()
            }

            is AuthState.Error -> {
                apiErrorMessage = (authState as AuthState.Error).message
                isLoading = false
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CopayColors.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Your Phone Number", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Country code picker.
        PhoneNumberField(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = {
                phoneNumber = it
                validateInputs()
            },
            selectedCountry = selectedCountry,
            onCountrySelected = {
                selectedCountry = it
                validateInputs()
            },
            isError = phoneNumberError != null,
            errorMessage = phoneNumberError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Submit",
            enabled = !isLoading && phoneNumber.isNotEmpty() && phoneNumberError == null,
            onClick = {
                validateInputs()
                if (phoneNumberError == null) {
                    isLoading = true

                    // Save phone prefix and phone number separately
                    val phonePrefix = selectedCountry.dialCode

                    // Pass the prefix and number separately to the backend API
                    authViewModel.registerStepTwo(context, phonePrefix, phoneNumber)
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
