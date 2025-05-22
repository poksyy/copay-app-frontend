package com.copay.app.ui.components

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

@Composable
fun memberItem(member: Any, expense: Double, currency: String?, currentUserId: Long?) {
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
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = memberName,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "${expense.format(2)} $currency",
                    fontWeight = FontWeight.Bold,
                    color = if (isCreditor) MaterialTheme.colorScheme.error else CopayColors.primary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = memberPhoneNumber,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val isCurrentUser = when (member) {
                    is RegisteredMemberDTO -> member.registeredMemberId == currentUserId
                    else -> false
                }

                if (isCurrentUser && expense > 0) {
                    TextButton(
                        onClick = { /* TODO */ },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Pay your debts",
                                color = CopayColors.success,
                                fontSize = 12.sp
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