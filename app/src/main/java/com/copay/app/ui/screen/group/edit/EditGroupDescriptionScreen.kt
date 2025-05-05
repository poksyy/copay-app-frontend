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
fun EditGroupDescriptionScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectedGroup by groupViewModel.group.collectAsState()
    val groupState by groupViewModel.groupState.collectAsState()

    var groupDescription by remember(selectedGroup?.description) {
        mutableStateOf(
            selectedGroup?.description ?: ""
        )
    }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }

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

    fun validateInputs(): Boolean {
        descriptionError = GroupValidation.validateGroupDescription(groupDescription).errorMessage
        return descriptionError == null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackButtonTop(
            onBackClick = { navigationViewModel.navigateBack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        TextButton(
            onClick = {
                if (validateInputs()) {
                    selectedGroup?.let { group ->
                        group.groupId?.let { id ->
                            val fieldChanges = mapOf<String, Any>(
                                "description" to groupDescription
                            )
                            groupViewModel.updateGroup(
                                context, id, fieldChanges
                            )
                        }
                    }
                }
            }, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Done", style = MaterialTheme.typography.bodyLarge)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 72.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Edit Group Description", color = CopayColors.primary, style = CopayTypography.title
            )

            InputField(
                value = groupDescription,
                onValueChange = {
                    groupDescription = it
                    validateInputs()
                    apiErrorMessage = null
                },
                label = "Group Description",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = descriptionError != null || apiErrorMessage != null
            )

            listOfNotNull(descriptionError, apiErrorMessage).forEach { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "Describe the purpose or theme of your group. This helps members " +
                        "understand what the group is about. You can mention activities, " +
                        "rules, or any important information.",
                style = CopayTypography.footer,
                color = CopayColors.surface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditGroupDescriptionScreenPreview() {
    MaterialTheme {
        EditGroupDescriptionScreen()
    }
}
