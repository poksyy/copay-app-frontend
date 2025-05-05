package com.copay.app.ui.screen.profile.security

import android.util.Log
import androidx.compose.foundation.layout.*
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
import com.copay.app.ui.components.button.BackButtonTop
import com.copay.app.ui.components.input.InputField
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.ProfileState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.ProfileViewModel

@Composable
fun ChangePasswordScreen(
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
        // Back button
        BackButtonTop(
            onBackClick = { navigationViewModel.navigateBack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        TextButton(
            onClick = {
                validateInputs()
                if (currentPasswordError == null && newPasswordError == null && passwordMatchError == null) {
                    profileViewModel.updatePassword(
                        context, currentPassword, newPassword, confirmPassword
                    )
                }
            }, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                "Done",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Screen content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 72.dp)
        ) {
            Text(
                "Change password", color = CopayColors.primary, style = CopayTypography.title
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Current password field
            InputField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    currentPasswordError = null
                },
                label = "Current password",
                isPassword = true,
                isError = currentPasswordError != null
            )

            Spacer(modifier = Modifier.height(12.dp))

            // New password field
            InputField(
                value = newPassword, onValueChange = {
                    newPassword = it
                    newPasswordError = UserValidation.validatePassword(it).errorMessage
                }, label = "New password", isPassword = true, isError = newPasswordError != null
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Retype password field
            InputField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordMatchError =
                        UserValidation.validatePasswordMatch(newPassword, it).errorMessage
                },
                label = "Retype password",
                isPassword = true,
                isError = passwordMatchError != null
            )

            // Show error messages if any
            listOfNotNull(
                currentPasswordError, passwordMatchError, apiErrorMessage
            ).forEach { errorMsg ->
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Password requirements
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "• Must be at least 8 characters long",
                    style = CopayTypography.footer,
                    color = CopayColors.surface,
                )
                Text(
                    "• Must contain at least one uppercase letter",
                    style = CopayTypography.footer,
                    color = CopayColors.surface,
                )
                Text(
                    "• Must contain at least one number",
                    style = CopayTypography.footer,
                    color = CopayColors.surface,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    MaterialTheme {
        ChangePasswordScreen(
            navigationViewModel = NavigationViewModel()
        )
    }
}