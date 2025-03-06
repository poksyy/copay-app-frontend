package com.copay.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    isRequired: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val labelText = if (isRequired) "$label *" else label

        TextField(
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            placeholder = { Text(text = labelText, fontSize = 16.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, if (isError) Color.Red else Color.Gray, RoundedCornerShape(48.dp))
                .background(Color.White, RoundedCornerShape(48.dp))
                .padding(8.dp)
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

@Preview
@Composable
fun PreviewInputField() {
    var text by remember { mutableStateOf("") }
    InputField(
        value = text,
        onValueChange = { text = it },
        label = "Enter text",
        isPassword = false,
        isError = text.isEmpty(),
        errorMessage = if (text.isEmpty()) "This field is required" else null
    )
}
