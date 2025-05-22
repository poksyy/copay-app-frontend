package com.copay.app.ui.screen.profile.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.R
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.components.topNavBar
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.ProfileState
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.ProfileViewModel
import com.copay.app.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun editProfileScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    // Values from User Session.
    val user by userViewModel.user.collectAsState()

    // Default values if user does not have a data.
    val username = user?.username ?: "Username"
    val email = user?.email ?: "Email"
    val phoneNumber = user?.phoneNumber ?: "Phone number"

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val profileState by profileViewModel.profileState.collectAsState()

    val successSnackbarHostState = remember { SnackbarHostState() }
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(profileState) {
        val message = when (val state = profileState) {
            is ProfileState.Success.UsernameUpdated -> "Username updated successfully"
            is ProfileState.Success.EmailUpdated -> "Email updated successfully"
            is ProfileState.Error -> state.message
            else -> null
        }

        message?.let {
            coroutineScope.launch {
                val isError = profileState is ProfileState.Error
                val hostState = if (isError) errorSnackbarHostState else successSnackbarHostState
                hostState.showSnackbar(it)
            }
            profileViewModel.resetProfileState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            topNavBar(
                title = "Edit Profile",
                onBackClick = { navigationViewModel.navigateTo(SpaScreens.Profile) },
                modifier = Modifier.fillMaxWidth()
            )
            // Main content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_picture),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .padding(top = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Edit photo",
                            color = CopayColors.primary,
                            style = CopayTypography.body
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // TODO: Edit profile image

                item {
                    profileRow(
                        label = "Username",
                        value = username,
                        onClick = { navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditUsername) }
                    )
                }

                item {
                    profileRow(
                        label = "Email",
                        value = email,
                        onClick = { navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditEmail) }
                    )
                }

                item {
                    profileRow(
                        label = "Phone",
                        value = phoneNumber,
                        onClick = { navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditPhoneNumber) }
                    )
                }
            }
        }

        // Snackbar host.
        redSnackbarHost(
            hostState = errorSnackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        greenSnackbarHost(
            hostState = successSnackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun profileRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            modifier = Modifier.width(100.dp),
            color = CopayColors.primary,
            style = CopayTypography.body
        )
        Text(
            text = value,
            color = CopayColors.primary,
            style = CopayTypography.body
        )
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun editProfileScreenPreview() {
    MaterialTheme {
        editProfileScreen()
    }
}
