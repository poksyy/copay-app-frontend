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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateGroupScreen(
    navigationViewModel: NavigationViewModel = viewModel()
) {
    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    var estimatedPrice by remember { mutableStateOf("") }

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
    var phoneNumbers by remember { mutableStateOf(listOf<String>()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showSnackbar) {
        LaunchedEffect(showSnackbar) {
            delay(5000)
            showSnackbar = false
        }
    }

    // Function to validate all inputs
    fun validateInputs() {
        groupNameError = GroupValidation.validateGroupName(groupName).errorMessage
        estimatedPriceError = GroupValidation.validateEstimatedPrice(estimatedPrice).errorMessage
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
                    groupDescription = it
                    groupDescriptionError = GroupValidation.validateGroupDescription(it).errorMessage
                },
                label = { Text("Description (optional)") },
                isError = groupDescriptionError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Enter description") }
            )
            if (groupDescriptionError != null) {
                Text(text = groupDescriptionError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = estimatedPrice,
                onValueChange = {
                    // Solo permitir números y el punto decimal
                    if (it.all { char -> char.isDigit() || char == '.' }) {
                        estimatedPrice = it
                    }
                    estimatedPriceError = GroupValidation.validateEstimatedPrice(it).errorMessage
                },
                label = { Text("Estimated Price") },
                isError = estimatedPriceError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Enter estimated price") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal
                )
            )

            if (estimatedPriceError != null) {
                Text(text = estimatedPriceError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = selectedCurrency,
                    onValueChange = {},
                    label = { Text("Currency") },
                    readOnly = true,
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Currency dropdown",
                            modifier = Modifier.clickable { isCurrencyDropdownExpanded = !isCurrencyDropdownExpanded }
                        )
                    }
                )

                DropdownMenu(
                    expanded = isCurrencyDropdownExpanded,
                    onDismissRequest = { isCurrencyDropdownExpanded = false }
                ) {
                    currencyList.forEach { currency ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCurrency = currency
                                isCurrencyDropdownExpanded = false
                            },
                            text = { Text(text = currency) }
                        )
                    }
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
                label = { Text("Add members by phone") },
                placeholder = { Text("Enter phone number and press Enter") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (phoneInput.length >= 5) {
                            val validationResult = GroupValidation.validateMaxPhoneNumbers(phoneNumbers)

                            if (validationResult.isValid) {
                                phoneNumbers = phoneNumbers + phoneInput
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

            // Chips inside outline.
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                phoneNumbers.forEach { phone ->
                    AssistChip(
                        onClick = {
                            phoneNumbers = phoneNumbers - phone
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

                    // Validates if the input content is null or not.
                    if (listOf(groupNameError, estimatedPriceError, groupDescriptionError, currencyError).all { it == null }) {

                        // TODO: BACKEND LOGIC.
                        snackbarMessage = "Group $groupName created successfully!"
                        snackbarColor = Color(0xFF4CAF50)
                        showSnackbar = true
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