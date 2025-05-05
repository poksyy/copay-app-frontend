package com.copay.app.ui.screen.group.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.copay.app.R
import com.copay.app.dto.expense.response.GetExpenseResponseDTO
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.BackButtonTop
import com.copay.app.ui.components.dialog.ConfirmationDialog
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.viewmodel.ExpenseViewModel
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

@Composable
fun GroupBalancesScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel(),
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Group state.
    val groupState by groupViewModel.groupState.collectAsState()

    // Values from Group Session.
    val group by groupViewModel.group.collectAsState()

    // Expense state,
    val expensesState by expenseViewModel.expenseState.collectAsState()
    val expenses by expenseViewModel.expenses.collectAsState()

    // Dialog states
    var showLeaveDialog by remember { mutableStateOf(false) }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var newMemberEmail by remember { mutableStateOf("") }

    // Snackbar state
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }


    // Load groups when entering the screen
    LaunchedEffect(Unit) {
        groupViewModel.getGroupsByUser(context)
        group?.groupId?.let { expenseViewModel.getExpensesByGroup(context, it) }
    }

    // Monitor group state changes
    LaunchedEffect(groupState) {
        when (groupState) {
            is GroupState.Success.GroupDeleted -> {
                snackbarMessage = "Group deleted successfully"
                showSnackbar = true
                navigationViewModel.navigateTo(SpaScreens.Home)
                groupViewModel.resetGroupState()
            }

            is GroupState.Success.GroupMemberLeft -> {
                snackbarMessage = "You left the group successfully"
                showSnackbar = true
                navigationViewModel.navigateTo(SpaScreens.Home)
                groupViewModel.resetGroupState()
            }

            is GroupState.Success.GroupMembersUpdated -> {
                snackbarMessage = "Members updated successfully"
                showSnackbar = true
                groupViewModel.getGroupsByUser(context)
                groupViewModel.resetGroupState()
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

    // Show the screen content
    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            AsyncImage(
                model = group?.imageUrl ?: R.drawable.copay_banner_white,
                contentDescription = "Group background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // TODO Move the style for group's banner in another place
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, Color.Black.copy(alpha = 3f)
                            ), startY = 0f, endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Back button
            BackButtonTop(
                onBackClick = {
                    navigationViewModel.navigateBack()
                    groupViewModel.resetGroupSession()
                }, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            )

            // TODO Add pencil icon to replace the edit text.
            // Edit button.
            if (group?.isOwner == true) {
                TextButton(
                    onClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        "Edit",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                TextButton(
                    onClick = { showLeaveDialog = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        "Leave",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Screen content
        when {
            groupState is GroupState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            group != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, top = 150.dp)
                ) {
                    // Group Header
                    item {
                        Text(
                            text = group?.name ?: "Group",
                            color = CopayColors.primary,
                            style = CopayTypography.title
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = group?.description ?: "Description",
                            style = CopayTypography.body,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Price information
//                            Surface(
//                                color = CopayColors.primary.copy(alpha = 0.1f),
//                                shape = RoundedCornerShape(8.dp)
//                            ) {
//                                Text(
//                                    text = "Total spent ${group?.estimatedPrice} ${group?.currency}",
//                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                                    fontWeight = FontWeight.Bold,
//                                    color = CopayColors.primary
//                                )
//                            }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Members ${(group?.registeredMembers?.size ?: 0) + (group?.externalMembers?.size ?: 0)} members",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Members Section Header
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Members ${(group?.registeredMembers?.size ?: 0) + (group?.externalMembers?.size ?: 0)} members",
                                style = CopayTypography.subtitle
                            )

                            // TODO move this small button into component.
                            Button(
                                onClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditMembers) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Manage members",
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    // Members List
                    items(
                        (group?.registeredMembers ?: emptyList()) + (group?.externalMembers
                            ?: emptyList())
                    ) { member ->
                        // Calculate expense for each member.
                        val memberExpense = calculateMemberExpense(member, expenses)
                        MemberItem(
                            member = member,
                            expense = memberExpense,
                            currency = group?.currency
                        )
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text("Group not found or error loading group details")
                }
            }
        }

        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        // Dialog for confirming leaving the group
        if (showLeaveDialog) {
            ConfirmationDialog(onDismiss = { showLeaveDialog = false }, onConfirm = {
                coroutineScope.launch {
                    groupViewModel.leaveGroup(context, group?.groupId ?: 0)
                }
                showLeaveDialog = false
            }, title = "Leave Group", text = "Are you sure you want to leave this group?")
        }
    }
}

// TODO move this into components or utils but somewhere else.
@Composable
private fun MemberItem(member: Any, expense: Double, currency: String?) {
    val memberName: String
    val memberPhoneNumber: String

    // Check if it is a registered or external member.
    when (member) {
        is RegisteredMemberDTO -> {
            memberName = member.username
            memberPhoneNumber = member.phoneNumber
        }

        is ExternalMemberDTO -> {
            memberName = member.name
            memberPhoneNumber = "External Member"
        }

        else -> {
            memberName = "Unknown"
            memberPhoneNumber = "Unknown"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder.
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                color = CopayColors.primary.copy(alpha = 0.2f)
            ) {
                Text(
                    text = memberName.firstOrNull().toString(),
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    style = CopayTypography.title,
                    color = CopayColors.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = memberName, fontWeight = FontWeight.Medium
                )

                Text(
                    text = memberPhoneNumber,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${expense.format(2)} $currency",
                fontWeight = FontWeight.Bold,
                color = CopayColors.primary
            )
        }
    }
}

// TODO: Move this into components or somewhere else.
private fun calculateMemberExpense(member: Any, expenses: List<GetExpenseResponseDTO>): Double {
    return expenses.flatMap { expense ->
        when (member) {
            is RegisteredMemberDTO -> {
                expense.registeredMembers.filter { it.debtorUserId == member.registeredMemberId }
                    .map { it.amount }
            }
            is ExternalMemberDTO -> {
                expense.externalMembers.filter { it.debtorExternalMemberId == member.externalMembersId }
                    .map { it.amount }
            }
            else -> emptyList()
        }
    }.sum()
}

// TODO: We could move this into utils.
fun Double.format(digits: Int) = "%.${digits}f".format(this)