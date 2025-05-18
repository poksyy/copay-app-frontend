package com.copay.app.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.copay.app.repository.PasswordRepository
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.components.button.primaryButton
import com.copay.app.config.RetrofitInstance
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.viewmodel.ForgotPasswordViewModel
import com.copay.app.viewmodel.ForgotPasswordViewModelFactory

@Composable
fun forgotPasswordScreen(navController: NavController) {
    val repository = PasswordRepository(RetrofitInstance.api)

    val viewModel: ForgotPasswordViewModel = viewModel(
        factory = ForgotPasswordViewModelFactory(repository)
    )

    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(CopayColors.background)
    ) {
        // Back button in the top-left corner.
        Box(modifier = Modifier.padding(top = 16.dp)) {
            backButtonTop(navController)
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(135.dp))

            Text(
                "Forgot Password",
                color = CopayColors.primary,
                style = CopayTypography.title
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Enter email to receive a password reset link.",
                color = CopayColors.primary,
                style = CopayTypography.subtitle
            )

            Spacer(modifier = Modifier.height(24.dp))

            inputField(
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

            primaryButton(
                text = "Send",
                onClick = {
                    if (emailError == null && email.isNotBlank()) {
                        viewModel.forgotPassword(email,
                            onSuccess = { message = "Email sent successfully!" },
                            onError = { errorMsg -> message = errorMsg }
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            message?.let {
                Text(text = it, color = if (it.contains("success")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            }
        }
    }
}
