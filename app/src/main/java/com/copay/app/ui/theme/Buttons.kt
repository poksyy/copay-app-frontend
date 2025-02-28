package com.copay.app.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MainButtonBackground),
        modifier = modifier.fillMaxWidth().padding(64.dp).height(64.dp)
    ) {
        Text(text = text, color = White, style = AppTypography.displayMedium)
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = SecondaryButtonFill),
        border = BorderStroke(1.dp, SecondaryButtonBorder),
        modifier = modifier.fillMaxWidth().padding(64.dp).height(64.dp)
    ) {
        Text(text = text, color = Black, style = AppTypography.displayMedium)
    }
}

@Composable
fun LogoutButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = LogoutButtonBackground),
        modifier = modifier.fillMaxWidth().padding(64.dp).height(64.dp)
    ) {
        Text(text = text, color = White, style = AppTypography.displayMedium)
    }
}
