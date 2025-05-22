package com.copay.app.ui.components.snackbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography

@Composable
fun redSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            Snackbar(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = CopayColors.onPrimary,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = CopayColors.onPrimary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = snackbarData.visuals.message,
                        style = CopayTypography.body,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
}
