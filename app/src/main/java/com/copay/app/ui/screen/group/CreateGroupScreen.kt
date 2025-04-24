package com.copay.app.ui.screen.group

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.PrimaryButton
import com.copay.app.ui.components.BackButtonTop
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.validation.GroupValidation
import kotlinx.coroutines.delay
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.copay.app.ui.components.countriesList
import com.copay.app.ui.components.input.DynamicInputList
import com.copay.app.ui.components.input.InputField
import com.copay.app.ui.components.input.PriceInputField
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.validation.UserValidation
import com.copay.app.viewmodel.GroupViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateGroupScreen(
    navigationViewModel: NavigationViewModel = viewModel()
) {
    val groupViewModel: GroupViewModel = hiltViewModel()
    val groupState by groupViewModel.groupState.collectAsState()

    val context = LocalContext.current

    var apiErrorMessage by remember { mutableStateOf<String?>(null) }

    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    var estimatedPriceText by remember { mutableStateOf("") }

    var selectedCurrency by remember { mutableStateOf("EUR") }
    val currencyList = listOf("USD", "EUR", "GBP", "JPY", "MXN")

    var groupNameError by remember { mutableStateOf<String?>(null) }
    var estimatedPriceError by remember { mutableStateOf<String?>(null) }
    var groupDescriptionError by remember { mutableStateOf<String?>(null) }
    var currencyError by remember { mutableStateOf<String?>(null) }

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarColor by remember { mutableStateOf(Color.Green) }

    var phoneInput by remember { mutableStateOf("") }
    val selectedCountry by remember { mutableStateOf(countriesList.first { it.code == "ES" }) }
    var phoneInputError by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var invitedCopayMembers by remember { mutableStateOf(listOf<String>()) }
    var invitedExternalMembers by remember { mutableStateOf(listOf("")) }

    val imageUrl by remember { mutableStateOf("") }
    val imageProvider by remember { mutableStateOf("") }

    LaunchedEffect(groupState) {
        when (groupState) {
            is GroupState.Success.GroupCreated -> {
                snackbarMessage = "Group $groupName created successfully!"
                snackbarColor = Color(0xFF4CAF50)
                showSnackbar = true
                navigationViewModel.navigateTo(SpaScreens.Home)
            }

            is GroupState.Error -> {
                apiErrorMessage = (groupState as GroupState.Error).message
                snackbarMessage = apiErrorMessage ?: "Error creating group"
                snackbarColor = Color.Red
                showSnackbar = true
            }

            else -> {}
        }
    }

    if (showSnackbar) {
        LaunchedEffect(showSnackbar) {
            delay(5000)
            showSnackbar = false
        }
    }

    fun validateInputs() {
        groupNameError = GroupValidation.validateGroupName(groupName).errorMessage
        estimatedPriceError =
            GroupValidation.validateEstimatedPrice(estimatedPriceText).errorMessage
        groupDescriptionError =
            GroupValidation.validateGroupDescription(groupDescription).errorMessage
        currencyError = GroupValidation.validateCurrency(selectedCurrency).errorMessage
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackButtonTop(
            onBackClick = { navigationViewModel.navigateTo(SpaScreens.Home) },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create a new group",
                color = CopayColors.primary,
                style = CopayTypography.title,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InputField(
                value = groupName,
                onValueChange = {
                    groupName = it
                    groupNameError = GroupValidation.validateGroupName(it).errorMessage
                },
                label = "Group Name",
                isRequired = true,
                isError = groupNameError != null,
                errorMessage = groupNameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                value = groupDescription,
                onValueChange = {
                    groupDescription = it
                    groupDescriptionError =
                        GroupValidation.validateGroupDescription(it).errorMessage
                },
                label = "Description",
                isError = groupDescriptionError != null,
                errorMessage = groupDescriptionError
            )

            Spacer(modifier = Modifier.height(16.dp))

            PriceInputField(
                value = estimatedPriceText,
                onValueChange = { newValue ->
                    estimatedPriceText = newValue
                    estimatedPriceError =
                        GroupValidation.validateEstimatedPrice(newValue).errorMessage
                },
                label = "Estimated Price",
                selectedCurrency = selectedCurrency,
                onCurrencyChange = { selectedCurrency = it },
                currencyList = currencyList,
                isRequired = true,
                isError = estimatedPriceError != null,
                errorMessage = estimatedPriceError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            DynamicInputList(
                items = invitedExternalMembers,
                onAddItem = { invitedExternalMembers = invitedExternalMembers + "" },
                onRemoveItem = { index ->
                    invitedExternalMembers =
                        invitedExternalMembers.filterIndexed { i, _ -> i != index }
                },
                onItemChange = { index, newValue ->
                    invitedExternalMembers = invitedExternalMembers.toMutableList().apply {
                        set(index, newValue)
                    }
                },
                label = "Enter member's name",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = CopayColors.surface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Invite Copay Users",
                style = CopayTypography.subtitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            )

            InputField(
                value = phoneInput,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        phoneInput = it
                        phoneInputError = UserValidation.validatePhoneNumber(it, selectedCountry.dialCode).errorMessage
                    }
                },
                label = "Enter phone numbers and press enter",
                isError = phoneInputError != null,
                errorMessage = phoneInputError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {

                    val phoneValidation = UserValidation.validatePhoneNumber(phoneInput, selectedCountry.dialCode)

                    if (phoneValidation.isValid) {

                        val maxMembersValidation =
                            GroupValidation.validateMaxInvitedCopayMembers(invitedCopayMembers)

                        if (maxMembersValidation.isValid) {
                            invitedCopayMembers = invitedCopayMembers + phoneInput
                            phoneInput = ""
                            keyboardController?.hide()
                        } else {
                            snackbarMessage = maxMembersValidation.errorMessage ?: "Error"
                            snackbarColor = Color.Red
                            showSnackbar = true
                        }
                    } else {
                        phoneInputError = phoneValidation.errorMessage
                    }
                }),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                invitedCopayMembers.forEach { phone ->
                    AssistChip(
                        onClick = {
                            invitedCopayMembers = invitedCopayMembers - phone
                        },
                        label = { Text(phone) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Added",
                                tint = Color(0xFF4CAF50)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFE8F5E9)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Create", onClick = {
                    validateInputs()

                    if (listOf(
                            groupNameError,
                            estimatedPriceError,
                            groupDescriptionError,
                            currencyError
                        ).all { it == null }
                    ) {
                        snackbarMessage = "Creating group..."
                        snackbarColor = Color(0xFF2196F3)
                        showSnackbar = true

                        val finalEstimatedPrice = estimatedPriceText.toFloatOrNull() ?: 0f
                        groupViewModel.createGroup(
                            context,
                            groupName,
                            groupDescription,
                            finalEstimatedPrice,
                            selectedCurrency,
                            invitedExternalMembers,
                            invitedCopayMembers,
                            imageUrl,
                            imageProvider
                        )
                    }
                }, modifier = Modifier.padding(horizontal = 10.dp)
            )
        }

        if (showSnackbar) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                containerColor = snackbarColor,
                contentColor = Color.White
            ) {
                Text(text = snackbarMessage)
            }
        }
    }
}