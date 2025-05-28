package com.copay.app.ui.screen.group.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.copay.app.R
import com.copay.app.dto.paymentconfirmation.request.ConfirmPaymentRequestDTO
import com.copay.app.dto.paymentconfirmation.response.ListUnconfirmedPaymentConfirmationResponseDTO
import com.copay.app.dto.paymentconfirmation.response.PaymentResponseDTO
import com.copay.app.utils.ExpenseUtils
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.GroupMember
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.components.button.manageDebtsButton
import com.copay.app.ui.components.dialog.requestPaymentDialog
import com.copay.app.ui.components.pillTabRow
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.ui.components.listitem.memberItem
import com.copay.app.ui.components.dialog.manageDebtsDialog
import com.copay.app.ui.components.listitem.paymentActivityItem
import com.copay.app.utils.parseConfirmationDate
import com.copay.app.utils.state.ExpenseState
import com.copay.app.utils.state.PaymentState
import com.copay.app.viewmodel.ExpenseViewModel
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.PaymentConfirmationViewModel
import kotlinx.coroutines.launch

@Composable
fun groupBalancesScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel(),
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    paymentConfirmationViewModel: PaymentConfirmationViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val group by groupViewModel.group.collectAsState()

    // Expenses state.
    val groupExpenseState by expenseViewModel.expenses.collectAsState()
    val userExpensesState by expenseViewModel.userExpenses.collectAsState()

    // Save the list of payments done (Confirmed) and in pending.
    val userExpenseIdsState = remember { mutableStateOf<List<PaymentResponseDTO>>(emptyList()) }
    // List of payments are sorted by date (newest first).
    val sortedPayments = userExpenseIdsState.value.sortedByDescending { parseConfirmationDate(it.confirmationDate) }

    // Payment state.
    val paymentState by paymentConfirmationViewModel.paymentState.collectAsState()

    // Get current user.
    val currentUserId = groupViewModel.getCurrentUserId()
    val isCreditor = groupExpenseState.any { it.creditorUserId == currentUserId }
    val isCreator = group?.isOwner == true

    // Dialog to manage debts.
    var showUnconfirmedDialog by remember { mutableStateOf(false) }
    var unconfirmedPayments by remember { mutableStateOf<List<ListUnconfirmedPaymentConfirmationResponseDTO>>(emptyList()) }

    // Member list of the group.
    val membersList = remember(group) {
        buildList {
            group?.registeredMembers?.forEach {
                add(GroupMember.RegisteredMember(it.username, it.phoneNumber))
            }
            group?.externalMembers?.forEach {
                add(GroupMember.ExternalMember(it.name))
            }
        }
    }

    // Load groups when entering the screen
    LaunchedEffect(Unit) {
        groupViewModel.getGroupsByUser(context)
        group?.groupId?.let {
            expenseViewModel.getExpensesByGroup(context, it)
            expenseViewModel.getAllUserExpensesByGroup(context, it)
            paymentConfirmationViewModel.getUserExpenseIds(context, it)
        }
    }

    val successSnackbarHostState = remember { SnackbarHostState() }
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Load the list of unconfirmed payments in the dialog.
    LaunchedEffect(paymentState) {
        when (val state = paymentState) {
            is PaymentState.Success.SinglePayment -> {
                coroutineScope.launch {
                    successSnackbarHostState.showSnackbar("Payment requested successfully")
                }
                paymentConfirmationViewModel.resetPaymentState()
            }

            is PaymentState.Success.UnconfirmedPayments -> {
                unconfirmedPayments = state.unconfirmedPayments
                showUnconfirmedDialog = true
            }

            is PaymentState.Success.ConfirmedPayments -> {
                userExpenseIdsState.value = state.confirmedPayments
            }

            is PaymentState.Error -> {
                coroutineScope.launch {
                    errorSnackbarHostState.showSnackbar(state.message)
                }
                paymentConfirmationViewModel.resetPaymentState()
            }

            else -> {}
        }
    }

    // Detects if banner image color is dark or not
    fun isBackgroundDark(imageUrl: String?): Boolean {
        return imageUrl == null || imageUrl.contains("dark", ignoreCase = true)
    }

    var showRequestDialog by remember { mutableStateOf(false) }
    var selectedMemberId by remember { mutableStateOf<Long?>(null) }

    val iconColor = if (isBackgroundDark(group?.imageUrl)) Color.White else Color.Black

    // Show the screen content
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            AsyncImage(
                model = group?.imageUrl ?: R.drawable.chinese_buffet,
                contentDescription = "Group background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, Color.Black.copy(alpha = 3f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Text(
                text = group?.name ?: "Group",
                color = Color.White,
                style = CopayTypography.title,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            )

            backButtonTop(
                onBackClick = {
                    navigationViewModel.navigateBack()
                    groupViewModel.resetGroupSession()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart),
                iconColor = iconColor
            )
        }

        // Screen content
        if (group != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 170.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = group?.description ?: "Description",
                        style = CopayTypography.body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Members: ${(group?.registeredMembers?.size ?: 0) + (group?.externalMembers?.size ?: 0)}",
                        style = CopayTypography.body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Total ${group!!.estimatedPrice} ${group?.currency}",
                        style = CopayTypography.subtitle,
                        fontWeight = FontWeight.Bold,
                        color = CopayColors.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val buttonModifier = Modifier.weight(1f)

                        if (isCreator) {
                            manageDebtsButton(
                                onClick = {
                                    group?.groupId?.let {
                                        showUnconfirmedDialog = true
                                        paymentConfirmationViewModel.getUnconfirmedPaymentConfirmations(context, it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditMembers) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = buttonModifier,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(text = "Manage members", maxLines = 1, softWrap = false)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Tabs
                    var selectedTabIndex by remember { mutableStateOf(0) }
                    val tabs = listOf("Members", "Activity")

                    pillTabRow(
                        tabs = tabs,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        when (selectedTabIndex) {
                            0 -> {
                                val registered = group?.registeredMembers.orEmpty()
                                    .sortedByDescending { member ->
                                        val isMemberCreditor =
                                            groupExpenseState.any { it.creditorUserId == member.registeredMemberId }
                                        val isCurrentUser = member.registeredMemberId == currentUserId
                                        (if (isMemberCreditor) 2 else if (isCurrentUser) 1 else 0)
                                    }

                                val external = group?.externalMembers.orEmpty()
                                    .sortedByDescending { member -> groupExpenseState.any { it.creditorUserId == member.externalMembersId } }

                                items(registered) { member ->
                                    val expense = ExpenseUtils.calculateMemberExpense(member, groupExpenseState)
                                    memberItem(
                                        member = member,
                                        expense = expense,
                                        currency = group?.currency,
                                        currentUserId = currentUserId,
                                        onPayClick = {
                                            selectedMemberId = member.registeredMemberId
                                            showRequestDialog = true
                                        }
                                    )
                                }

                                items(external) { member ->
                                    val expense = ExpenseUtils.calculateMemberExpense(member, groupExpenseState)
                                    memberItem(
                                        member = member,
                                        expense = expense,
                                        currency = group?.currency,
                                        currentUserId = currentUserId
                                    )
                                }
                            }

                            1 -> {
                                val userExpenseIds = userExpenseIdsState.value
                                if (userExpenseIds.isEmpty()) {
                                    item {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "No activity found.",
                                                style = CopayTypography.body,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                } else {
                                    items(sortedPayments) { paymentActivity ->
                                        paymentActivityItem(payment = paymentActivity)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        greenSnackbarHost(
            hostState = successSnackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        redSnackbarHost(
            hostState = errorSnackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Dialog to show the list of unconfirmed payments.
    if (showUnconfirmedDialog) {
        manageDebtsDialog(
            onDismiss = { showUnconfirmedDialog = false },
            groupMembers = membersList,
            paymentConfirmationViewModel = paymentConfirmationViewModel,
            groupViewModel = groupViewModel,
            paymentState = paymentState,
            userExpenses = userExpensesState,
            context = context
        )
    }

    // Dialog to send a request to pay your debt.
    val memberId = selectedMemberId
    val groupId = group?.groupId

    var amount by remember { mutableStateOf("") }

    if (showRequestDialog && memberId != null && groupId != null) {
        val userExpenseId = userExpensesState.find { it.debtorUserId == memberId }?.userExpenseId

        if (userExpenseId != null) {
            requestPaymentDialog(
                groupId = groupId,
                userId = memberId,
                amount = amount,
                onAmountChange = { amount = it },
                onDismiss = {
                    showRequestDialog = false
                    selectedMemberId = null
                    amount = ""
                },
                onRequestSent = {
                    val amountFloat = amount.toFloatOrNull() ?: 0f
                    val request = ConfirmPaymentRequestDTO(
                        userExpenseId = userExpenseId,
                        confirmationAmount = amountFloat
                    )
                    paymentConfirmationViewModel.requestPayment(context, request)
                    showRequestDialog = false
                    selectedMemberId = null
                    amount = ""
                }
            )
        }
    }
}

// Function to format a Double to a fixed number of decimal places.
fun Double.format(digits: Int) = "%.${digits}f".format(this)
