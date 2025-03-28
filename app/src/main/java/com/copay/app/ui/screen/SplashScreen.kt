package com.copay.app.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.copay.app.R
import com.copay.app.navigation.NavRoutes
import com.copay.app.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
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
    SplashScreenContent()
}

@Composable
fun SplashScreenContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.copay_logo_512),
            contentDescription = "Logo",
            modifier = Modifier.padding(bottom = 40.dp)
        )

        CircularProgressIndicator(color = Color.Black)
    }
}
