package com.copay.app.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun logoutAfterPhoneChangeDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String = "Warning",
    message: String = "Changing your phone number will log you out for security reasons. Do you want to continue?",
    confirmText: String = "Yes",
    dismissText: String = "No"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}
