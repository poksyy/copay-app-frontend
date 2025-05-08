package com.copay.app.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copay.app.ui.components.input.InputField

@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onAddRegistered: (String) -> Unit,
    onAddExternal: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var externalName by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Members") },
        text = {
            Column {
                TabRow(selectedTabIndex = activeTab) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = { Text("Copay Users") }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = { Text("External") }
                    )
                }

                when (activeTab) {
                    0 -> {
                        InputField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone Number",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                    1 -> {
                        InputField(
                            value = externalName,
                            onValueChange = { externalName = it },
                            label = "Name",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when (activeTab) {
                        0 -> {
                            onAddRegistered(phoneNumber)
                            phoneNumber = ""
                        }
                        1 -> {
                            onAddExternal(externalName)
                            externalName = ""
                        }
                    }
                },
                enabled = when (activeTab) {
                    0 -> phoneNumber.isNotBlank()
                    1 -> externalName.isNotBlank()
                    else -> false
                }
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