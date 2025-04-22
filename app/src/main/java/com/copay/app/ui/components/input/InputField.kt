package com.copay.app.ui.components.input

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.copay.app.ui.theme.CopayColors

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier,
    isSingleLine: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    isRequired: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column (modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(text = if (isRequired) "$label *" else label)
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = isSingleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = CopayColors.onBackground,
                unfocusedTextColor = CopayColors.onBackground,
                cursorColor = CopayColors.primary,
                focusedContainerColor = CopayColors.onPrimary,
                unfocusedContainerColor = CopayColors.onPrimary,
                focusedBorderColor = CopayColors.primary,
                unfocusedBorderColor = CopayColors.surface.copy(alpha = 0.3f)
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = trailingIcon
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
