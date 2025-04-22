package com.copay.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Light mode theme.
val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    secondary = LightBackground,
    onSecondary = LightSecondaryText,
    background = LightBackground,
    onBackground = LightBackgroundText,
    surface = LightAccent,
    outline = LightBorders
)

// Dark mode theme.
val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    secondary = DarkBackground,
    onSecondary = DarkSecondaryText,
    background = DarkBackground,
    onBackground = DarkBackgroundText,
    surface = DarkAccent,
    outline = DarkBorders
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
