package com.copay.app.ui.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.copay.app.R
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.ui.screen.group.detail.format
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun memberItem(member: Any, expense: Double, currency: String?, currentUserId: Long?, onPayClick: (() -> Unit)? = null) {
    val memberName: String
    val memberPhoneNumber: String

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

    val isCreditor = expense < 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CopayColors.onPrimary
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = memberName,
                    style = CopayTypography.body,
                )
                Text(
                    text = "${expense.format(2)}€",
                    style = CopayTypography.body,
                    color = if (isCreditor) MaterialTheme.colorScheme.error else CopayColors.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

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

                val isCurrentUser = when (member) {
                    is RegisteredMemberDTO -> member.registeredMemberId == currentUserId
                    else -> false
                }

                Box(
                    modifier = Modifier
                        .height(24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
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
}