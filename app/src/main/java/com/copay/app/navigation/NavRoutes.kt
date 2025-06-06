package com.copay.app.navigation

sealed class NavRoutes(val route: String) {

    data object AuthScreen : NavRoutes("api/auth")
    
    data object RegisterStepOneScreen : NavRoutes("api/register/step-one")
    data object RegisterStepTwoScreen : NavRoutes("api/register/step-two")

    data object LoginScreen : NavRoutes("api/login")
    data object ForgotPasswordScreen : NavRoutes("api/forgot-password")

    // Main SpaScreen
    data object HubScreen : NavRoutes("api/hub")

}
