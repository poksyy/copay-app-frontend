package com.copay.app.ui.screen.profile.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.dialog.logoutAfterPhoneChangeDialog
import com.copay.app.ui.components.button.secondaryButton
import com.copay.app.ui.components.input.countriesList
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.components.TopNavBar
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.ProfileState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.AuthViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.ProfileViewModel
import com.copay.app.viewmodel.UserViewModel

@Composable
fun editPhoneNumberScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val user by userViewModel.user.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Extract country prefix and local number
    val country = countriesList.find { user?.phonePrefix?.startsWith(it.dialCode) == true } ?: countriesList.first()

    // Local states for managing the phone number value and errors
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var phoneNumber by remember { mutableStateOf(user?.phoneNumber ?: "") }
    val selectedCountry by remember { mutableStateOf(country) }

    LaunchedEffect(profileState) {
        when (profileState) {
            is ProfileState.Success.PhoneUpdated -> {
                authViewModel.logout(context)
            }
            is ProfileState.Error -> {
                apiErrorMessage = (profileState as ProfileState.Error).message
                profileViewModel.resetProfileState()
            }
            else -> {}
        }
    }

    // Function to validate inputs.
    fun validateInputs() {
        val validationResult = UserValidation.validatePhoneNumber(phoneNumber, selectedCountry.dialCode)
        phoneNumberError = validationResult.errorMessage
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TopNavBar(
            title = "Edit phone number",
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditProfile) },
        )

        // Screen content.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 90.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            inputField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    validateInputs()
                    apiErrorMessage = null
                },
                label = "Phone number",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = phoneNumberError != null
            )

            // Show error message if API fails or wrong phone number format.
            listOfNotNull(phoneNumberError, apiErrorMessage).forEach { errorMsg ->
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            // Phone number's description
            Text(
                text = "Your phone number is used for account verification, recovery, and notifications. Make sure it's a valid and active number that you can be reached at.",
                style = CopayTypography.footer,
                color = CopayColors.onSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            secondaryButton(
                text = "Save changes",
                onClick = {
                    validateInputs()
                    if (phoneNumberError == null) {
                        showLogoutDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }

        if (showLogoutDialog) {
            logoutAfterPhoneChangeDialog(
                onDismiss = { showLogoutDialog = false },
                onConfirm = {
                    profileViewModel.updatePhoneNumber(context, phoneNumber)
                    showLogoutDialog = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun editPhoneNumberScreenPreview() {
    MaterialTheme {
        editPhoneNumberScreen(
            navigationViewModel = NavigationViewModel()
        )
    }
}