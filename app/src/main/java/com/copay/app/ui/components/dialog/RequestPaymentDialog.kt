package com.copay.app.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun requestPaymentDialog(
    groupId: Long,
    userId: Long,
    amount: String,
    onAmountChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onRequestSent: (Float) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Request Payment") },
        text = {
            Column {
                Text("Enter the amount to request:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = amount,
                    onValueChange = { input ->
                        if (input.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                            onAmountChange(input)
                        }
                    },
                    placeholder = { Text("0.00") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    )
            }
        },
        confirmButton = {
            TextButton(
                enabled = amount.isNotEmpty() && !isLoading,
                onClick = {
                    isLoading = true
                    val amountFloat = amount.toFloatOrNull() ?: 0f
                    onRequestSent(amountFloat)
                    isLoading = false
                }
            ) {
                Text("Send")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}