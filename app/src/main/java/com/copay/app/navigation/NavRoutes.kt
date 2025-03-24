package com.copay.app.navigation

sealed class NavRoutes(val route: String) {

    data object SplashScreen : NavRoutes("api/splash")
    data object AuthScreen : NavRoutes("api/auth")
    data object RegisterStepOneScreen : NavRoutes("api/register/step-one")
    data object RegisterStepTwoScreen : NavRoutes("api/register/step-two")
    data object LoginScreen : NavRoutes("api/login")
}
