package com.copay.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = CopayTypography.navTitle,
                    color = CopayColors.primary
                )
            },
            navigationIcon = {
                backButtonTop(onBackClick = onBackClick)
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = CopayColors.background
            )
        )

        HorizontalDivider(
            color = CopayColors.outline,
            thickness = 0.5.dp
        )
    }
}