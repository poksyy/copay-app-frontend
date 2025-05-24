package com.copay.app.ui.components.input

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors

@Composable
fun priceInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    selectedCurrency: String,
    onCurrencyChange: (String) -> Unit,
    currencyList: List<String>,
    isRequired: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var isCurrencyDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label + if (isRequired) " *" else "",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        onValueChange(it)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = CopayColors.primary.copy(alpha = 0.3f),
                    focusedBorderColor = CopayColors.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else CopayColors.primary,
                    cursorColor = if (isError) MaterialTheme.colorScheme.error else CopayColors.primary
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                isError = isError,
                singleLine = true,
                placeholder = { Text("0.00") }
            )

            Box(
                modifier = Modifier
                    .width(90.dp)
                    .fillMaxHeight()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
                    .clickable { isCurrencyDropdownExpanded = true },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = selectedCurrency)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = isCurrencyDropdownExpanded,
                    onDismissRequest = { isCurrencyDropdownExpanded = false }
                ) {
                    currencyList.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(currency) },
                            onClick = {
                                onCurrencyChange(currency)
                                isCurrencyDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
