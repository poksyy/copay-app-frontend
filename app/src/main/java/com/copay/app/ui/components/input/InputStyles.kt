package com.copay.app.ui.components.input

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import com.copay.app.ui.theme.CopayColors

@Composable
fun copayOutlinedTextFieldColors(isError: Boolean): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = CopayColors.onBackground,
        unfocusedTextColor = CopayColors.onBackground,
        cursorColor = if (isError) MaterialTheme.colorScheme.error else CopayColors.primary,
        focusedContainerColor = CopayColors.onPrimary,
        unfocusedContainerColor = CopayColors.onPrimary,
        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else CopayColors.primary,
        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else CopayColors.surface.copy(alpha = 0.3f),
        errorBorderColor = MaterialTheme.colorScheme.error,
        errorCursorColor = MaterialTheme.colorScheme.error,
        errorTextColor = MaterialTheme.colorScheme.error,
        errorLabelColor = MaterialTheme.colorScheme.error,
    )
}
