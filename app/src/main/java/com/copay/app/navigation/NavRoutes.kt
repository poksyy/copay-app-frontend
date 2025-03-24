package com.copay.app.navigation

sealed class NavRoutes(val route: String) {

    data object SplashScreen : NavRoutes("api/splash")
    data object AuthScreen : NavRoutes("api/auth")
    data object RegisterScreen : NavRoutes("api/register")
    data object LoginScreen : NavRoutes("api/login")

    data object HubScreen : NavRoutes("api/hub")
    data object HomeScreen : NavRoutes("api/home")
    data object PlanningScreen : NavRoutes("api/planning")
    data object FriendScreen : NavRoutes("api/friend")
    data object ProfileScreen : NavRoutes("api/profile")
}
