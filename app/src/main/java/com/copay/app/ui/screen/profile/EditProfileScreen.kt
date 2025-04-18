package com.copay.app.ui.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.ProfileViewModel
import com.copay.app.viewmodel.UserViewModel

@Composable
fun EditProfileScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    // Values from User Session.
    val user by userViewModel.user.collectAsState()

    // Default values if user does not have a data.
    val username = user?.username ?: "Username"
    val email = user?.email ?: "Email"
    val phoneNumber = user?.phoneNumber ?: "Phone number"

    Box(modifier = Modifier.fillMaxSize()) {
        // Back button
        BackButtonTop(
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.Profile) },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 72.dp)
        ) {
            Text(
                "Edit Profile",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )

            // TODO Edit profile image
            ProfileRow(label = "Username", value = username, onClick = { navigationViewModel.navigateTo(SpaScreens.EditUsername) })
            ProfileRow(label = "Email", value = email, onClick = { navigationViewModel.navigateTo(SpaScreens.EditEmail) })
            ProfileRow(label = "Phone", value = phoneNumber, onClick = { navigationViewModel.navigateTo(SpaScreens.EditPhoneNumber) })
        }
    }
}

@Composable
fun ProfileRow(
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
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
    }
    Divider()
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    MaterialTheme {
        EditProfileScreen()
    }
}
