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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.R
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.BackButtonTop
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.UserViewModel


// TODO: Move dialogs into components
@Composable
fun EditGroupMembersScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentUser by userViewModel.user.collectAsState()
    val groupState = groupViewModel.group.collectAsState()

    val group = groupState.value
    val groupId = group?.groupId ?: return

    val registeredMembers by remember(group) {
        derivedStateOf { group.registeredMembers ?: emptyList() }
    }
    val externalMembers by remember(group) {
        derivedStateOf { group.externalMembers ?: emptyList() }
    }

    // States dialog
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var registeredMemberToRemove by remember { mutableStateOf<RegisteredMemberDTO?>(null) }
    var externalMemberToRemove by remember { mutableStateOf<ExternalMemberDTO?>(null) }
    var showLeaveDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Back button
        BackButtonTop(
            onBackClick = { navigationViewModel.navigateBack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        // Add member button
        IconButton(
            onClick = { showAddMemberDialog = true },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
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
                    isCurrentUser = member.registeredMemberId == currentUser?.userId,
                    onLeave = { showLeaveDialog = true },
                    onRemove = {
                        registeredMemberToRemove = member
                    }
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
                    onRemove = {
                        externalMemberToRemove = member
                    }
                )
            }
        }
    }

    // Leave group confirmation dialog
    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = { Text("Leave Group") },
            text = { Text("Are you sure you want to leave this group?") },
            confirmButton = {
                TextButton(onClick = {
                    showLeaveDialog = false
                    groupViewModel.leaveGroup(context, groupId)
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup)
                }) {
                    Text("Leave")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Remove member confirmation dialog
    registeredMemberToRemove?.let { member ->
        AlertDialog(
            onDismissRequest = { registeredMemberToRemove = null },
            title = { Text("Remove Member") },
            text = { Text("Are you sure you want to remove ${member.username}?") },
            confirmButton = {
                TextButton(onClick = {
                    registeredMemberToRemove = null
                    val updated = registeredMembers.filter { it.registeredMemberId != member.registeredMemberId }
                    groupViewModel.updateGroupRegisteredMembers(
                        context, groupId, updated.map { it.phoneNumber }
                    )
                }) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { registeredMemberToRemove = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add member dialog
    if (showAddMemberDialog) {
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onAdd = { phoneNumber ->
                val updatedPhoneNumbers = registeredMembers.map { it.phoneNumber }.toMutableList()
                updatedPhoneNumbers.add(phoneNumber)

                groupViewModel.updateGroupRegisteredMembers(
                    context, groupId, updatedPhoneNumbers
                )
                showAddMemberDialog = false
            }
        )
    }

    externalMemberToRemove?.let { member ->
        AlertDialog(
            onDismissRequest = { externalMemberToRemove = null },
            title = { Text("Remove Member") },
            text = { Text("Are you sure you want to remove ${member.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    externalMemberToRemove = null
                    val updatedMembers = externalMembers.filter { it.name != member.name }
                    groupViewModel.updateGroupExternalMembers(
                        context, groupId, updatedMembers.map { it.name }
                    )
                }) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { externalMemberToRemove = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RegisteredMemberItem(
    member: RegisteredMemberDTO,
    isCurrentUser: Boolean,
    onLeave: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = member.username,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )

            IconButton(onClick = {
                if (isCurrentUser) onLeave() else onRemove()
            }) {
                Icon(
                    painter = painterResource(
                        id = if (isCurrentUser) R.drawable.ic_leave else R.drawable.ic_delete
                    ),
                    contentDescription = if (isCurrentUser) "Leave Group" else "Remove Member"
                )
            }
        }
    }
}

@Composable
fun ExternalMemberItem(
    member: ExternalMemberDTO,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = member.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )

            IconButton(onClick = onRemove) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Remove External Member"
                )
            }
        }
    }
}

// Add member dialog
@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Member") },
        text = {
            Column {
                Text("Enter phone number of the member to add:")
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAdd(phoneNumber)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
