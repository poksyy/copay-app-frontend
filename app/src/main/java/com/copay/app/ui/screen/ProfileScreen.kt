package com.copay.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.copay.app.R
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.LogoutButton
import com.copay.app.viewmodel.AuthViewModel

@Composable
fun ProfileScreen() {
    val authViewModel: AuthViewModel = hiltViewModel()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    ProfileContent(
        onLogoutClick = { showLogoutDialog = true }
    )

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = { authViewModel.logout(context) }
        )
    }
}

@Composable
private fun ProfileContent(onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.google_256),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Dylan Navarro",
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground)
        )

        Spacer(modifier = Modifier.height(32.dp))

        ProfileOptionsList()

        Spacer(modifier = Modifier.weight(1f))

        LogoutButton(text = "Log out", onClick = onLogoutClick)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileOptionsList() {
    val options = listOf(
        "Notifications",
        "Payment Methods",
        "Help & Support",
        "Language Settings",
        "Security",
        "Privacy Policy",
        "Terms of Service",
        "Account Settings"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            Text(
                text = option,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Logout") },
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}