package com.copay.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.copay.app.ui.screen.HubScreen
import com.copay.app.ui.screen.SplashScreen
import com.copay.app.ui.screen.auth.*
import com.copay.app.viewmodel.SplashViewModel
import com.copay.app.ui.screen.auth.RegisterStepOneScreen
import com.copay.app.ui.screen.auth.RegisterStepTwoScreen

@Composable
fun CopayNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoutes.SplashScreen.route
) {
    val splashViewModel: SplashViewModel = hiltViewModel()
    val isDataLoaded = splashViewModel.isDataLoaded.collectAsState()

    LaunchedEffect(isDataLoaded.value) {
        if (isDataLoaded.value) {
            navController.navigate(NavRoutes.AuthScreen.route) {
                popUpTo(NavRoutes.SplashScreen.route) { inclusive = true }
            }
        }
    }

    // Set up the navigation graph.
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        // SplashScreen.
        composable(NavRoutes.SplashScreen.route) {
            SplashScreen(automaticRedirection = { navController.navigate(NavRoutes.AuthScreen.route) })
        }

        // AuthScreen.
        composable(NavRoutes.AuthScreen.route) {
            AuthScreen(onSignUpClick = { navController.navigate(NavRoutes.RegisterStepOneScreen.route) },
                onLogInClick = { navController.navigate(NavRoutes.LoginScreen.route) })
        }

        // RegisterStepOneScreen.
        composable(NavRoutes.RegisterStepOneScreen.route) {
            RegisterStepOneScreen(
                navController,
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.RegisterStepTwoScreen.route) {
                    }
                }
            )
        }

        // RegisterStepTwoScreen.
        composable(NavRoutes.RegisterStepTwoScreen.route) {
            RegisterStepTwoScreen(
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.HubScreen.route)
                },

                )
        }

        // LoginScreen.
        composable(NavRoutes.LoginScreen.route) {

            LoginScreen(
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
            ForgotPasswordScreen(navController = navController)
        }

        // HubScreen.
        composable(NavRoutes.HubScreen.route) {
            HubScreen(
                onLogoutSuccess = {
                    navController.navigate(NavRoutes.AuthScreen.route)
                }
            )
        }
    }
}