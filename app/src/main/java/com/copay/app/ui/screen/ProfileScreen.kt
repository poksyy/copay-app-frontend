package com.copay.app.ui.screen

import AuthViewModelFactory
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.R
import com.copay.app.config.RetrofitInstance
import com.copay.app.ui.components.LogoutButton
import com.copay.app.repository.UserRepository
import com.copay.app.viewmodel.AuthViewModel

@Composable
fun ProfileScreen() {
    val userRepository = remember { UserRepository(RetrofitInstance.authService) }
    val viewModelFactory = remember { AuthViewModelFactory(userRepository) }
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)

    ProfileContent(authViewModel = authViewModel)
}

@Composable
private fun ProfileContent(authViewModel: AuthViewModel) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // TODO Profile image
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

        // TODO This is not the definitive list for the profile screen.
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Payment Methods",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Help & Support",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Language Settings",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Security",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Privacy Policy",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Terms of Service",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Account Settings",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        LogoutButton(text = "Log out", onClick = { showLogoutDialog = true })

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showLogoutDialog) {
        AlertDialog(onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "Confirm Logout") },
            text = { Text(text = "Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    authViewModel.logout(context)
                }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(text = "No")
                }
            })
    }
}