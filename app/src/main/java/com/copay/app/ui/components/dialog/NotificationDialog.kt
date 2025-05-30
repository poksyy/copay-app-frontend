package com.copay.app.ui.components.dialog

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.copay.app.dto.notification.response.NotificationResponseDTO
import com.copay.app.ui.components.listitem.notificationItem
import com.copay.app.ui.components.pillTabRow
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.viewmodel.NotificationViewModel

@Composable
fun notificationDialog(
    allNotifications: List<NotificationResponseDTO>,
    unreadNotifications: List<NotificationResponseDTO>,
    notificationViewModel: NotificationViewModel,
    context: Context,
    onDismiss: () -> Unit
) {

    var isVisible by remember { mutableStateOf(false) }

    // Tab to manage read and unread messages.
    var activeTab by remember { mutableStateOf(0) }

    // List of notifications that are only on read.
    val historyNotifications = allNotifications.filter { it.read }

    // Launch effect for the animation.
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CopayColors.onBackground.copy(alpha = 0.3f)),
        contentAlignment = Alignment.TopCenter
    ) {
        // Animation for the dialog. Start at top-right.
        AnimatedVisibility(
            visible = isVisible, enter = slideIn(
                initialOffset = { fullSize ->
                    IntOffset(x = fullSize.width, y = 0)
                }, animationSpec = tween(durationMillis = 400)
            ), exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            Surface(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .fillMaxWidth(0.85f),
                color = CopayColors.onPrimary,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Header row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Notifications",
                            style = CopayTypography.title,
                            color = CopayColors.primary
                        )

                        TextButton(onClick = {
                            notificationViewModel.markAllNotificationsAsRead(context)
                        }) {
                            Text(
                                text = "Mark all as read",
                                style = CopayTypography.button,
                                color = CopayColors.onSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tabs
                    pillTabRow(
                        tabs = listOf("Unread", "History"),
                        selectedTabIndex = activeTab,
                        onTabSelected = { activeTab = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = when (activeTab) {
                            0 -> "These are your new or pending notifications. You can mark them as read or delete them."
                            1 -> "This is the history of all notifications you’ve already read."
                            else -> ""
                        },
                        style = CopayTypography.footer,
                        color = CopayColors.onBackground
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // List by tab
                    val list = if (activeTab == 0) unreadNotifications else historyNotifications

                    if (list.isEmpty()) {
                        Text(
                            "No ${if (activeTab == 0) "unread" else "read"} notifications",
                            style = CopayTypography.body
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        ) {
                            items(list) { notif ->
                                notificationItem(
                                    notification = notif,
                                    isHistory = activeTab == 1,
                                    onMarkAsRead = {
                                        notificationViewModel.markNotificationAsRead(
                                            context,
                                            notif.notificationId
                                        )
                                    }.takeIf { !notif.read },
                                    onDelete = {
                                        notificationViewModel.deleteNotification(
                                            context,
                                            notif.notificationId
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = {
                            isVisible = false
                            onDismiss()
                        }, modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}
