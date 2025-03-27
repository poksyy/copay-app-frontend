package com.copay.app.ui.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.ui.components.InputField
import com.copay.app.ui.components.PrimaryButton

@Composable
fun ForgotPasswordScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Box(modifier = Modifier.padding(top = 16.dp)) {
            BackButtonTop(navController)
        }

        Spacer(modifier = Modifier.height(135.dp))

        Text("Forgot Password", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Enter email to receive a password reset link.", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))

        InputField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (it.contains("@")) null else "Invalid email address"
            },
            label = "Email Address",
            isError = emailError != null,
            errorMessage = emailError
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Send",
            onClick = {
                if (emailError == null && email.isNotBlank()) {
                }
            }
        )
    }
}
