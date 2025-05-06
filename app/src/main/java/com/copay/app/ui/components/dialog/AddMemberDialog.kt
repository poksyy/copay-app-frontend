package com.copay.app.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onAddRegistered: (String) -> Unit,
    onAddExternal: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var isExternal by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Member") },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username or Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    RadioButton(
                        selected = !isExternal,
                        onClick = { isExternal = false }
                    )
                    Text("Registered")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = isExternal,
                        onClick = { isExternal = true }
                    )
                    Text("External")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isExternal) onAddExternal(username)
                    else onAddRegistered(username)
                    onDismiss()
                },
                enabled = username.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
