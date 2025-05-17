package com.copay.app.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.model.Group
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.dialog.DeleteGroupDialog
import com.copay.app.ui.components.dialog.LeaveGroupDialog
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.UserViewModel
import com.copay.app.ui.components.listitem.GroupItem

@Composable
fun HomeScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    // Get the username value through the userViewModel.
    val user by userViewModel.user.collectAsState()
    Log.d("HomeScreen", "User in HomeScreen: $user")
    val username = user?.username ?: "Username"

    val context = LocalContext.current

    // Group state.
    val groupState by groupViewModel.groupState.collectAsState()
    Log.d("HomeScreen", "$groupState")

    // State variables to control the visibility of the leave & delete dialogs.
    var showLeaveDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // The group selected by the user.
    var groupSelected by remember { mutableStateOf<Group?>(null) }

    // Trigger group loading when the screen is first composed
    LaunchedEffect(Unit) {
        groupViewModel.getGroupsByUser(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (groupState) {
            is GroupState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is GroupState.Error -> {
                Text(
                    text = (groupState as GroupState.Error).message,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }

            is GroupState.Success.GroupsFetched -> {

                val groups = (groupState as GroupState.Success.GroupsFetched).groups
                    .sortedByDescending { it.createdAt }

                HomeContent(
                    onCreateClick = { navigationViewModel.navigateTo(SpaScreens.CreateGroup) },
                    onDetailClick = { group ->
                        groupViewModel.selectGroup(group)
                        navigationViewModel.navigateTo(SpaScreens.BalancesGroup)
                    },
                    onEditClick = { group ->
                        groupViewModel.selectGroup(group)
                        navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup)
                    },
                    onDeleteClick = { group ->
                        groupSelected = group
                        showDeleteDialog = true
                    },
                    onLeaveClick = { group ->
                        groupSelected = group
                        showLeaveDialog = true
                    },
                    username = username,
                    groups = groups
                )
            }

            else -> {
                HomeContent(
                    onCreateClick = { navigationViewModel.navigateTo(SpaScreens.CreateGroup) },
                    onDetailClick = { navigationViewModel.navigateTo(SpaScreens.BalancesGroup) },
                    onEditClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup) },
                    onDeleteClick = { group ->
                        groupSelected = group
                        showDeleteDialog = true
                    },
                    onLeaveClick = { group ->
                        groupSelected = group
                        showLeaveDialog = true
                    },
                    username = username,
                    groups = emptyList()
                )
            }
        }

        if (showDeleteDialog) {
            DeleteGroupDialog(
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    groupSelected?.groupId?.let { groupId ->
                        groupViewModel.deleteGroup(context, groupId) {
                            groupViewModel.getGroupsByUser(context)
                        }
                    }
                    showDeleteDialog = false
                }
            )
        }

        if (showLeaveDialog) {
            LeaveGroupDialog(
                onDismiss = { showLeaveDialog = false },
                onConfirm = {
                    groupSelected?.groupId?.let { groupId ->
                        groupViewModel.leaveGroup(context, groupId) {
                            groupViewModel.getGroupsByUser(context)
                        }
                    }
                    showLeaveDialog = false
                }
            )
        }
    }
}

@Composable
private fun HomeContent(
    // Receives the callbacks from HomeViewModel.
    onCreateClick: () -> Unit,
    onDetailClick: (Group) -> Unit,
    onEditClick: (Group) -> Unit,
    onDeleteClick: (Group) -> Unit,
    onLeaveClick: (Group) -> Unit,
    username: String,
    groups: List<Group> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            // Shows the username of the logged user.
            text = "Welcome $username!",
            color = CopayColors.primary,
            style = CopayTypography.title,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Dashboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "--- €",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "Total spent this month",
                    color = Color.Gray, fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (index == 0) Color.Black else Color.Gray)
                        )
                        if (index < 2) Spacer(modifier = Modifier.width(8.dp))
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
                text = "My groups", color = CopayColors.primary, style = CopayTypography.subtitle
            )
            Row {
                TextButton(onClick = onCreateClick) {
                    Text("Create")
                }
            }
        }

        // Display user's groups dynamically
        if (groups.isEmpty()) {
            Text("You do not have any groups.", modifier = Modifier.padding(vertical = 16.dp))
        } else {
            groups.forEach { group ->
                GroupItem(group = group,
                    onItemClick = { onDetailClick(group) },
                    onEditClick = { onEditClick(group) },
                    onDeleteClick = { onDeleteClick(group) },
                    onLeaveClick = { onLeaveClick(group) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}