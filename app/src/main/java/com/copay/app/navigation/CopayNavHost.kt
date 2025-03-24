package com.copay.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.copay.app.ui.screen.auth.LoginScreen
import com.copay.app.ui.screen.auth.AuthScreen
import com.copay.app.ui.screen.HubScreen
import com.copay.app.ui.screen.SplashScreen
import com.copay.app.viewmodel.SplashViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.repository.UserRepository
import com.copay.app.config.RetrofitInstance
import com.copay.app.ui.screen.AuthScreen
import com.copay.app.ui.screen.auth.RegisterStepOneScreen
import com.copay.app.ui.screen.auth.RegisterStepTwoScreen

@Composable
fun CopayNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavRoutes.SplashScreen.route
) {
    val splashViewModel: SplashViewModel = viewModel()
    val isDataLoaded = splashViewModel.isDataLoaded.collectAsState()
    val userRepository = remember { UserRepository(RetrofitInstance.api) }

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
        // SplashScreen
        composable(NavRoutes.SplashScreen.route) {
            SplashScreen(automaticRedirection = { navController.navigate(NavRoutes.AuthScreen.route) })
        }

        // AuthScreen
        composable(NavRoutes.AuthScreen.route) {
            AuthScreen(onSignUpClick = { navController.navigate(NavRoutes.RegisterStepOneScreen.route) },
                onLogInClick = { navController.navigate(NavRoutes.LoginScreen.route) })
        }

        // RegisterStepOneScreen
        composable(NavRoutes.RegisterStepOneScreen.route) {
            RegisterStepOneScreen(navController, userRepository)
        }

        // RegisterStepTwoScreen
        composable(NavRoutes.RegisterStepTwoScreen.route) {
            RegisterStepTwoScreen(navController = navController,
                userRepository = userRepository,
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.HubScreen.route) {
                        popUpTo(NavRoutes.RegisterStepOneScreen.route) { inclusive = true }
                    }
                }
            )
        }

        // LoginScreen
        composable(NavRoutes.LoginScreen.route) {
            LoginScreen(
                navController = navController,
                userRepository = userRepository,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.HubScreen.route) {
                        popUpTo(NavRoutes.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

        // HubScreen
        composable(NavRoutes.HubScreen.route) {
            HubScreen(navController = navController)
        }
    }
}