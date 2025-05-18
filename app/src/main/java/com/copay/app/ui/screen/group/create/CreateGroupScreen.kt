package com.copay.app.ui.screen.group.create

import MemberListDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.copay.app.dto.group.auxiliary.InvitedExternalMemberDTO
import com.copay.app.dto.group.auxiliary.InvitedRegisteredMemberDTO
import com.copay.app.navigation.SpaScreens
import com.copay.app.ui.components.button.BackButtonTop
import com.copay.app.ui.components.button.PrimaryButton
import com.copay.app.ui.components.button.SecondaryButton
import com.copay.app.ui.components.dialog.AddMemberDialog
import com.copay.app.ui.components.input.InputField
import com.copay.app.ui.components.input.PriceInputField
import com.copay.app.ui.screen.HomeScreen
import com.copay.app.ui.theme.CopayColors
import com.copay.app.ui.theme.CopayTypography
import com.copay.app.utils.state.GroupState
import com.copay.app.utils.state.ProfileState
import com.copay.app.validation.GroupValidation
import com.copay.app.viewmodel.GroupViewModel
import com.copay.app.viewmodel.NavigationViewModel
import com.copay.app.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    navigationViewModel: NavigationViewModel = viewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    groupViewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Get the current user.
    val user by userViewModel.user.collectAsState()
    val userPhoneNumber = user?.phoneNumber

    // Inputs.
    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    var estimatedPriceText by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("EUR") }
    var invitedRegisteredMembers by remember { mutableStateOf(listOf<InvitedRegisteredMemberDTO>()) }
    var invitedExternalMembers by remember { mutableStateOf(listOf<InvitedExternalMemberDTO>()) }
    var isCreditor by remember { mutableStateOf(true) }
    val imageUrl by remember { mutableStateOf("") } // TODO: Store image
    val imageProvider by remember { mutableStateOf("") }

    // Price parsed to Float.
    val estimatedPriceFloat = estimatedPriceText.toFloatOrNull() ?: 0f

    // Error inputs.
    var groupNameError by remember { mutableStateOf<String?>(null) }
    var estimatedPriceError by remember { mutableStateOf<String?>(null) }
    var groupDescriptionError by remember { mutableStateOf<String?>(null) }
    var currencyError by remember { mutableStateOf<String?>(null) }

    // Group members (registered and external).
    val registeredMembers = remember { mutableStateListOf(userPhoneNumber) }
    val externalMembers = remember { mutableStateListOf<String>() }

    // Save names.
    val memberNames = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(registeredMembers) {
        registeredMembers
            .filterNotNull()
            .filter { it != userPhoneNumber && !memberNames.containsKey(it) }
            .forEach { phone ->
                userViewModel.getUserByPhone(context, phone)
            }
    }

    val userState by userViewModel.userState.collectAsState()

    LaunchedEffect(userState) {
        handleUserState(userState, memberNames)
    }

    // Members list for dropdown
    val membersList = remember {
        derivedStateOf {
            buildList<GroupMember> {
                add(GroupMember.Me(userPhoneNumber))

                registeredMembers
                    .filterNotNull()
                    .filter { it != userPhoneNumber }
                    .forEach { phone ->
                        val name = memberNames[phone] ?: phone
                        add(GroupMember.RegisteredMember(name, phone))
                    }

                externalMembers
                    .forEach {
                        add(GroupMember.ExternalMember(it))
                    }
            }
        }
    }

    val groupState by groupViewModel.groupState.collectAsState()

    LaunchedEffect(groupState) {
        if (groupState is GroupState.Success.GroupCreated) {
            navigationViewModel.navigateTo(SpaScreens.Home)
            groupViewModel.resetGroupState()
        }
    }

    // Dialogs
    var showMembersDialog by remember { mutableStateOf(false) }
    var showAddMembersDialog by remember { mutableStateOf(false) }

    // Dropdown creditor.
    var selectedCreditorPhone by remember { mutableStateOf<String?>(userPhoneNumber) }
    var selectedCreditor by remember { mutableStateOf<String?>("Me") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val currencyList = listOf("USD", "EUR", "GBP", "JPY", "MXN")

    fun validateInputs() {
        groupNameError = GroupValidation.validateGroupName(groupName).errorMessage
        estimatedPriceError =
            GroupValidation.validateEstimatedPrice(estimatedPriceText).errorMessage
        groupDescriptionError =
            GroupValidation.validateGroupDescription(groupDescription).errorMessage
        currencyError = GroupValidation.validateCurrency(selectedCurrency).errorMessage
    }

    // TODO: Could move this method into another package. Maybe utils?
    fun updateInvitedMembers() {
        invitedRegisteredMembers = registeredMembers.map { phone ->
            InvitedRegisteredMemberDTO(
                phoneNumber = phone!!,
                creditor = phone == selectedCreditorPhone
            )
        }

        invitedExternalMembers = externalMembers.map { name ->
            InvitedExternalMemberDTO(
                name = name,
                creditor = name == selectedCreditorPhone
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButtonTop(onBackClick = { navigationViewModel.navigateBack() })
            }

            Text(
                text = "Create New Group",
                style = CopayTypography.title,
                color = CopayColors.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Content in a scrollable column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                InputField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = "Group Name",
                    isRequired = true,
                    isError = groupNameError != null,
                    errorMessage = groupNameError
                )

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    value = groupDescription,
                    onValueChange = { groupDescription = it },
                    label = "Description",
                    isRequired = false,
                    isError = groupDescriptionError != null,
                    errorMessage = groupDescriptionError
                )

                Spacer(modifier = Modifier.height(16.dp))

                PriceInputField(
                    value = estimatedPriceText,
                    onValueChange = { estimatedPriceText = it },
                    label = "Estimated Price",
                    selectedCurrency = selectedCurrency,
                    onCurrencyChange = { selectedCurrency = it },
                    currencyList = currencyList,
                    isRequired = true,
                    isError = estimatedPriceError != null,
                    errorMessage = estimatedPriceError
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Group Members",
                    style = CopayTypography.subtitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SecondaryButton(
                        text = "View Members",
                        onClick = { showMembersDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = "Add Members",
                        onClick = {
                            showAddMembersDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Who paid for this group?",
                    style = CopayTypography.subtitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = it }
                ) {
                    TextField(
                        value = selectedCreditor ?: "Select creditor",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = CopayColors.primary,
                            unfocusedIndicatorColor = CopayColors.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .clickable { dropdownExpanded = !dropdownExpanded })

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        membersList.value.forEach { member ->
                            DropdownMenuItem(
                                text = { Text(member.displayText()) },
                                onClick = {
                                    selectedCreditor = member.displayText()
                                    selectedCreditorPhone = member.identifier
                                    dropdownExpanded = false
                                    isCreditor = true
                                }
                            )
                        }
                    }
                }
            }

            // Create Group button at the bottom
            PrimaryButton(
                text = "Create Group",
                onClick = {
                    validateInputs()
                    updateInvitedMembers()

                    groupViewModel.createGroup(
                        context,
                        groupName,
                        groupDescription,
                        estimatedPriceFloat,
                        selectedCurrency,
                        invitedExternalMembers,
                        invitedRegisteredMembers,
                        imageUrl,
                        imageProvider
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        }

        if (showAddMembersDialog) {
            AddMemberDialog(
                onDismiss = { showAddMembersDialog = false },
                onAddRegistered = { phone ->
                    registeredMembers.add(phone)
                    userViewModel.getUserByPhone(context, phone)
                    updateInvitedMembers()
                },
                onAddExternal = { name ->
                    externalMembers.add(name)
                    updateInvitedMembers()
                }
            )
        }

        if (showMembersDialog) {
            MemberListDialog(
                onDismiss = { showMembersDialog = false },
                registeredMembers = registeredMembers.filterNotNull(),
                externalMembers = externalMembers,
                onRemoveRegisteredMember = { phone ->
                    registeredMembers.remove(phone)
                    updateInvitedMembers()
                },
                onRemoveExternalMember = { name ->
                    externalMembers.remove(name)
                    updateInvitedMembers()
                },
                currentUserPhone = userPhoneNumber
            )
        }
    }
}

sealed class GroupMember {
    abstract fun displayText(): String
    abstract val identifier: String

    data class Me(val phoneNumber: String?) : GroupMember() {
        override fun displayText(): String = "Me"
        override val identifier: String = phoneNumber ?: "me"
    }

    data class RegisteredMember(val name: String, val phoneNumber: String) : GroupMember() {
        override fun displayText(): String = phoneNumber
        override val identifier: String = phoneNumber
    }

    data class ExternalMember(val name: String) : GroupMember() {
        override fun displayText(): String = "$name (External)"
        override val identifier: String = name
    }
}


fun handleUserState(state: ProfileState, memberNames: MutableMap<String, String>) {
    if (state is ProfileState.Success.GetUser) {
        val user = state.data
        user.let {
            memberNames[it.phoneNumber] = it.username
        }
    }
}
