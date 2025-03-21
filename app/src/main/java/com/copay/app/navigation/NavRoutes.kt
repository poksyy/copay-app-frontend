package com.copay.app.navigation

sealed class NavRoutes(val route: String) {

    data object AuthScreen : NavRoutes("api/auth")
    data object RegisterScreen : NavRoutes("api/register")
    data object LoginScreen : NavRoutes("api/login")
    data object SplashScreen : NavRoutes("api/splash")
}
