package com.copay.app.ui.screen.group.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.R
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.model.Group
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.BackButtonTop
import com.copay.app.ui.components.dialog.AddMemberDialog
import com.copay.app.ui.components.dialog.DeleteGroupDialog
import com.copay.app.ui.components.dialog.LeaveGroupDialog
import com.copay.app.ui.components.dialog.RemoveExternalMemberDialog
import com.copay.app.ui.components.dialog.RemoveRegisteredMemberDialog
import com.copay.app.ui.components.listitem.ExternalMemberItem
import com.copay.app.ui.components.listitem.RegisteredMemberItem
import com.copay.app.utils.state.GroupState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.UserViewModel

@Composable
fun EditGroupMembersScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentUser by userViewModel.user.collectAsState()
    val group by groupViewModel.group.collectAsState()
    val groupState by groupViewModel.groupState.collectAsState()
    val groupId = group?.groupId ?: return

    val registeredMembers by remember(group) {
        derivedStateOf { group!!.registeredMembers ?: emptyList() }
    }
    val externalMembers by remember(group) {
        derivedStateOf { group!!.externalMembers ?: emptyList() }
    }

    // State variables to control the visibility of the leave & delete dialogs.
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var registeredMemberToRemove by remember { mutableStateOf<RegisteredMemberDTO?>(null) }
    var externalMemberToRemove by remember { mutableStateOf<ExternalMemberDTO?>(null) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Snackbar state
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    // Monitor group state changes
    LaunchedEffect(groupState) {
        when (groupState) {
            is GroupState.Success.GroupUpdated -> {
                snackbarMessage = (groupState as GroupState.Success.GroupUpdated).updateData.message
                showSnackbar = true
            }

            is GroupState.Error -> {
                snackbarMessage = (groupState as GroupState.Error).message
                showSnackbar = true
            }

            else -> {}
        }
    }

    // Show snackbar when needed
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(snackbarMessage)
            showSnackbar = false
        }
    }

    EditGroupMembersContent(
        group = group!!,
        registeredMembers = registeredMembers,
        externalMembers = externalMembers,
        currentUserId = currentUser?.userId,
        onBack = { navigationViewModel.navigateBack() },
        onAddMemberClick = { showAddMemberDialog = true },
        onDeleteGroup = { showDeleteDialog = true },
        onLeaveGroup = { showLeaveDialog = true },
        onRemoveRegisteredMember = { registeredMemberToRemove = it },
        onRemoveExternalMember = { externalMemberToRemove = it },
        snackbarHostState = snackbarHostState
    )

    // Add member dialog.
    if (showAddMemberDialog) {
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onAddRegistered = { phoneNumber ->
                groupViewModel.addRegisteredMember(context, groupId, registeredMembers, phoneNumber)
                showAddMemberDialog = false
            },
            onAddExternal = { name ->
                groupViewModel.addExternalMember(context, groupId, externalMembers, name)
                showAddMemberDialog = false
            }
        )
    }

    // Delete group confirmation dialog.
    if (showDeleteDialog) {
        DeleteGroupDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                groupViewModel.deleteGroup(context, groupId) {
                    groupViewModel.getGroupsByUser(context)
                }
                navigationViewModel.navigateTo(SpaScreens.Home)
            }
        )
    }

    // Leave group confirmation dialog.
    if (showLeaveDialog) {
        LeaveGroupDialog(
            onDismiss = { showLeaveDialog = false },
            onConfirm = {
                groupViewModel.leaveGroup(context, groupId) {
                    groupViewModel.getGroupsByUser(context)
                }
                navigationViewModel.navigateTo(SpaScreens.Home)
            }
        )
    }

    // Remove registered member confirmation dialog
    RemoveRegisteredMemberDialog(
        member = registeredMemberToRemove,
        onDismiss = { registeredMemberToRemove = null },
        onConfirm = { member ->

            // Obtain the list of registered members excluding the member to be eliminated
            val newRegisteredMember = registeredMembers
                .filter { it.registeredMemberId != member.registeredMemberId }
                .map { it.phoneNumber }

            groupViewModel.updateGroupRegisteredMembers(
                context, groupId, registeredMembers, newRegisteredMember
            )
        }
    )

    // Remove external member confirmation dialog
    RemoveExternalMemberDialog(
        member = externalMemberToRemove,
        onDismiss = { externalMemberToRemove = null },
        onConfirm = { member ->
            
            // Obtain the list of external members excluding the member to be eliminated
            val newExternalMembers = externalMembers
                .filter { it.externalMembersId != member.externalMembersId }
                .map { it.name }

            groupViewModel.updateGroupExternalMembers(
                context, groupId, externalMembers, newExternalMembers
            )
        }
    )
}

@Composable
fun EditGroupMembersContent(
    group: Group,
    registeredMembers: List<RegisteredMemberDTO>,
    externalMembers: List<ExternalMemberDTO>,
    currentUserId: Long?,
    onBack: () -> Unit,
    onAddMemberClick: () -> Unit,
    onDeleteGroup: () -> Unit,
    onLeaveGroup: () -> Unit,
    onRemoveRegisteredMember: (RegisteredMemberDTO) -> Unit,
    onRemoveExternalMember: (ExternalMemberDTO) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Back button
        BackButtonTop(
            onBackClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        // Add member button
        IconButton(
            onClick = onAddMemberClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_member),
                contentDescription = "Add Member"
            )
        }

        // Main content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Edit Members",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Text(
                    "Registered Members",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(registeredMembers) { member ->
                RegisteredMemberItem(
                    member = member,
                    group = group,
                    isCurrentUser = member.registeredMemberId == currentUserId,
                    onDelete = onDeleteGroup,
                    onLeave = onLeaveGroup,
                    onRemove = { onRemoveRegisteredMember(member) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "External Members",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(externalMembers) { member ->
                ExternalMemberItem(
                    member = member,
                    group = group,
                    onRemove = { onRemoveExternalMember(member) }
                )
            }
        }

        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}