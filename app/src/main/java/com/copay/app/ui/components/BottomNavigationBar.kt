package com.copay.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.Color

@Composable
fun bottomNavigationBar(
    // Current route to indicate the selected item.
    currentRoute: String,
    // Callback to handle route selection.
    onRouteSelected: (String) -> Unit
) {
    Column {
        HorizontalDivider(color = Color.Black.copy(alpha = 0.2f), thickness = 0.5.dp)

        NavigationBar(
            containerColor = CopayColors.onPrimary
        ) {
            // List of navigation items with their routes and icons.
            val items = listOf(
                NavigationItem("home", Icons.Filled.Home),
                NavigationItem("plannings", Icons.Filled.Event),
                NavigationItem("friends", Icons.Filled.People),
                NavigationItem("profile", Icons.Filled.Settings)
            )
            // Loop through items and create NavigationBarItem for each.
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            // No text below icons = null.
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    // Check if the current route matches the item's route to mark it as selected.
                    selected = currentRoute == item.route,
                    // Trigger route change on item click.
                    onClick = { onRouteSelected(item.route) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CopayColors.primary,
                        unselectedIconColor = CopayColors.outline,
                        indicatorColor = CopayColors.surface
                    )
                )
            }
        }
    }
}
// Data class to hold route and corresponding icon.
private data class NavigationItem(
    val route: String,
    val icon: ImageVector,
)
