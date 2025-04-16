package com.copay.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.foundation.border
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.copay.app.utils.state.GroupState
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
    var isCurrencyDropdownExpanded by remember { mutableStateOf(false) }
    val currencyList = listOf("USD", "EUR", "GBP", "JPY", "MXN")

    var groupNameError by remember { mutableStateOf<String?>(null) }
    var estimatedPriceError by remember { mutableStateOf<String?>(null) }
    var groupDescriptionError by remember { mutableStateOf<String?>(null) }
    var currencyError by remember { mutableStateOf<String?>(null) }

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarColor by remember { mutableStateOf(Color.Green) }

    var phoneInput by remember { mutableStateOf("") }
    var invitedCopayMembers by remember { mutableStateOf(listOf<String>()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var invitedExternalMembers by remember { mutableStateOf(listOf("")) }

    LaunchedEffect(groupState) {
        when (groupState) {
            is GroupState.Success -> {
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
        estimatedPriceError = GroupValidation.validateEstimatedPrice(estimatedPriceText).errorMessage
        groupDescriptionError = GroupValidation.validateGroupDescription(groupDescription).errorMessage
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create a new group",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = groupName,
                onValueChange = {
                    groupName = it
                    groupNameError = GroupValidation.validateGroupName(it).errorMessage
                },
                label = { Text("Group Name") },
                isError = groupNameError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Enter group name") }
            )
            if (groupNameError != null) {
                Text(text = groupNameError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = groupDescription,
                onValueChange = {
                    if (it.length <= 50) {
                        groupDescription = it
                        groupDescriptionError = GroupValidation.validateGroupDescription(it).errorMessage
                    }
                },
                label = { Text("Description (optional)") },
                isError = groupDescriptionError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Enter description") },
                supportingText = {
                    Text("${groupDescription.length}/50")
                }
            )

            if (groupDescriptionError != null) {
                Text(text = groupDescriptionError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = estimatedPriceText,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            estimatedPriceText = newValue
                            estimatedPriceError = GroupValidation.validateEstimatedPrice(newValue).errorMessage
                        }
                    },
                    label = { Text("Estimated Price") },
                    isError = estimatedPriceError != null,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter estimated price") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    ),
                    trailingIcon = {
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(0.dp)
                                )
                                .padding(horizontal = 8.dp)
                                .clickable { isCurrencyDropdownExpanded = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(text = selectedCurrency)
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select currency"
                                )
                            }

                            DropdownMenu(
                                expanded = isCurrencyDropdownExpanded,
                                onDismissRequest = { isCurrencyDropdownExpanded = false }
                            ) {
                                currencyList.forEach { currency ->
                                    DropdownMenuItem(
                                        text = { Text(currency) },
                                        onClick = {
                                            selectedCurrency = currency
                                            isCurrencyDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }

            if (estimatedPriceError != null) {
                Text(
                    text = estimatedPriceError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            invitedExternalMembers.forEachIndexed { index, member ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = member,
                        onValueChange = { newValue ->
                            invitedExternalMembers = invitedExternalMembers.toMutableList().apply {
                                set(index, newValue)
                            }
                        },
                        label = { Text("Enter members name") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    if (index == 0) {
                        IconButton(
                            onClick = {
                                invitedExternalMembers = invitedExternalMembers + ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add member",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                invitedExternalMembers = invitedExternalMembers.filterIndexed { i, _ -> i != index }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Remove member",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                if (index < invitedExternalMembers.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = phoneInput,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        phoneInput = it
                    }
                },
                label = { Text("Enter phone of Copay users (optional)") },
                placeholder = { Text("Enter phone number and press Enter") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (phoneInput.length >= 5) {
                            val validationResult = GroupValidation.validateMaxInvitedCopayMembers(invitedCopayMembers)

                            if (validationResult.isValid) {
                                invitedCopayMembers = invitedCopayMembers + phoneInput
                                phoneInput = ""
                                keyboardController?.hide()
                            } else {
                                snackbarMessage = validationResult.errorMessage ?: "Error"
                                snackbarColor = Color.Red
                                showSnackbar = true
                            }
                        }
                    }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                text = "Create",
                onClick = {
                    validateInputs()

                    if (listOf(groupNameError, estimatedPriceError, groupDescriptionError, currencyError).all { it == null }) {
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
                            invitedCopayMembers
                        )
                    }
                },
                modifier = Modifier.padding(horizontal = 10.dp)
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