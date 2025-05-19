package com.copay.app.ui.screen.profile.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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
import com.copay.app.viewmodel.UserViewModel

@Composable
fun editEmailScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val user by userViewModel.user.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()

    // Local states for managing the email value and errors
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }
    var email by remember(user?.email) { mutableStateOf(user?.email ?: "") }
    var emailError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(profileState) {
        when (profileState) {
            is ProfileState.Success.EmailUpdated -> {
                navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditProfile)
                profileViewModel.resetProfileState()
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
        emailError = UserValidation.validateEmail(email).errorMessage
    }

    Box(modifier = Modifier.fillMaxSize()) {
        topNavBar(
            title = "Edit email",
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditProfile) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        // Screen content.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 90.dp),
        ) {
            // Text field for email.
            inputField(
                value = email,
                onValueChange = {
                    email = it
                    validateInputs()
                    apiErrorMessage = null
                },
                label = "Email",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null
            )

            // Show error message if API fails or wrong email format.
            listOfNotNull(emailError, apiErrorMessage).forEach { errorMsg ->
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Email's description.
            Text(
                text = "Your email is used to send notifications, updates, and important information related to your account. It should be a valid and accessible email address that you check regularly.",
                style = CopayTypography.footer,
                color = CopayColors.surface,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            secondaryButton(
                text = "Save changes",
                onClick = {
                    validateInputs()
                    if (emailError == null) {
                        profileViewModel.updateEmail(context, email)
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
fun editEmailScreenPreview() {
    MaterialTheme {
        editEmailScreen(
            navigationViewModel = NavigationViewModel()
        )
    }
}