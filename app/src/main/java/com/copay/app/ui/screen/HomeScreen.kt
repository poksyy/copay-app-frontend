package com.copay.app.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.copay.app.dto.response.LoginResponseDTO
import com.copay.app.navigation.SpaScreens
import com.copay.app.viewmodel.UserViewModel

/**
 * HomeScreen composable that handles the main view and sub-views.
 * @param onNavigateFromBottomBar Flag indicating if navigation came from bottom bar.
 * @param onNavigationComplete Callback to reset navigation state after handling.
 */
@Composable
fun HomeScreen(
    onNavigateFromBottomBar: Boolean = false,
    onNavigationComplete: () -> Unit = {},
    userViewModel: UserViewModel = viewModel()
) {
    // Get the username value trough the userViewModel.
    val userState = userViewModel.user.collectAsState()

    // Manages the current view state within HomeScreen.
    var currentView by remember { mutableStateOf<SpaScreens>(SpaScreens.Home) }

    // Reset to Home view when navigating from bottom bar and notify completion.
    LaunchedEffect(onNavigateFromBottomBar) {
        if (onNavigateFromBottomBar) {
            currentView = SpaScreens.Home
            onNavigationComplete()
        }
    }

    LaunchedEffect(userState.value) {
        Log.d("UserState", "User state: ${userState.value}")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentView) {
            // Displays the Home content screen.
            SpaScreens.Home -> HomeContent(
                // Navigation to JoinGroup.
                onJoinClick = { currentView = SpaScreens.JoinGroup },
                // Navigation to CreateGroup.
                onCreateClick = { currentView = SpaScreens.CreateGroup },
                // Sends the username of the logged user.
                user = userState.value
            )
            // Displays the JoinGroup screen and allows navigating back to Home.
            SpaScreens.JoinGroup -> JoinGroupScreen(
                onBackClick = { currentView = SpaScreens.Home }
            )
            // Displays the CreateGroup screen and allows navigating back to Home.
            SpaScreens.CreateGroup -> CreateGroupScreen(
                onBackClick = { currentView = SpaScreens.Home }
            )
            // Default case: if no screen matches, display Home content screen.
            else -> HomeContent(
                onJoinClick = { currentView = SpaScreens.JoinGroup },
                onCreateClick = { currentView = SpaScreens.CreateGroup },
                user = userState.value
            )
        }
    }
}

@Composable
private fun HomeContent(
    // Receives the callbacks from HomeViewModel.
    onJoinClick: () -> Unit,
    onCreateClick: () -> Unit,
    user: LoginResponseDTO?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            // Shows the username of the logged user.
            text = "Welcome ${user?.username ?: "User"}!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Dashboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "1.250€",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "Total spent this month",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (index == 1) Color.Black else Color.Gray)
                                .padding(end = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My groups",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                TextButton(onClick = onCreateClick) {
                    Text("Create")
                }
                TextButton(onClick = onJoinClick) {
                    Text("Join")
                }
            }
        }

        // Display the different groups with their respective information.
        GroupItem(
            imageUrl = "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad",
            title = "London trip",
            members = "6 members",
            amount = "16.250€"
        )

        Spacer(modifier = Modifier.height(8.dp))

        GroupItem(
            imageUrl = "https://images.unsplash.com/photo-1553621042-f6e147245754",
            title = "Sushi dinner",
            members = "69 members",
            amount = "1.250€"
        )
    }
}

@Composable
private fun GroupItem(
    imageUrl: String,
    title: String,
    members: String,
    amount: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = members,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Text(
                text = amount,
                fontWeight = FontWeight.Bold
            )
        }
    }
}