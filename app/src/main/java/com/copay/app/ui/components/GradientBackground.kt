package com.copay.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF121212) || MaterialTheme.colorScheme.background == Color.Black

    Box(
        modifier = Modifier
            .fillMaxSize()
            .let { base ->
                if (isDarkTheme) {
                    base.background(Color.Black)
                } else {
                    base.background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFEAF0F9),
                                Color(0xFFF7F9FB),
                                Color(0xFFEDE7F6)
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
                }
            }
    ) {
        content()
    }
}
