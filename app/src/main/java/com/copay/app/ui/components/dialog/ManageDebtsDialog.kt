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
import com.copay.app.dto.expense.response.UserExpenseDTO
import com.copay.app.dto.paymentconfirmation.request.ConfirmPaymentRequestDTO
import com.copay.app.dto.paymentconfirmation.response.ListUnconfirmedPaymentConfirmationResponseDTO
import com.copay.app.ui.components.GroupMember
import com.copay.app.ui.components.input.dropdownField
import com.copay.app.ui.components.input.priceInputField
import com.copay.app.ui.components.pillTabRow
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.ExpenseState
import com.copay.app.utils.state.PaymentState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.PaymentConfirmationViewModel
import kotlinx.coroutines.launch

@Composable
fun manageDebtsDialog(
    onDismiss: () -> Unit,
    payments: List<ListUnconfirmedPaymentConfirmationResponseDTO>,
    groupMembers: List<GroupMember>,
    paymentConfirmationViewModel: PaymentConfirmationViewModel,
    groupViewModel: GroupViewModel,
    paymentState: PaymentState,
    userExpenses: List<UserExpenseDTO>,
    context: Context
) {
    var activeTab by remember { mutableStateOf(0) }
    var manualAmount by remember { mutableStateOf("") }

    // Convert manualAmount to float. (PrinceInputField does not accept float as value. Only String)
    val amountAsFloat by remember {
        derivedStateOf {
            manualAmount.toFloatOrNull() ?: 0f
        }
    }

    // Values from Group Session.
    val group by groupViewModel.group.collectAsState()

    // Handle snackbar to show the message.
    val successSnackbarHostState = remember { SnackbarHostState() }
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Values for the dropdowns when owner does a payment manually.
    var payer by remember { mutableStateOf(groupMembers.firstOrNull()) }
    var receiver by remember { mutableStateOf(groupMembers.getOrNull(1)) }

    // Get the debtorId from the selected receiver (either registered or external).
    val debtorId = when (receiver) {
        is GroupMember.RegisteredMember -> {
            group?.registeredMembers
                ?.find { it.phoneNumber == (receiver as GroupMember.RegisteredMember).phoneNumber }
                ?.registeredMemberId
        }

        is GroupMember.ExternalMember -> {
            group?.externalMembers
                ?.find { it.name == (receiver as GroupMember.ExternalMember).name }
                ?.externalMembersId
        }

        else -> null
    }

    // Find the userExpenseId using the debtorId (used to confirm the payment).
    val userExpenseId = userExpenses.find { it.debtorUserId == debtorId }!!.userExpenseId

    // Handle the payment state when it changes. (e.g. After approving a payment request)
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
                        Text(
                            text = "Confirm or reject payment requests made by members.",
                            style = CopayTypography.footer,
                            color = CopayColors.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (payments.isEmpty()) {
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
                                items(payments) { payment ->
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
                                                    paymentConfirmationViewModel.deletePaymentConfirmation(
                                                        context,
                                                        payment.paymentConfirmationId
                                                    )
                                                    group?.groupId?.let {
                                                        paymentConfirmationViewModel.getUnconfirmedPaymentConfirmations(
                                                            context,
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
                        Text(
                            text = "Manually record a payment for a member.",
                            style = CopayTypography.footer,
                            color = CopayColors.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        dropdownField(
                            label = "Who paid?",
                            items = groupMembers.map { it.displayText() },
                            selectedItem = payer?.displayText() ?: "",
                            onItemSelected = { selected ->
                                payer = groupMembers.find { it.displayText() == selected }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        dropdownField(
                            label = "Who received?",
                            items = groupMembers.map { it.displayText() },
                            selectedItem = receiver?.displayText() ?: "",
                            onItemSelected = { selected ->
                                receiver = groupMembers.find { it.displayText() == selected }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

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
                                    paymentConfirmationViewModel.confirmPayment(
                                        context,
                                        ConfirmPaymentRequestDTO(
                                            userExpenseId = userExpenseId,
                                            confirmationAmount = amountAsFloat
                                        )
                                    )
                                },
                            ) {
                                Text("Add Payment")
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
