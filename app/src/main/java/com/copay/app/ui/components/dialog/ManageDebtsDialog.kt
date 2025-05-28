package com.copay.app.ui.components.dialog

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.copay.app.dto.expense.response.GetExpenseResponseDTO
import com.copay.app.dto.expense.response.UserExpenseDTO
import com.copay.app.dto.paymentconfirmation.request.ConfirmPaymentRequestDTO
import com.copay.app.ui.components.GroupMember
import com.copay.app.ui.components.input.dropdownField
import com.copay.app.ui.components.input.priceInputField
import com.copay.app.ui.components.pillTabRow
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.PaymentState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.PaymentConfirmationViewModel
import kotlinx.coroutines.launch

@Composable
fun manageDebtsDialog(
    onDismiss: () -> Unit,
    groupMembers: List<GroupMember>,
    paymentConfirmationViewModel: PaymentConfirmationViewModel,
    groupViewModel: GroupViewModel,
    paymentState: PaymentState,
    userExpenses: List<UserExpenseDTO>,
    context: Context,
    groupExpenses: List<GetExpenseResponseDTO> = emptyList()
) {
    var activeTab by remember { mutableStateOf(0) }
    var manualAmount by remember { mutableStateOf("") }

    // Convert manualAmount to float
    val amountAsFloat by remember {
        derivedStateOf {
            manualAmount.toFloatOrNull() ?: 0f
        }
    }

    // List of unconfirmedPayments
    val unconfirmedPayments by paymentConfirmationViewModel.unconfirmedPayments.collectAsState()

    // Values from Group Session
    val group by groupViewModel.group.collectAsState()

    // Handle snackbar to show the message
    val successSnackbarHostState = remember { SnackbarHostState() }
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Identify the creditor based on expenses
    val creditor = remember(groupExpenses, group) {
        // Find the creditor ID from expenses
        val creditorUserId = groupExpenses.firstOrNull()?.creditorUserId

        // Search for the creditor in registered members
        val registeredCreditor = group?.registeredMembers?.find {
            it.registeredMemberId == creditorUserId
        }?.let { registered ->
            GroupMember.RegisteredMember(
                name = registered.username,
                phoneNumber = registered.phoneNumber
            )
        }

        // If not found in registered, search in external members
        val externalCreditor = if (registeredCreditor == null) {
            group?.externalMembers?.find {
                it.externalMembersId == creditorUserId
            }?.let { external ->
                GroupMember.ExternalMember(name = external.name)
            }
        } else null

        // Return the found creditor or use the owner as fallback
        registeredCreditor ?: externalCreditor ?: run {
            group?.registeredMembers?.find {
                it.registeredMemberId == group!!.ownerId
            }?.let { owner ->
                GroupMember.RegisteredMember(
                    name = owner.username,
                    phoneNumber = owner.phoneNumber
                )
            }
        }
    }

    // Filter members excluding the creditor
    val filteredMembers = remember(groupMembers, creditor) {
        groupMembers.filter { member ->
            when {
                creditor == null -> true
                creditor is GroupMember.RegisteredMember && member is GroupMember.RegisteredMember -> {
                    member.phoneNumber != creditor.phoneNumber
                }
                creditor is GroupMember.ExternalMember && member is GroupMember.ExternalMember -> {
                    member.name != creditor.name
                }
                else -> member::class != creditor::class
            }
        }
    }

    // Values for the dropdowns when owner does a payment manually
    var payer by remember { mutableStateOf(filteredMembers.firstOrNull()) }

    // Find the userExpenseId using the debtorId (used to confirm the payment)
    val userExpenseId = when (payer) {
        is GroupMember.RegisteredMember -> {
            val id = group?.registeredMembers
                ?.find { it.phoneNumber == (payer as GroupMember.RegisteredMember).phoneNumber }
                ?.registeredMemberId
            userExpenses.find { it.debtorUserId == id }?.userExpenseId
        }

        is GroupMember.ExternalMember -> {
            val id = group?.externalMembers
                ?.find { it.name == (payer as GroupMember.ExternalMember).name }
                ?.externalMembersId
            userExpenses.find { it.debtorExternalId == id }?.userExpenseId
        }

        else -> null
    }

    // Handle the payment state when it changes
    LaunchedEffect(paymentState) {
        when (paymentState) {
            is PaymentState.Success.SinglePayment -> {
                coroutineScope.launch {
                    successSnackbarHostState.showSnackbar("Payment confirmed successfully")
                }
            }

            is PaymentState.Success.Message -> {
                coroutineScope.launch {
                    successSnackbarHostState.showSnackbar("Payment request rejected")
                }
            }

            is PaymentState.Error -> {
                coroutineScope.launch {
                    errorSnackbarHostState.showSnackbar(paymentState.message)
                }
            }

            else -> {}
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            tonalElevation = 8.dp,
            shape = MaterialTheme.shapes.medium,
            color = CopayColors.onPrimary
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .width(320.dp)
            ) {
                Text("Manage Payments", style = CopayTypography.title)

                Spacer(modifier = Modifier.height(8.dp))

                pillTabRow(
                    tabs = listOf("Requests", "Manual"),
                    selectedTabIndex = activeTab,
                    onTabSelected = { activeTab = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                when (activeTab) {
                    0 -> {
                        // Tab "Requests"
                        Text(
                            text = "Confirm or reject payment requests made by members.",
                            style = CopayTypography.footer,
                            color = CopayColors.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (unconfirmedPayments.isEmpty()) {
                            Text(
                                "No pending payment confirmations.",
                                style = CopayTypography.footer,
                                color = CopayColors.onSecondary
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .heightIn(max = 200.dp)
                                    .fillMaxWidth()
                            ) {
                                items(unconfirmedPayments) { payment ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(Modifier.weight(1f)) {
                                            Text(payment.username, style = CopayTypography.body)
                                            Text(
                                                "${payment.confirmationAmount}€",
                                                style = CopayTypography.footer
                                            )
                                        }
                                        Row {
                                            IconButton(
                                                onClick = {
                                                    group?.groupId?.let {
                                                        paymentConfirmationViewModel.markAsConfirmed(
                                                            context,
                                                            payment.paymentConfirmationId,
                                                            it
                                                        )
                                                    }
                                                }) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Accept",
                                                    tint = CopayColors.primary
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    group?.groupId?.let {
                                                        paymentConfirmationViewModel.deletePaymentConfirmation(
                                                            context,
                                                            payment.paymentConfirmationId,
                                                            it
                                                        )
                                                    }
                                                }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Reject",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text("Close")
                            }
                        }
                    }

                    1 -> {
                        // Manual tab content
                        Text(
                            text = "Manually record a payment for a member.",
                            style = CopayTypography.footer,
                            color = CopayColors.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        dropdownField(
                            label = "Who paid?",
                            items = filteredMembers.map { it.displayText() },
                            selectedItem = payer?.displayText() ?: "",
                            onItemSelected = { selected ->
                                payer = filteredMembers.find { it.displayText() == selected }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Receiver: ",
                                style = CopayTypography.footer
                            )
                            Text(
                                text = creditor?.displayText() ?: "Unknown",
                                style = CopayTypography.body,
                                color = CopayColors.onSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        priceInputField(
                            value = manualAmount,
                            onValueChange = { manualAmount = it },
                            label = "",
                            selectedCurrency = "EUR",
                            onCurrencyChange = {},
                            currencyList = listOf("EUR"),
                            isError = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text("Close")
                            }

                            Button(
                                onClick = {
                                    userExpenseId?.let {
                                        paymentConfirmationViewModel.confirmPayment(
                                            context,
                                            ConfirmPaymentRequestDTO(
                                                userExpenseId = it,
                                                confirmationAmount = amountAsFloat
                                            )
                                        )
                                        onDismiss()
                                    }
                                }
                            ) {
                                Text("Add Payment")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Snackbars
                redSnackbarHost(
                    hostState = errorSnackbarHostState,
                    modifier = Modifier.align(Alignment.End)
                )

                greenSnackbarHost(
                    hostState = successSnackbarHostState,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}