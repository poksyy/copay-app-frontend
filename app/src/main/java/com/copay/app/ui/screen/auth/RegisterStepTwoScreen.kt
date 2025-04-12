package com.copay.app.ui.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.copay.app.ui.components.PhoneNumberField
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.utils.state.AuthState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.AuthViewModel

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
    var selectedCountry by remember { mutableStateOf(com.copay.app.ui.components.countriesList.first { it.code == "ES" }) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // This function combines the country code with the number for E.164 format.
    fun getE164PhoneNumber(): String {
        // Remove unnecessary characters and spaces.
        val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        // E.164 format should have the + sign followed by country code and number.
        return "${selectedCountry.dialCode}$cleanNumber"
    }

    // Function to validate the number in international format.
    fun validateInputs() {
        phoneNumberError = UserValidation.validatePhoneNumber(phoneNumber).errorMessage
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
            errorMessage = phoneNumberError
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Submit",  // Removed loading state text here
            enabled = !isLoading && phoneNumber.isNotEmpty() && phoneNumberError == null,
            onClick = {
                validateInputs()
                if (phoneNumberError == null) {
                    isLoading = true
                    // Send the complete E.164 formatted phone number to the backend.
                    val completePhoneNumber = getE164PhoneNumber()
                    authViewModel.registerStepTwo(context, completePhoneNumber)
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
