package com.copay.app.navigation

sealed class NavRoutes(val route: String) {

    data object SplashScreen : NavRoutes("api/splash")
    data object AuthScreen : NavRoutes("api/auth")
    
    data object RegisterStepOneScreen : NavRoutes("api/register/step-one")
    data object RegisterStepTwoScreen : NavRoutes("api/register/step-two")

    data object LoginScreen : NavRoutes("api/login")
    data object ForgotPasswordScreen : NavRoutes("api/forgot-password")

    data object HubScreen : NavRoutes("api/hub")
    data object HomeScreen : NavRoutes("api/home")
    data object PlanningScreen : NavRoutes("api/planning")
    data object FriendScreen : NavRoutes("api/friend")
    data object ProfileScreen : NavRoutes("api/profile")
}
