package com.copay.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

object CopayColors {
    val primary: Color
        @Composable get() = MaterialTheme.colorScheme.primary

    //  Text inside primary buttons.
    val onPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary

    val secondary: Color
        @Composable get() = MaterialTheme.colorScheme.secondary

    // Text inside secondary buttons.
    val onSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.onSecondary

    val background: Color
        @Composable get() = MaterialTheme.colorScheme.background

    // Text for default body.
    val onBackground: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground

    val surface: Color
        @Composable get() = MaterialTheme.colorScheme.surface

    val outline: Color
        @Composable get() = MaterialTheme.colorScheme.outline

    // Success/Green color
    val success: Color
        @Composable get() = SuccessGreen
}

object CopayTypography {
    val title: TextStyle
        @Composable get() = MaterialTheme.typography.titleLarge

    val navTitle: TextStyle
        @Composable get() = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)

    val subtitle: TextStyle
        @Composable get() = MaterialTheme.typography.titleMedium

    val body: TextStyle
        @Composable get() = MaterialTheme.typography.bodyMedium

    val button: TextStyle
        @Composable get() = MaterialTheme.typography.displayMedium

    val footer: TextStyle
        @Composable get() = MaterialTheme.typography.labelSmall

}
