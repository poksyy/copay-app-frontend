package com.copay.app.ui.components.dialog

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.copay.app.dto.notification.response.NotificationResponseDTO
import com.copay.app.ui.components.listitem.notificationItem
import com.copay.app.ui.components.pillTabRow
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@Composable
fun notificationDialog(
    allNotifications: List<NotificationResponseDTO>,
    unreadNotifications: List<NotificationResponseDTO>,
    notificationViewModel: NotificationViewModel,
    context: Context,
    onDismiss: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(0) }
    val historyNotifications = remember(allNotifications) {
        allNotifications.filter { it.read }
    }
    val scope = rememberCoroutineScope()

    // Show dialog with animation on first composition
    LaunchedEffect(Unit) {
        isVisible = true
    }

    // Handle dialog dismissal with proper animation
    fun dismissDialog() {
        isVisible = false
    }

    // Wait for exit animation to complete before calling onDismiss
    LaunchedEffect(isVisible) {
        if (!isVisible) {
            kotlinx.coroutines.delay(250)
            onDismiss()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // More efficient overlay - use solid color instead of complex background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { dismissDialog() }
        )

        // AnimatedVisibility with proper state control for slide animations
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { it }, // Slide from right
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it }, // Slide to right
                animationSpec = tween(200, easing = FastOutLinearInEasing)
            )
        ) {
            Surface(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .fillMaxWidth(0.85f)
                    .wrapContentHeight()
                    .clickable(enabled = false) {}, // Prevent clicks on dialog from dismissing
                color = CopayColors.onPrimary,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentHeight()
                ) {
                    notificationDialogHeader(
                        onMarkAllRead = {
                            scope.launch {
                                notificationViewModel.markAllNotificationsAsRead(context)
                            }
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    pillTabRow(
                        tabs = listOf("Unread", "History"),
                        selectedTabIndex = activeTab,
                        onTabSelected = { activeTab = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    notificationDialogDescription(activeTab)

                    Spacer(Modifier.height(16.dp))

                    // Optimized notification list with remembered computation
                    val notificationsList = remember(activeTab, unreadNotifications, historyNotifications) {
                        if (activeTab == 0) unreadNotifications else historyNotifications
                    }

                    OptimizedNotificationsList(
                        notifications = notificationsList,
                        isHistory = (activeTab == 1),
                        notificationViewModel = notificationViewModel,
                        context = context
                    )

                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = { dismissDialog() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun notificationDialogHeader(onMarkAllRead: () -> Unit) {
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
        TextButton(onClick = onMarkAllRead) {
            Text(
                text = "Mark all as read",
                style = CopayTypography.button,
                color = CopayColors.onSecondary
            )
        }
    }
}

@Composable
private fun notificationDialogDescription(activeTab: Int) {
    val description = when (activeTab) {
        0 -> "These are your new or pending notifications. You can mark them as read or delete them."
        1 -> "This is the history of all notifications you've already read."
        else -> ""
    }
    Text(
        text = description,
        style = CopayTypography.footer,
        color = CopayColors.onBackground
    )
}

@Composable
private fun OptimizedNotificationsList(
    notifications: List<NotificationResponseDTO>,
    isHistory: Boolean,
    notificationViewModel: NotificationViewModel,
    context: Context
) {
    if (notifications.isEmpty()) {
        Text(
            "No ${if (isHistory) "read" else "unread"} notifications",
            style = CopayTypography.body,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    } else {
        // Use more efficient LazyColumn with proper constraints
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = notifications,
                key = { it.notificationId }
            ) { notif ->
                notificationItem(
                    notification = notif,
                    isHistory = isHistory,
                    onMarkAsRead = if (!notif.read) {
                        { notificationViewModel.markNotificationAsRead(context, notif.notificationId) }
                    } else null,
                    onDelete = {
                        notificationViewModel.deleteNotification(context, notif.notificationId)
                    }
                )
            }
        }
    }
}