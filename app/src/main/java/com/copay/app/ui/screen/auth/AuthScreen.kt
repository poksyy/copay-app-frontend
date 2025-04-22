package com.copay.app.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import com.copay.app.R
import androidx.compose.ui.unit.dp
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.ui.components.SecondaryButton
import com.copay.app.ui.components.SignInWithGoogleButton
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun AuthScreen(
    // Redirection to RegisterScreen.
    onSignUpClick: () -> Unit = {},
    // Redirection to LoginScreen.
    onLogInClick: () -> Unit = {},
    // Swap theme to Dark Mode or vice versa.
    onToggleTheme: () -> Unit = {},
    isDarkTheme: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CopayColors.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Theme toggle button in top right corner.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onToggleTheme,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isDarkTheme) R.drawable.ic_dark_mode else R.drawable.ic_light_mode
                    ),
                    contentDescription = if (isDarkTheme) "Light Mode" else "Dark Mode",
                    tint = CopayColors.onBackground
                )
            }
        }

        // Space between the image.
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        Spacer(modifier = Modifier.height(screenHeight * 0.2f))

        val bannerRes = if (isDarkTheme) R.drawable.copay_banner_white else R.drawable.copay_banner_black

        // Copay banner.
        Image(
            painter = painterResource(id = bannerRes),
            contentDescription = "Copay Logo",
        )

        // Space between the image and the button.
        Spacer(modifier = Modifier.height(30.dp))

        // Sign up button.
        PrimaryButton(
            text = "Sign up",
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        )

        // Space between the buttons.
        Spacer(modifier = Modifier.height(16.dp))

        // Log in button.
        SecondaryButton(
            text = "Log in",
            onClick = onLogInClick,
            modifier = Modifier.fillMaxWidth()
        )

        // Space between the buttons.
        Spacer(modifier = Modifier.height(16.dp))

        // Log in with Google button (Secondary).
        SignInWithGoogleButton(
            text = "Sign in with Google",
            onClick = onLogInClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        // Footer with terms and policies.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Terms", style = CopayTypography.footer, color = CopayColors.onBackground)
            Text(text = " | ", style = CopayTypography.footer, color = CopayColors.onBackground)
            Text(text = "Privacy Policy", style = CopayTypography.footer, color = CopayColors.onBackground)
            Text(text = " | ", style = CopayTypography.footer, color = CopayColors.onBackground)
            Text(text = "Contact Us", style = CopayTypography.footer, color = CopayColors.onBackground)
        }
    }
}
