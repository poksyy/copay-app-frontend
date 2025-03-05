package com.copay.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.copay.app.ui.screen.HubScreen

@Composable
fun CopayNavHost(
    // NavController to manage navigation, passed from MainActivity.
    navController: NavHostController,
    // Modifier for UI customization.
    modifier: Modifier = Modifier,
    // TODO: ADD SPLASH SCREEN HERE:
    startDestination: String = NavRoutes.HubScreen.route
) {
    NavHost(
        navController = navController,
        // Set the start destination screen.
        startDestination = startDestination,
        // Apply modifier to NavHost.
        modifier = modifier
    ) {
        composable(NavRoutes.HubScreen.route) {
            HubScreen(
                onSignUpClick = { navController.navigate(NavRoutes.RegisterScreen.route) },
                onLogInClick = { navController.navigate(NavRoutes.LoginScreen.route) }
            )
        }

        composable(NavRoutes.LoginScreen.route) {
//            LoginScreen(navController)
        }

        composable(NavRoutes.RegisterScreen.route) {
//            RegisterScreen(navController)
        }
    }
}
