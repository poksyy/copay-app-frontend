package com.copay.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.copay.app.ui.screen.auth.RegisterScreen
import com.copay.app.ui.screen.auth.LoginScreen
import com.copay.app.ui.screen.HubScreen
import com.copay.app.ui.screen.SplashScreen
import com.copay.app.viewmodel.SplashViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CopayNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoutes.SplashScreen.route
) {
    val splashViewModel: SplashViewModel = viewModel()
    val isDataLoaded = splashViewModel.isDataLoaded.collectAsState()

    LaunchedEffect(isDataLoaded.value) {
        if (isDataLoaded.value) {
            navController.navigate(NavRoutes.HubScreen.route) {
                // Pop the SplashScreen from the back stack after navigating
                popUpTo(NavRoutes.SplashScreen.route) { inclusive = true }
            }
        }
    }

    // Set up the navigation graph
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // SplashScreen
        composable(NavRoutes.SplashScreen.route) {
            SplashScreen(navController)
        }

        // HubScreen
        composable(NavRoutes.HubScreen.route) {
            HubScreen(
                onSignUpClick = { navController.navigate(NavRoutes.RegisterScreen.route) },
                onLogInClick = { navController.navigate(NavRoutes.LoginScreen.route) }
            )
        }

        // RegisterScreen
        composable(NavRoutes.RegisterScreen.route) {
            RegisterScreen(navController)
        }

        // LoginScreen
        composable(NavRoutes.LoginScreen.route) {
            LoginScreen(navController)
        }
    }
}
