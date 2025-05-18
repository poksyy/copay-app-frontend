package com.copay.app.ui.screen.group.edit

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.backButtonTop
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
        backButtonTop(
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.GroupSubscreen.EditGroup )},
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        TextButton(
            onClick = {
                if (validateInputs()) {
                    selectedGroup?.groupId?.let { id ->
                        val fieldChanges = mapOf<String, Any>(
                            "estimatedPrice" to groupPrice.toFloat()
                        )
                        groupViewModel.updateGroupEstimatedPrice(
                            context,
                            id,
                            groupPrice.toFloat()
                        )

                    }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "Done",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 72.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Edit Group Price",
                color = CopayColors.primary,
                style = CopayTypography.title
            )

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

                Text(
                    text = "Set a fair monthly price for the group subscription. The price must be a number between 0.00 and 99.99 €.",
                    style = CopayTypography.footer,
                    color = CopayColors.surface,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
