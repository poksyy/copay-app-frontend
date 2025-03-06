package com.copay.app.ui.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.ui.components.InputField
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.ui.theme.CopayTheme

@Composable
fun RegisterScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Back button in the top-left corner
        Box(modifier = Modifier.padding(top = 16.dp)) {
            BackButtonTop(navController)
        }

        // Content with padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(64.dp))

            Text("Welcome to Copay!", style = MaterialTheme.typography.titleLarge)
            Text("Let's set up your account.", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(32.dp))

//          var phone by remember { mutableStateOf("") }
            var username by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var passwordConfirmation by remember { mutableStateOf("") }

            /*
            We will implement this into a screen. The first time that the users
            enters in the application
             */

//            InputField(
//                value = phone,
//                onValueChange = { phone = it },
//                label = "Phone Number",
//                isPassword = false
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                isRequired = false
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                isRequired = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true,
                isRequired = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = passwordConfirmation,
                onValueChange = { passwordConfirmation = it },
                label = "Confirm Password",
                isPassword = true,
                isRequired = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Done",
                onClick = { /* Handle Login Action */ },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    CopayTheme {
        val fakeNavController = rememberNavController()
        RegisterScreen(navController = fakeNavController)
    }
}
