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
import com.copay.app.ui.components.button.BackButtonTop
import com.copay.app.ui.components.input.InputField
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.ProfileState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.ProfileViewModel
import com.copay.app.viewmodel.UserViewModel

@Composable
fun EditUsernameScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val user by userViewModel.user.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()

    // Local states for managing the username value and errors
    var username by remember(user?.username) { mutableStateOf(user?.username?: "") }
    var usernameError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(profileState) {
        when (profileState) {
            is ProfileState.Success.UsernameUpdated -> {
                navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditProfile)
                profileViewModel.resetProfileState()
            }

            else -> {}
        }
    }

    // Function to validate inputs.
    fun validateInputs() {
        usernameError = UserValidation.validateUsername(username).errorMessage
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
                if (usernameError == null ) {
                    profileViewModel.updateUsername(context, username)
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                "Done",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 72.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Edit Username",
                color = CopayColors.primary,
                style = CopayTypography.title
            )

            InputField(
                value = username,
                onValueChange = {
                    username = it
                    validateInputs()
                },
                label = "Username",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = usernameError != null
            )

            // Show username error if any.
            usernameError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "Your username is how people will identify you on the platform." +
                        "Make sure it reflects your identity or something you'll be " +
                        "comfortable with others seeing.",
                style = CopayTypography.footer,
                color = CopayColors.surface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditUsernameScreenPreview() {
    MaterialTheme {
        EditUsernameScreen(
            navigationViewModel = NavigationViewModel()
        )
    }
}