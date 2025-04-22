package com.copay.app.ui.components.input

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copay.app.ui.theme.CopayColors

@Composable
fun DynamicInputList(
    items: List<String>,
    onAddItem: () -> Unit,
    onRemoveItem: (Int) -> Unit,
    onItemChange: (Int, String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    InputField(
                        value = item,
                        onValueChange = { newValue -> onItemChange(index, newValue) },
                        label = label,
                        isRequired = if (index == 0) isRequired else false
                    )
                }
                IconButton(onClick = {
                    if (index == 0) {
                        onAddItem()
                    } else {
                        onRemoveItem(index)
                    }
                }) {
                    Icon(
                        imageVector = if (index == 0) Icons.Default.Add else Icons.Default.Remove,
                        contentDescription = if (index == 0) "Add item" else "Remove item",
                        tint = if (index == 0) CopayColors.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            if (index < items.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}