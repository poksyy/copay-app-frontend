package com.copay.app.navigation

sealed class SpaScreens(val route: String) {

    // Main SpaScreens inside HubScreen.
    data object Home : SpaScreens("home")
    data object Plannings : SpaScreens("plannings")
    data object Friends : SpaScreens("friends")
    data object Profile : SpaScreens("profile")

    // HomeScreen subpages inside the SPA.
    data object JoinGroup : SpaScreens("join_group")
    data object CreateGroup : SpaScreens("create_group")

    // ProfileScreen subpages inside the SPA.
    sealed class ProfileSubscreen(route: String) : SpaScreens(route) {
        // Profile Editing
        data object EditProfile : ProfileSubscreen("profile/edit")
        data object EditUsername : ProfileSubscreen("profile/edit/username")
        data object EditEmail : ProfileSubscreen("profile/edit/email")
        data object EditPhoneNumber : ProfileSubscreen("profile/edit/phone")

        // Security
        data object ChangePassword : ProfileSubscreen("profile/security/password")
    }
}