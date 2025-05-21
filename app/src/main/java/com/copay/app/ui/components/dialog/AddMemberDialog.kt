package com.copay.app.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.components.pillTabRow
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import kotlinx.coroutines.launch

@Composable
fun addMemberDialog(
    onDismiss: () -> Unit,
    onAddRegistered: (String) -> Unit,
    onAddExternal: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var externalName by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf(0) }

    // Show snackbar messages.
    val localErrorSnackbarHostState = remember { SnackbarHostState() }
    val localSuccessSnackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            tonalElevation = 8.dp,
            shape = MaterialTheme.shapes.medium,
            color = CopayColors.onPrimary
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .width(320.dp)
            ) {
                Text("Add Members", style = CopayTypography.title)

                Spacer(modifier = Modifier.height(8.dp))

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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        when (activeTab) {
                            0 -> {
                                if (phoneNumber.length < 9) {
                                    coroutineScope.launch {
                                        localErrorSnackbarHostState.showSnackbar("Invalid phone number")
                                    }
                                } else {
                                    onAddRegistered(phoneNumber)
                                    phoneNumber = ""
                                    coroutineScope.launch {
                                        localSuccessSnackbarHostState.showSnackbar("Member added")
                                    }
                                }
                            }

                            1 -> {
                                if (externalName.isBlank()) {
                                    coroutineScope.launch {
                                        localErrorSnackbarHostState.showSnackbar("Name cannot be empty")
                                    }
                                } else {
                                    onAddExternal(externalName)
                                    externalName = ""
                                    coroutineScope.launch {
                                        localSuccessSnackbarHostState.showSnackbar("Member added")
                                    }
                                }
                            }
                        }
                    }) {
                        Text("Add")
                    }
                }

                // Snackbar host.
                redSnackbarHost(
                    hostState = localErrorSnackbarHostState,
                    modifier = Modifier.align(Alignment.End)
                )

                greenSnackbarHost(
                    hostState = localSuccessSnackbarHostState,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}