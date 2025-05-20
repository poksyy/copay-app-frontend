package com.copay.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun pillTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(CopayColors.surface)
            .padding(4.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (selectedTabIndex == index)
                            MaterialTheme.colorScheme.surfaceVariant
                        else
                            CopayColors.surface,
                        RoundedCornerShape(50.dp)
                    )
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = CopayTypography.body,
                    textAlign = TextAlign.Center,
                    fontWeight = if (selectedTabIndex == index) FontWeight.Medium else FontWeight.Normal,
                    color = if (selectedTabIndex == index)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}