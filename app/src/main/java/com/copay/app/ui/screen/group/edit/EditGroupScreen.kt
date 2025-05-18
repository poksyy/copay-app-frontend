package com.copay.app.ui.screen.group.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.backButtonTop
import com.copay.app.ui.screen.homeScreen
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel

@Composable
fun editGroupScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    // Get selected group from GroupSession
    val selectedGroup by groupViewModel.group.collectAsState()

    // Default values if group does not have a data.
    val name = selectedGroup?.name ?: "No name"
    val description = selectedGroup?.description ?: "No description"
    val estimatedPrice = "${selectedGroup?.estimatedPrice ?: "0"} ${selectedGroup?.currency ?: "€"}"
    val members = "${selectedGroup?.registeredMembers?.size ?: 0} registered, " +
            "${selectedGroup?.externalMembers?.size ?: 0} external"

    Box(modifier = Modifier.fillMaxSize()) {
        // Back button
        backButtonTop(
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.Home)},
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 72.dp)
        ) {
            Text(
                "Edit Group",
                color = CopayColors.primary,
                style = CopayTypography.title
            )

            // Group Name Row
            GroupRow(
                label = "Name",
                value = name,
                onClick = {
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditName)
                }
            )

            // Description Row
            GroupRow(
                label = "Description",
                value = description,
                onClick = {
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditDescription)
                }
            )

            // Estimated Price Row
            GroupRow(
                label = "Estimated Price",
                value = estimatedPrice,
                onClick = {
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditPrice)
                }
            )

            // Members Row
            GroupRow(
                label = "Members",
                value = members,
                onClick = {
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditMembers)
                }
            )
        }
    }
}

@Composable
fun GroupRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            modifier = Modifier.width(120.dp),
            color = CopayColors.primary,
            style = CopayTypography.body
        )
        Text(
            text = value,
            color = CopayColors.primary,
            style = CopayTypography.body
        )
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun editGroupScreenPreview() {
    MaterialTheme {
        editGroupScreen()
    }
}