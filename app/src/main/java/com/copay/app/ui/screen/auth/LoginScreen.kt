package com.copay.app.ui.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.ui.components.InputField
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.ui.theme.CopayTheme

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Back button in the top-left corner
        Box(modifier = Modifier.padding(top = 16.dp)) {
            BackButtonTop(navController)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(128.dp))

            Text(text = "Log in", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(32.dp))

            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            InputField(
                value = username,
                onValueChange = { username = it },
                label = "Username"
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Log in",
                onClick = { /* Handle login action */ },
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Forgot your password?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    CopayTheme {
        val fakeNavController = rememberNavController()
        LoginScreen(navController = fakeNavController)
    }
}
