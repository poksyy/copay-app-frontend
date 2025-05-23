package com.copay.app.ui.screen.group.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.R
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.model.Group
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.dialog.*
import com.copay.app.ui.components.listitem.externalMemberItem
import com.copay.app.ui.components.listitem.registeredMemberItem
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.topNavBar
import com.copay.app.utils.state.GroupState
import com.copay.app.utils.state.ProfileState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.UserViewModel

@Composable
fun editGroupMembersScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentUser by userViewModel.user.collectAsState()
    val group by groupViewModel.group.collectAsState()
    val groupId = group?.groupId ?: return

    // Handle states.
    val userState by userViewModel.userState.collectAsState()
    val groupState by groupViewModel.groupState.collectAsState()

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
    var screenSnackbarMessage by remember { mutableStateOf("") }
    val screenSnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        groupViewModel.getGroupByGroupId(context, groupId)
    }

    // Handle the state when user updates the members.
    LaunchedEffect(groupState) {
        when (groupState) {
            is GroupState.Success.GroupUpdated -> {
                screenSnackbarMessage = (groupState as GroupState.Success.GroupUpdated).updateData.message
                showSnackbar = true
            }

            else -> {}
        }
    }

    // Show in the screen the snackbar message.
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            screenSnackbarHostState.showSnackbar(screenSnackbarMessage)
            showSnackbar = false
        }
    }

    editGroupMembersContent(
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
        screenSnackbarHostState = screenSnackbarHostState
    )

    // Add member dialog.
    if (showAddMemberDialog) {
        addMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onAddRegistered = { phoneNumber ->
                userViewModel.getUserByPhone(context, phoneNumber)
                groupViewModel.addRegisteredMember(context, groupId, registeredMembers, phoneNumber)
            },
            onAddExternal = { name ->
                groupViewModel.addExternalMember(context, groupId, externalMembers, name)
            },
            userState = userState,
            userViewModel = userViewModel
        )
    }

    // Delete group confirmation dialog.
    if (showDeleteDialog) {
        deleteGroupDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                groupViewModel.deleteGroup(context, groupId)
                navigationViewModel.navigateTo(SpaScreens.Home)
            }
        )
    }

    // Leave group confirmation dialog.
    if (showLeaveDialog) {
        leaveGroupDialog(
            onDismiss = { showLeaveDialog = false },
            onConfirm = {
                groupViewModel.leaveGroup(context, groupId)
                navigationViewModel.navigateTo(SpaScreens.Home)
            }
        )
    }

    // Remove registered member confirmation dialog
    removeRegisteredMemberDialog(
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
    removeExternalMemberDialog(
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
fun editGroupMembersContent(
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
    screenSnackbarHostState: SnackbarHostState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        topNavBar(
            title = "Edit group members",
            onBackClick = onBack,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        FloatingActionButton(
            onClick = onAddMemberClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
            ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_member),
                contentDescription = "Add Member"
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, start = 16.dp, end = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Registered Members",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(registeredMembers) { member ->
                registeredMemberItem(
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
                externalMemberItem(
                    member = member,
                    group = group,
                    onRemove = { onRemoveExternalMember(member) }
                )
            }
        }

        // Snackbar host is shown when user removes a member.
        greenSnackbarHost(
            hostState = screenSnackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}