package com.copay.app.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    val selectedTabColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
    val unselectedTabColor = Color.Transparent
    val selectedTextColor = MaterialTheme.colorScheme.primary
    val unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Member") },
        text = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        modifier = Modifier
                            .weight(1f)
                            .background(if (activeTab == 0) selectedTabColor else unselectedTabColor)
                            .padding(vertical = 4.dp, horizontal = 2.dp),
                        text = {
                            Text(
                                "Registered Users",
                                color = if (activeTab == 0) selectedTextColor else unselectedTextColor,
                                fontWeight = if (activeTab == 0) FontWeight.Medium else FontWeight.Normal,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    )

                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        modifier = Modifier
                            .weight(1f)
                            .background(if (activeTab == 1) selectedTabColor else unselectedTabColor)
                            .padding(vertical = 4.dp, horizontal = 2.dp),
                        text = {
                            Text(
                                "Guests",
                                color = if (activeTab == 1) selectedTextColor else unselectedTextColor,
                                fontWeight = if (activeTab == 1) FontWeight.Medium else FontWeight.Normal,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                when (activeTab) {
                    0 -> {
                        Text(
                            text = "Invite a user who already has a Copay account using their phone number.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        InputField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone Number",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    1 -> {
                        Text(
                            text = "Add a temporary external user by name. They won't need to have a Copay account.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        InputField(
                            value = externalName,
                            onValueChange = { externalName = it },
                            label = "Name",
                            modifier = Modifier.padding(top = 12.dp)
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