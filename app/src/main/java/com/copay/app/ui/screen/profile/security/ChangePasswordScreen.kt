package com.copay.app.ui.screen.profile.security

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.components.button.secondaryButton
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.components.topNavBar
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.ProfileState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.ProfileViewModel

@Composable
fun changePasswordScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val profileState by profileViewModel.profileState.collectAsState()

    // Local states for managing password values and errors
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentPasswordError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var passwordMatchError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(profileState) {
        when (profileState) {
            is ProfileState.Success.PasswordUpdated -> {
                Log.d("ChangePassword", "Password updated successfully")
                navigationViewModel.navigateTo(SpaScreens.Profile)
                profileViewModel.resetProfileState()
            }

            is ProfileState.Error -> {
                Log.d("ChangePassword", "Password NOT updated")
                apiErrorMessage = (profileState as ProfileState.Error).message
                profileViewModel.resetProfileState()
            }

            else -> {}
        }
    }

    // Function to validate inputs
    fun validateInputs() {
        currentPasswordError = UserValidation.validatePassword(currentPassword).errorMessage
        newPasswordError = UserValidation.validatePassword(newPassword).errorMessage
        passwordMatchError =
            UserValidation.validatePasswordMatch(newPassword, confirmPassword).errorMessage
    }

    Box(modifier = Modifier.fillMaxSize()) {
        topNavBar(
            title = "Change password",
            onBackClick = { navigationViewModel.navigateBack() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        val scrollState = rememberScrollState()

        // Screen content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(bottom = 12.dp)
                .padding(top = 90.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Current password field
            inputField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    currentPasswordError = null
                    apiErrorMessage = null
                },
                label = "Current password",
                isPassword = true,
                isError = currentPasswordError != null
            )
            currentPasswordError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            inputField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    newPasswordError = UserValidation.validatePassword(it).errorMessage
                    apiErrorMessage = null
                },
                label = "New password",
                isPassword = true,
                isError = newPasswordError != null
            )
            newPasswordError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Password requirements
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "• Must be at least 8 characters long",
                    style = CopayTypography.footer,
                    color = CopayColors.surface
                )
                Text(
                    "• Must contain at least one uppercase letter",
                    style = CopayTypography.footer,
                    color = CopayColors.surface
                )
                Text(
                    "• Must contain at least one number",
                    style = CopayTypography.footer,
                    color = CopayColors.surface
                )
            }

            inputField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordMatchError =
                        UserValidation.validatePasswordMatch(newPassword, it).errorMessage
                    apiErrorMessage = null
                },
                label = "Confirm password",
                isPassword = true,
                isError = passwordMatchError != null
            )
            passwordMatchError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            apiErrorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            secondaryButton(
                text = "Save changes",
                onClick = {
                    validateInputs()
                    if (currentPasswordError == null && newPasswordError == null && passwordMatchError == null) {
                        profileViewModel.updatePassword(
                            context, currentPassword, newPassword, confirmPassword
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun changePasswordScreenPreview() {
    MaterialTheme {
        changePasswordScreen(
            navigationViewModel = NavigationViewModel()
        )
    }
}
