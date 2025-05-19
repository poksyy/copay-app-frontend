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
import com.copay.app.ui.components.button.secondaryButton
import com.copay.app.ui.components.topNavBar
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.validation.GroupValidation
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel

@Composable
fun editGroupPriceScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectedGroup by groupViewModel.group.collectAsState()
    val groupState by groupViewModel.groupState.collectAsState()

    var groupPrice by remember(selectedGroup?.estimatedPrice) {
        mutableStateOf(selectedGroup?.estimatedPrice?.toString() ?: "")
    }
    var priceError by remember { mutableStateOf<String?>(null) }
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
        priceError = GroupValidation.validateEstimatedPrice(groupPrice).errorMessage
        return priceError == null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        topNavBar(
            title = "Edit group price",
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 90.dp, bottom = 16.dp),
        ) {
            inputField(
                value = groupPrice,
                onValueChange = {
                    groupPrice = it
                    validateInputs()
                    apiErrorMessage = null
                },
                label = "Price (€)",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = priceError != null || apiErrorMessage != null
            )

            listOfNotNull(priceError, apiErrorMessage).forEach { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "Set an estimated price for the group's initial expense. The price must be a positive number and cannot be negative.",
                style = CopayTypography.footer,
                color = CopayColors.surface,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            secondaryButton(
                text = "Save changes",
                onClick = {
                    if (validateInputs()) {
                        selectedGroup?.groupId?.let { id ->
                            val priceFloat = groupPrice.toFloatOrNull()
                            if (priceFloat != null) {
                                groupViewModel.updateGroupEstimatedPrice(context, id, priceFloat)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun editGroupPriceScreenPreview() {
    MaterialTheme {
        editGroupPriceScreen()
    }
}
