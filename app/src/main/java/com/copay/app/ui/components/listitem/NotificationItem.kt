package com.copay.app.ui.components.listitem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.formatDateTime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import com.copay.app.dto.notification.response.NotificationResponseDTO

@Composable
fun notificationItem(
    notification: NotificationResponseDTO,
    isHistory: Boolean,
    onMarkAsRead: (() -> Unit)? = null,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CopayColors.onPrimary
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = notification.message,
                style = CopayTypography.body,
                color = CopayColors.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDateTime(notification.createdAt),
                    style = CopayTypography.footer,
                    color = CopayColors.onSecondary
                )

                Row {
                    if (!isHistory) {
                        onMarkAsRead?.let {
                            IconButton(onClick = it) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Mark as read",
                                    tint = CopayColors.success
                                )
                            }
                        }
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete notification",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
