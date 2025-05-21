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
import com.copay.app.ui.components.snackbar.greenSnackbarHost
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.components.topNavBar
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.utils.state.ProfileState
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

@Composable
fun editGroupScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    // Get selected group from GroupSession.
    val selectedGroup by groupViewModel.group.collectAsState()

    // Default values if group does not have a data.
    val name = selectedGroup?.name ?: "No name"
    val description = selectedGroup?.description ?: "No description"
    val estimatedPrice = "${selectedGroup?.estimatedPrice ?: "0"} ${selectedGroup?.currency ?: "€"}"
    val members = "${selectedGroup?.registeredMembers?.size ?: 0} registered, " +
            "${selectedGroup?.externalMembers?.size ?: 0} external"

    val groupState by groupViewModel.groupState.collectAsState()

    val successSnackbarHostState = remember { SnackbarHostState() }
    val errorSnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(groupState) {
        when (groupState) {
            is GroupState.Success.GroupUpdated -> {
                coroutineScope.launch {
                    successSnackbarHostState.showSnackbar("Group information updated successfully")
                }
                groupViewModel.resetGroupState()
            }
            is GroupState.Error -> {
                coroutineScope.launch {
                    errorSnackbarHostState.showSnackbar("An error occurred while updating group information")
                }
                groupViewModel.resetGroupState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        topNavBar(
            title = "Edit group",
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.Home) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        // Main content.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 90.dp)
        ) {
            groupRow(
                label = "Name",
                value = name,
                onClick = {
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditName)
                }
            )
            groupRow(
                label = "Description",
                value = description,
                onClick = {
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditDescription)
                }
            )
            groupRow(
                label = "Estimated Price",
                value = estimatedPrice,
                onClick = {
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditPrice)
                }
            )
            groupRow(
                label = "Members",
                value = members,
                onClick = {
                    navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditMembers)
                }
            )
        }

        // Snackbar host.
        redSnackbarHost(
            hostState = errorSnackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        greenSnackbarHost(
            hostState = successSnackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun groupRow(
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