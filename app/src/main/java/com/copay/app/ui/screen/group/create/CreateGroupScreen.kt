package com.copay.app.ui.screen.group.create

import memberListDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.copay.app.ui.components.button.primaryButton
import com.copay.app.ui.components.button.secondaryButton
import com.copay.app.ui.components.dialog.addMemberDialog
import com.copay.app.ui.components.input.inputField
import com.copay.app.ui.components.input.priceInputField
import com.copay.app.ui.components.snackbar.redSnackbarHost
import com.copay.app.ui.components.topNavBar
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

    // Handle states.
    val userState by userViewModel.userState.collectAsState()
    val groupState by groupViewModel.groupState.collectAsState()

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

    // Save members names for the group.
    val memberNames = remember { mutableStateMapOf<String, String>() }

    // Save the phoneNumber in a variable to add it in the list through LaunchedEffect.
    var lastSearchedPhone by remember { mutableStateOf<String?>(null) }

    // Snackbar state
    var showSnackbar by remember { mutableStateOf(false) }
    var screenSnackbarMessage by remember { mutableStateOf("") }
    val screenSnackbarHostState = remember { SnackbarHostState() }

    // Dialogs
    var showMembersDialog by remember { mutableStateOf(false) }
    var showAddMembersDialog by remember { mutableStateOf(false) }

    // Dropdown creditor.
    var selectedCreditorPhone by remember { mutableStateOf<String?>(userPhoneNumber) }
    var selectedCreditor by remember { mutableStateOf<String?>("Me") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val currencyList = listOf("USD", "EUR", "GBP", "JPY", "MXN")

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

    LaunchedEffect(registeredMembers) {
        registeredMembers
            .filterNotNull()
            .filter { it != userPhoneNumber && !memberNames.containsKey(it) }
            .forEach { phone ->
                userViewModel.getUserByPhone(context, phone)
            }
    }

    // Handle the state to identify if the number (user) is alrady in the database.
    LaunchedEffect(userState) {
        when (userState) {
            is ProfileState.Success -> {
                when (userState) {
                    is ProfileState.Success.GetUser -> {
                        val userDto = (userState as ProfileState.Success.GetUser).data
                        val phone = userDto.phoneNumber

                        if (!registeredMembers.contains(phone)) {
                            phone?.let {
                                registeredMembers.add(it)
                                memberNames[it] = userDto.username ?: it
                                updateInvitedMembers()
                            }
                        }
                    }
                    else -> {}
                }
            }

            is ProfileState.Error -> {
                screenSnackbarMessage = (userState as ProfileState.Error).message
                showSnackbar = true
                lastSearchedPhone = null
            }

            else -> {}
        }
    }

    // Handle the state when a user tries to create a group.
    LaunchedEffect(groupState) {
        when (groupState) {
            is GroupState.Success.GroupResponse -> {
                screenSnackbarMessage = (groupState as GroupState.Success.GroupResponse).groupData.message.toString()
                showSnackbar = true
                navigationViewModel.navigateTo(SpaScreens.Home)
                groupViewModel.resetGroupState()
            }

            is GroupState.Error -> {
                screenSnackbarMessage = (groupState as GroupState.Error).message
                showSnackbar = true
            }

            else -> {}
        }
    }

    // Show in the screen the snackbar message.
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            screenSnackbarHostState.showSnackbar(screenSnackbarMessage)
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
        Column {
            topNavBar(
                title = "Create new group",
                onBackClick = { navigationViewModel.navigateBack() }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 70.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Group details",
                style = CopayTypography.subtitle,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            inputField(
                value = groupName,
                onValueChange = { groupName = it },
                label = "Group Name",
                isRequired = true,
                isError = groupNameError != null,
                errorMessage = groupNameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            inputField(
                value = groupDescription,
                onValueChange = { groupDescription = it },
                label = "Description",
                isRequired = false,
                isError = groupDescriptionError != null,
                errorMessage = groupDescriptionError
            )

            Spacer(modifier = Modifier.height(16.dp))

            priceInputField(
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
                text = "Group members",
                style = CopayTypography.subtitle,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                secondaryButton(
                    text = "View Members",
                    onClick = { showMembersDialog = true },
                    modifier = Modifier.weight(1f)
                )
                secondaryButton(
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

            // Create Group button at the bottom
            primaryButton(
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                addMemberDialog(
                    onDismiss = { showAddMembersDialog = false },
                    onAddRegistered = { phone ->
                        if (!registeredMembers.contains(phone)) {
                            lastSearchedPhone = phone
                            userViewModel.getUserByPhone(context, phone)
                        }
                    },
                    onAddExternal = { name ->
                        externalMembers.add(name)
                        updateInvitedMembers()
                    }
                )
            }
        }

        if (showMembersDialog) {
            memberListDialog(
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

        // Snackbar host is shown when user creates a group with invalid format.
        redSnackbarHost(
            hostState = screenSnackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
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
