package com.copay.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun topNavBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            backButtonTop(onBackClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart))
            Text(
                text = title,
                style = CopayTypography.navTitle,
                color = CopayColors.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            color = CopayColors.outline,
            thickness = 0.5.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

