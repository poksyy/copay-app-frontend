package com.copay.app.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO

@Composable
fun removeRegisteredMemberDialog(
    member: RegisteredMemberDTO?,
    onDismiss: () -> Unit,
    onConfirm: (RegisteredMemberDTO) -> Unit,
    title: String = "Remove Member",
    confirmText: String = "Remove",
    dismissText: String = "Cancel"
) {
    if (member == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text("Are you sure you want to remove ${member.username}?") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(member)
                    onDismiss()
                }
            ) {
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

@Composable
fun RemoveExternalMemberDialog(
    member: ExternalMemberDTO?,
    onDismiss: () -> Unit,
    onConfirm: (ExternalMemberDTO) -> Unit,
    title: String = "Remove Member",
    confirmText: String = "Remove",
    dismissText: String = "Cancel"
) {
    if (member == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text("Are you sure you want to remove ${member.name}?") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(member)
                    onDismiss()
                }
            ) {
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