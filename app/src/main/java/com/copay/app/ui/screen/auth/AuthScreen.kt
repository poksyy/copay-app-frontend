package com.copay.app.ui.screen.auth

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.copay.app.R
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.copay.app.ui.components.button.primaryButton
import com.copay.app.ui.components.button.secondaryButton
import com.copay.app.ui.components.button.signInWithGoogleButton
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.viewmodel.AuthViewModel
import com.google.android.gms.common.api.ApiException

@Composable
fun authScreen(
    // Redirection to RegisterScreen.
    onSignUpClick: () -> Unit = {},
    // Redirection to LoginScreen.
    onLogInClick: () -> Unit = {},
    // Redirection to RegisterStepTwoScreen.
    onGoogleRegister : () -> Unit = {},
    // Redirection to HubScreen.
    onGoogleLogin : () -> Unit = {},
    // Swap theme to Dark Mode or vice versa.
    onToggleTheme: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel(),
    isDarkTheme: Boolean
) {

    val isLogin by authViewModel.isLoginResult.collectAsState(initial = null)

    LaunchedEffect(isLogin) {
        when (isLogin) {
            true -> onGoogleLogin()
            false -> onGoogleRegister()
            null -> { /* Do nothing */ }
        }
    }

    val context = LocalContext.current

    // Google Sign in configuration.
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("GoogleSignIn", "Result code: ${result.resultCode}")
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            Log.d("GoogleSignIn", "Intent data received.")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("GoogleSignIn", "ID Token: ${account.idToken}")
                // ...
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "signInResult: failed code=" + e.statusCode)
                e.printStackTrace()
            }
            authViewModel.handleSignInResult(task, context)
        } else {
            Log.e("GoogleSignIn", "Google Sign-In canceled or failed.")
        }
    }

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
        Spacer(modifier = Modifier.height(screenHeight * 0.1f))

        val bannerRes = if (isDarkTheme) R.drawable.copay_banner_white else R.drawable.copay_banner_black

        // Copay banner.
        Image(
            painter = painterResource(id = bannerRes),
            contentDescription = "Copay Logo",
        )

        // Space between the image and the button.
        Spacer(modifier = Modifier.height(30.dp))

        // Sign up button.
        primaryButton(
            text = "Sign up",
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        )

        // Space between the buttons.
        Spacer(modifier = Modifier.height(16.dp))

        // Log in button.
        secondaryButton(
            text = "Log in",
            onClick = onLogInClick,
            modifier = Modifier.fillMaxWidth()
        )

        // Space between the buttons.
        Spacer(modifier = Modifier.height(16.dp))

        // Log in with Google button (Secondary).
        signInWithGoogleButton(
            text = "Sign in with Google",
            onClick = {
                googleSignInClient.signOut().addOnCompleteListener {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
            },
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