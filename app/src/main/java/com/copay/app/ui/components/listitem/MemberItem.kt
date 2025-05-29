package com.copay.app.ui.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.copay.app.R
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.ui.screen.group.detail.format
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun memberItem(
    member: Any,
    expense: Double,
    currency: String? = "€",
    currentUserId: Long?,
    onPayClick: (() -> Unit)? = null
) {
    // Retrieve member's display name based on member type
    val memberName = getMemberName(member)
    // Retrieve member's phone number or label based on member type
    val memberPhoneNumber = getMemberPhoneNumber(member)
    // Check if the member is a creditor (negative expense)
    val isCreditor = expense < 0
    // Check if the member is the current logged-in user
    val isCurrentUser = isCurrentUser(member, currentUserId)
    // Format the expense amount with currency symbol
    val formattedExpense = "${expense.format(2)}${currency ?: "€"}"

    // Card container for the member item with rounded corners and padding
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CopayColors.onPrimary),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top row showing member name and expense amount spaced apart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Row containing member name and optional creditor tag
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = memberName, style = CopayTypography.body)
                    if (isCreditor) {
                        Spacer(Modifier.width(8.dp))
                        creditorTag()
                    }
                }

                // Display the formatted expense, red if creditor, primary color otherwise
                Text(
                    text = formattedExpense,
                    style = CopayTypography.body,
                    color = if (isCreditor) MaterialTheme.colorScheme.error else CopayColors.primary
                )
            }

            Spacer(Modifier.height(6.dp))

            // Bottom row showing member phone number and "Pay your debt" button if applicable
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = memberPhoneNumber,
                    style = CopayTypography.footer,
                    color = CopayColors.onSecondary
                )

                // Show pay button only if current user and owes money (positive expense)
                if (isCurrentUser && expense > 0) {
                    TextButton(
                        onClick = { onPayClick?.invoke() },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Pay your debt",
                                color = CopayColors.success,
                                style = CopayTypography.footer
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_forward),
                                contentDescription = "Forward arrow",
                                tint = CopayColors.success
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun creditorTag() {
    // Small tag UI to indicate the member is a creditor with light red background and red text
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "Creditor",
            color = MaterialTheme.colorScheme.error,
            style = CopayTypography.footer
        )
    }
}

// Helper function to get member's display name based on type
private fun getMemberName(member: Any): String = when (member) {
    is RegisteredMemberDTO -> member.username
    is ExternalMemberDTO -> member.name
    else -> "Unknown"
}

// Helper function to get member's phone number or label
private fun getMemberPhoneNumber(member: Any): String = when (member) {
    is RegisteredMemberDTO -> member.phoneNumber
    is ExternalMemberDTO -> "External Member"
    else -> "Unknown"
}

// Helper function to check if the given member is the current logged-in user
private fun isCurrentUser(member: Any, currentUserId: Long?): Boolean = when (member) {
    is RegisteredMemberDTO -> member.registeredMemberId == currentUserId
    else -> false
}