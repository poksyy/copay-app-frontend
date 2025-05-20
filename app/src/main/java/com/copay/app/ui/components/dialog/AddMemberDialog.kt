package com.copay.app.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.components.pillTabRow
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun addMemberDialog(
    onDismiss: () -> Unit,
    onAddRegistered: (String) -> Unit,
    onAddExternal: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var externalName by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.width(320.dp),
        title = {
            Column {
                Text(
                    text = "Add Members",
                    style = CopayTypography.title
                )
            }
        },
        text = {
            Column {
                pillTabRow(
                    tabs = listOf("Users", "Guests"),
                    selectedTabIndex = activeTab,
                    onTabSelected = { activeTab = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                when (activeTab) {
                    0 -> {
                        Text(
                            text = "Invite a user who already has a Copay account using their phone number.",
                            style = CopayTypography.footer,
                            color = CopayColors.onBackground
                        )
                        inputField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone Number",
                            modifier = Modifier.padding(top = 12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                    }

                    1 -> {
                        Text(
                            text = "Add a temporary external user by name. They won't need to have a Copay account.",
                            style = CopayTypography.footer,
                            color = CopayColors.onBackground
                        )
                        inputField(
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
                Text("Add", style = CopayTypography.body)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = CopayTypography.body)
            }
        }
    )
}