package com.copay.app.ui.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.copay.app.R
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.logoutButton
import com.copay.app.ui.components.dialog.logoutDialog
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.viewmodel.AuthViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.UserViewModel

@Composable
fun profileScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val user by userViewModel.user.collectAsState()
    val username = user?.username ?: "Username"

    ProfileContent(
        onEditProfileClick = { navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.EditProfile) },
        onSecurityClick = { navigationViewModel.navigateTo(SpaScreens.ProfileSubscreen.ChangePassword) },
        onLogoutClick = { showLogoutDialog = true },
        username = username
    )

    if (showLogoutDialog) {
        logoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                authViewModel.logout(context)
                navigationViewModel.clearHistory()
            }
        )
    }
}

@Composable
private fun ProfileContent(
    onEditProfileClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onLogoutClick: () -> Unit,
    username: String
) {
    val options = listOf(
        "Help & Support",
        "Language Settings",
        "Password & Security",
        "Privacy Policy",
        "Account Settings"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onEditProfileClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = CopayColors.onPrimary,
                        contentColor = CopayColors.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "Edit Profile",
                        style = CopayTypography.button
                    )
                }
                Button(
                    onClick = { /* TODO: Add share profile action to add as a friend (url) */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = CopayColors.onPrimary,
                        contentColor = CopayColors.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "Share Profile",
                        style = CopayTypography.button
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        items(options) { option ->
            val isEnabled = option == "Password & Security"

            profileOptionItem(
                option = option,
                enabled = isEnabled,
                onClick = {
                    if (isEnabled) {
                        onSecurityClick()
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            logoutButton(text = "Log out", onClick = onLogoutClick)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun profileOptionItem(
    option: String,
    enabled: Boolean,
    onClick: () -> Unit,
){
    val optionIcons = mapOf(
        "Help & Support" to ImageVector.vectorResource(R.drawable.ic_help),
        "Language Settings" to ImageVector.vectorResource(R.drawable.ic_language),
        "Password & Security" to ImageVector.vectorResource(R.drawable.ic_security),
        "Privacy Policy" to ImageVector.vectorResource(R.drawable.ic_privacy),
        "Account Settings" to ImageVector.vectorResource(R.drawable.ic_account)
    )

    val icon = optionIcons[option] ?: ImageVector.vectorResource(R.drawable.ic_general)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .alpha(if (enabled) 1f else 0.4f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CopayColors.onPrimary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = option,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = option,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}