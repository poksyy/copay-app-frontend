package com.copay.app.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.Icons

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {

        // Home item.
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = false,
            onClick = {
                navController.navigate("home")
            }
        )

        // Plannings item.
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Event, contentDescription = null) },
            label = { Text("Plannings") },
            selected = false,
            onClick = {
                navController.navigate("plannings")
            }
        )

        // Friends item.
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.People, contentDescription = null) },
            label = { Text("Friends") },
            selected = false,
            onClick = {
                navController.navigate("friends")
            }
        )

        // Profile item.
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = null) },
            label = { Text("Profile") },
            selected = false,
            onClick = {
                navController.navigate("profile")
            }
        )
    }
}
