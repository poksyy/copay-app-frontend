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
import com.copay.app.ui.components.button.secondaryButton
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.components.TopNavBar
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.ProfileState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.ProfileViewModel
import com.copay.app.viewmodel.UserViewModel

@Composable
fun editUsernameScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val user by userViewModel.user.collectAsState()
    val profileState by profileViewModel.profileState.collectAsState()

    // Local states for managing the username value and errors
    var username by remember(user?.username) { mutableStateOf(user?.username ?: "") }
    var usernameError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(profileState) {
        when (profileState) {
            is ProfileState.Success.UsernameUpdated -> {
                navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditProfile)
            }
            is ProfileState.Error -> {
                navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditProfile)
            }
            else -> {}
        }
    }

    // Function to validate inputs.
    fun validateInputs() {
        usernameError = UserValidation.validateUsername(username).errorMessage
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TopNavBar(
            title = "Edit username",
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
                color = CopayColors.onSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            secondaryButton(
                text = "Save changes",
                onClick = {
                    validateInputs()
                    if (usernameError == null) {
                        profileViewModel.updateUsername(context, username)
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
fun EditUsernameScreenPreview() {
    MaterialTheme {
        editUsernameScreen(
            navigationViewModel = NavigationViewModel()
        )
    }
}