package com.copay.app.ui.components.listitem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copay.app.dto.paymentconfirmation.response.PaymentResponseDTO
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.formatDateTime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon

@Composable
fun paymentActivityItem(payment: PaymentResponseDTO) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Main line: debtor ➝ recipient
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${payment.debtorUsername} ➝ ${payment.creditorUsername}",
                    style = CopayTypography.body,
                    color = CopayColors.primary
                )

                Text(
                    text = "${payment.confirmationAmount}€",
                    style = CopayTypography.body,
                    color = CopayColors.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom line: date and status.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDateTime(payment.confirmationDate),
                    style = CopayTypography.footer,
                    color = CopayColors.onSecondary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (payment.isConfirmed) Icons.Filled.Check else Icons.Filled.Schedule,
                        contentDescription = if (payment.isConfirmed) "Confirmed" else "Pending",
                        tint = if (payment.isConfirmed) CopayColors.success else CopayColors.warning,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (payment.isConfirmed) "Confirmed" else "Pending",
                        style = CopayTypography.footer,
                        color = if (payment.isConfirmed) CopayColors.success else CopayColors.warning
                    )
                }
            }
        }
    }
}
