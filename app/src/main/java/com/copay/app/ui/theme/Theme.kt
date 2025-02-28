package com.copay.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Primary,
    surface = Secondary,
    onPrimary = Secondary
)

val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Secondary,
    surface = Primary,
    onPrimary = Primary
)

@Composable
fun CopayTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
