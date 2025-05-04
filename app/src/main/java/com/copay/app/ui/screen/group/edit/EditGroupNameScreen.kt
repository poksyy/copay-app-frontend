package com.copay.app.ui.screen.group.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.ui.components.input.InputField
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.validation.GroupValidation
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel

@Composable
fun EditGroupNameScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectedGroup by groupViewModel.group.collectAsState()
    val groupState by groupViewModel.groupState.collectAsState()

    // Local states
    var groupName by remember(selectedGroup?.name) { mutableStateOf(selectedGroup?.name ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }

    // Handle group state changes
    LaunchedEffect(groupState) {
        when (groupState) {
            is GroupState.Success.GroupUpdated -> {
                navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup)
                groupViewModel.resetGroupState()
            }

            is GroupState.Error -> {
                apiErrorMessage = (groupState as GroupState.Error).message
                groupViewModel.resetGroupState()
            }

            else -> {}
        }
    }

    // Function to validate inputs
    fun validateInputs(): Boolean {
        nameError = GroupValidation.validateGroupName(groupName).errorMessage
        return nameError == null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Back button
        BackButtonTop(
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup) },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        // Done button
        TextButton(
            onClick = {
                if (validateInputs()) {
                    selectedGroup?.let { group ->
                        group.groupId?.let { id ->
                            val fieldChanges = mapOf<String, Any>(
                                "name" to groupName
                            )
                            groupViewModel.updateGroup(
                                context,
                                id,
                                fieldChanges
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Done", style = MaterialTheme.typography.bodyLarge)
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 72.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Edit Group Name",
                color = CopayColors.primary,
                style = CopayTypography.title
            )

            // Group name input field
            InputField(
                value = groupName,
                onValueChange = {
                    groupName = it
                    validateInputs()
                    apiErrorMessage = null
                },
                label = "Group Name",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = nameError != null || apiErrorMessage != null
            )

            // Show validation errors
            listOfNotNull(nameError, apiErrorMessage).forEach { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Description text
            Text(
                text = "The group name should be unique and descriptive enough for members " +
                        "to easily identify it. Avoid using special characters and keep it " +
                        "between 3 and 50 characters.",
                style = CopayTypography.footer,
                color = CopayColors.surface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditGroupNameScreenPreview() {
    MaterialTheme {
        EditGroupNameScreen()
    }
}