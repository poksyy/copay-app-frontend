package com.copay.app.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.R
import com.copay.app.dto.notification.response.NotificationResponseDTO
import com.copay.app.model.Group
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.DashboardPager
import com.copay.app.ui.components.GradientBackground
import com.copay.app.ui.components.dialog.deleteGroupDialog
import com.copay.app.ui.components.dialog.leaveGroupDialog
import com.copay.app.ui.components.dialog.notificationDialog
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.UserViewModel
import com.copay.app.ui.components.listitem.groupItem
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@Composable
fun homeScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    groupViewModel: GroupViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    // Get the username value through the userViewModel.
    val user by userViewModel.user.collectAsState()
    Log.d("HomeScreen", "User in HomeScreen: $user")
    val username = user?.username ?: "Username"

    val context = LocalContext.current

    // Variable from Group VW to detect the state if there are new groups.
    val hasNewGroups by groupViewModel.hasNewGroups.collectAsState()

    // Snackbar.
    val snackbarHostState = remember { SnackbarHostState() }

    // Group state.
    val groupState by groupViewModel.groupState.collectAsState()
    Log.d("HomeScreen", "$groupState")

    // State variables to control the visibility of the dialogs.
    var showNotificationDialog by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // The group selected by the user.
    var groupSelected by remember { mutableStateOf<Group?>(null) }

    // List of notifications.
    val allNotifications by notificationViewModel.allNotifications.collectAsState()

    // List of unread notifications.
    val unreadNotifications by notificationViewModel.unreadNotifications.collectAsState()

    // Trigger group loading when the screen is first composed
    LaunchedEffect(Unit) {
        // Fetch all the groups that the user is part of.
        groupViewModel.getGroupsByUser(context)

        // Start background auto-refresh groups.
        groupViewModel.autoRefreshGroups(context)

        // Fetch notifications.
        notificationViewModel.getAllNotifications(context)
        notificationViewModel.getUnreadNotifications(context)

        // Start background auto-refresh for notifications.
        notificationViewModel.autoRefreshNotifications(context)
    }

    // Trigger when
    LaunchedEffect(hasNewGroups) {
        if (hasNewGroups) {
            val result = snackbarHostState.showSnackbar(
                message = "New groups available.",
                actionLabel = "Update"
            )
            if (result == SnackbarResult.ActionPerformed) {
                groupViewModel.getGroupsByUser(context, forceRefresh = true)
            }
        }
    }

    GradientBackground {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
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

                    homeContent(
                        onNotificationClick = { showNotificationDialog = true },
                        onRefreshClick = {
                            groupViewModel.getGroupsByUser(
                                context,
                                forceRefresh = true
                            )
                        },
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
                    homeContent(
                        onNotificationClick = { showNotificationDialog = true },
                        onRefreshClick = {
                            groupViewModel.getGroupsByUser(
                                context,
                                forceRefresh = true
                            )
                        },
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

            greenSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )

            // Dialog for notifications.
            if (showNotificationDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                ) {
                    // Block any interaction from the user if the dialog is open.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                            .clickable(enabled = true, onClick = {})
                    )

                    // Notification's dialog.
                    notificationDialog(
                        allNotifications = allNotifications,
                        unreadNotifications = unreadNotifications,
                        notificationViewModel = notificationViewModel,
                        context = context,
                        onDismiss = { showNotificationDialog = false }
                    )
                }
            }
            // Dialog for deleting a group as an owner.
            if (showDeleteDialog) {
                deleteGroupDialog(
                    onDismiss = { showDeleteDialog = false },
                    onConfirm = {
                        groupSelected?.groupId?.let { groupId ->
                            groupViewModel.deleteGroup(context, groupId)
                        }
                        showDeleteDialog = false
                    }
                )
            }

            // Dialog for leaving a group.
            if (showLeaveDialog) {
                leaveGroupDialog(
                    onDismiss = { showLeaveDialog = false },
                    onConfirm = {
                        groupSelected?.groupId?.let { groupId ->
                            groupViewModel.leaveGroup(context, groupId)
                        }
                        showLeaveDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun homeContent(
    // Receives the callbacks from HomeViewModel.
    onNotificationClick: () -> Unit,
    onRefreshClick: () -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hi, $username!",
                color = CopayColors.primary,
                style = CopayTypography.title,
            )

            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notifications),
                    contentDescription = "Notifications",
                    tint = CopayColors.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Dashboard with 3 sliders.
        DashboardPager(
            totalSpent = "125.50 €",
            pendingPayments = 3,
            groupsJoined = 6
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "My groups",
                    color = CopayColors.primary,
                    style = CopayTypography.subtitle
                )

                IconButton(onClick = onRefreshClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_refresh),
                        contentDescription = "Refresh",
                        tint = CopayColors.primary
                    )
                }
            }

            TextButton(onClick = onCreateClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add",
                    tint = CopayColors.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Create",
                    style = CopayTypography.button,
                    color = CopayColors.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Display user's groups dynamically
        if (groups.isEmpty()) {
            Text("You do not have any groups.", modifier = Modifier.padding(vertical = 16.dp))
        } else {
            groups.forEach { group ->
                groupItem(group = group,
                    onItemClick = { onDetailClick(group) },
                    onEditClick = { onEditClick(group) },
                    onDeleteClick = { onDeleteClick(group) },
                    onLeaveClick = { onLeaveClick(group) })
            }
        }
    }
}