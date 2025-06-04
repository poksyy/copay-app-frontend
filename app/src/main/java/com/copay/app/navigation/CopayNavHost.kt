package com.copay.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.copay.app.ui.screen.hubScreen
import com.copay.app.ui.screen.auth.*
import com.copay.app.viewmodel.SplashViewModel
import com.copay.app.ui.screen.auth.registerStepOneScreen
import com.copay.app.ui.screen.auth.registerStepTwoScreen

@Composable
fun copayNavHost(
    navController: NavHostController,
    toggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoutes.AuthScreen.route
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    val splashViewModel: SplashViewModel = hiltViewModel()
    val isDataLoaded = splashViewModel.isDataLoaded.collectAsState()

    // Set up the navigation graph.
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        // AuthScreen.
        composable(NavRoutes.AuthScreen.route) {
            authScreen(
                onSignUpClick = { navController.navigate(NavRoutes.RegisterStepOneScreen.route) },
                onLogInClick = { navController.navigate(NavRoutes.LoginScreen.route) },
                onGoogleRegister = { navController.navigate(NavRoutes.RegisterStepTwoScreen.route)},
                onGoogleLogin = { navController.navigate(NavRoutes.HubScreen.route)},
                onToggleTheme = {
                    isDarkTheme = !isDarkTheme
                    toggleTheme()
                },
                isDarkTheme = isDarkTheme
            )
        }

        // RegisterStepOneScreen.
        composable(NavRoutes.RegisterStepOneScreen.route) {
            registerStepOneScreen(
                navController,
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.RegisterStepTwoScreen.route) {
                    }
                }
            )
        }

        // RegisterStepTwoScreen.
        composable(NavRoutes.RegisterStepTwoScreen.route) {
            registerStepTwoScreen(
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.HubScreen.route)
                },

                )
        }

        // LoginScreen.
        composable(NavRoutes.LoginScreen.route) {

            loginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.HubScreen.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(NavRoutes.ForgotPasswordScreen.route) {
                        popUpTo(NavRoutes.LoginScreen.route) { inclusive = true }
                    }

                },

                )
        }

        // ForgotPasswordScreen.
        composable(NavRoutes.ForgotPasswordScreen.route) {
            forgotPasswordScreen(navController = navController)
        }

        // HubScreen.
        composable(NavRoutes.HubScreen.route) {
            hubScreen(
                onLogoutSuccess = {
                    navController.navigate(NavRoutes.AuthScreen.route)
                }
            )
        }
    }
}