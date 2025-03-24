package com.copay.app.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.copay.app.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.ui.components.SecondaryButton
import com.copay.app.ui.components.SignInWithGoogleButton

@Composable
fun AuthScreen(
    // Redirection to RegisterScreen.
    onSignUpClick: () -> Unit = {},
    // Redirection to LoginScreen.
    onLogInClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // Space between the image.
        Spacer(modifier = Modifier.height(200.dp))

        // Copay banner.
        Image(
            painter = painterResource(id = R.drawable.copay_banner),
            contentDescription = "Copay Logo",
        )

        // Space between the image and the button.
        Spacer(modifier = Modifier.height(30.dp))

        // Sign up button.
        PrimaryButton(
            text = "Sign up",
            onClick = onSignUpClick,
        )

        // Space between the buttons.
        Spacer(modifier = Modifier.height(16.dp))

        // Log in button.
        SecondaryButton(
            text = "Log in",
            onClick = onLogInClick,
        )

        // Space between the buttons.
        Spacer(modifier = Modifier.height(16.dp))

        // Log in with Google button (Secondary).
        SignInWithGoogleButton(
            text = "Sign in with Google",
            onClick = onLogInClick,
        )

        Spacer(modifier = Modifier.weight(1f))

        // Footer with terms and policies.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Terms", fontSize = 15.sp, color = Color.Black)
            Text(text = " | ", fontSize = 15.sp, color = Color.Black)
            Text(text = "Privacy Policy", fontSize = 15.sp, color = Color.Black)
            Text(text = " | ", fontSize = 15.sp, color = Color.Black)
            Text(text = "Contact Us", fontSize = 15.sp, color = Color.Black)
        }
    }
}
