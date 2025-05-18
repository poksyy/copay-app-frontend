package com.copay.app.navigation

sealed class SpaScreens(val route: String) {

    // Main SpaScreens inside HubScreen.
    data object Home : SpaScreens("home")
    data object Plannings : SpaScreens("plannings")
    data object Friends : SpaScreens("friends")
    data object Profile : SpaScreens("profile")

    // HomeScreen subpages inside the SPA.
    data object CreateGroup : SpaScreens("create_group")
    data object BalancesGroup : SpaScreens("balances_group")

    // Group editing screens
    sealed class GroupSubscreen(route: String) : SpaScreens(route) {
        data object EditGroup : GroupSubscreen("group/edit")
        data object EditName : GroupSubscreen("group/edit/name")
        data object EditDescription : GroupSubscreen("group/edit/description")
        data object EditPrice : GroupSubscreen("group/edit/price")
        data object EditMembers : GroupSubscreen("group/edit/members")
    }

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