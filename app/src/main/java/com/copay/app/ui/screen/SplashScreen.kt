package com.copay.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.copay.app.R
import com.copay.app.ui.theme.CopayColors
import com.copay.app.viewmodel.SplashViewModel

@Composable
fun splashScreen(
    // Redirection to AuthScreen.
    automaticRedirection: () -> Unit = {},
    viewModel: SplashViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val isDataLoaded by viewModel.isDataLoaded.collectAsState()

    LaunchedEffect(isDataLoaded) {
        if (isDataLoaded) {
            automaticRedirection()
        }
    }
    splashScreenContent()
}

@Composable
fun splashScreenContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(CopayColors.background)
    ) {

        val isDarkTheme = isSystemInDarkTheme()
        val logoRes = if (isDarkTheme) R.drawable.copay_logo_512_white else R.drawable.copay_logo_512_black

        Image(
            painter = painterResource(id = logoRes),
            contentDescription = "Logo",
            modifier = Modifier.padding(bottom = 40.dp)
        )

        CircularProgressIndicator(color = CopayColors.primary)
    }
}
