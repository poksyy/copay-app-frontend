package com.copay.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.InvitedExternalMemberDTO
import com.copay.app.dto.group.auxiliary.InvitedRegisteredMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.model.Group
import com.copay.app.repository.GroupRepository
import com.copay.app.service.GroupService
import com.copay.app.utils.session.GroupSession
import com.copay.app.utils.session.UserSession
import com.copay.app.utils.state.GroupState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val groupService: GroupService,
    private val userSession: UserSession,
    private val groupSession: GroupSession
) : ViewModel() {

    init {
        Log.d("GroupViewModel", "GroupViewModel created")
    }

    private val _groupState = MutableStateFlow<GroupState>(GroupState.Idle)
    val groupState: MutableStateFlow<GroupState> get() = _groupState

    val group = groupSession.group

    // Resets the UI state to idle.
    fun resetGroupState() {
        _groupState.value = GroupState.Idle

    }

    fun resetGroupSession() {
        groupSession.clearGroup()
    }

    fun selectGroup(group: Group) {
        groupSession.setGroup(group)
    }

    // Fetches groups for the current user.
    fun getGroupsByUser(context: Context) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val userId = userSession.user.value?.userId ?: run {
                _groupState.value = GroupState.Error("User not logged in")
                return@launch
            }

            val backendResponse = groupRepository.getGroupsByUser(context, userId)

            _groupState.value = backendResponse
        }
    }

    // Method to create a group.
    fun createGroup(
        context: Context,
        name: String,
        description: String,
        estimatedPrice: Float,
        currency: String,
        invitedExternalMembers: List<InvitedExternalMemberDTO>,
        invitedRegisteredMembers: List<InvitedRegisteredMemberDTO>,
        imageUrl: String,
        imageProvider: String
    ) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val createdBy = userSession.user.value?.userId ?: run {
                _groupState.value = GroupState.Error("User not logged in")
                return@launch
            }

            val backendResponse = groupRepository.createGroup(
                context,
                createdBy,
                name,
                description,
                estimatedPrice,
                currency,
                invitedExternalMembers,
                invitedRegisteredMembers,
                imageUrl,
                imageProvider
            )

            _groupState.value = backendResponse
        }
    }

    // Method to delete a group
    fun deleteGroup(context: Context, groupId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.deleteGroup(context, groupId)

            if (backendResponse is GroupState.Success.GroupUpdated) {
                onSuccess()
            }

            _groupState.value = backendResponse
        }
    }

    // Method to leave a group
    fun leaveGroup(context: Context, groupId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.leaveGroup(context, groupId)

            if (backendResponse is GroupState.Success.GroupUpdated) {
                onSuccess()
            }

            _groupState.value = backendResponse
        }
    }

    // Method to update a group
    fun updateGroup(context: Context, groupId: Long, fieldChanges: Map<String, Any>) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.updateGroup(context, groupId, fieldChanges)

            _groupState.value = backendResponse

            // If the update was successful, update the group session.
            if (backendResponse is GroupState.Success.GroupUpdated) {
                val currentGroup = groupSession.group.value
                currentGroup?.let { group ->
                    val updatedGroup = group.copy(
                        name = fieldChanges["name"] as? String ?: group.name,
                        description = fieldChanges["description"] as? String ?: group.description,
                        estimatedPrice = fieldChanges["estimatedPrice"] as? Float
                            ?: group.estimatedPrice
                    )
                    groupSession.setGroup(updatedGroup)
                }
            }
        }
    }

    // Method to update registered members of a group
    fun updateGroupRegisteredMembers(
        context: Context,
        groupId: Long,
        currentRegisteredMembers: List<RegisteredMemberDTO>,
        newRegisteredMembers: List<String>
    ) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            // TODO Maybe we could simplify this with an endpoint that only does a GET by group id.
            // Filter members that contains a phone number.
            val existingRegisteredMembers = currentRegisteredMembers.filter { member ->
                newRegisteredMembers.contains(member.phoneNumber)
            }

            val newPhoneNumbers = newRegisteredMembers.filter { phoneNumber ->
                currentRegisteredMembers.none { it.phoneNumber == phoneNumber }
            }

            val newMembers = newPhoneNumbers.map { phoneNumber ->
                RegisteredMemberDTO(
                    registeredMemberId = -1, // Temporal
                    username = "Temporal (cj still working on this)", // Temporal
                    phoneNumber = phoneNumber
                )
            }

            val invitedRegisteredMembers = existingRegisteredMembers + newMembers

            val backendResponse = groupRepository.updateGroupRegisteredMembers(
                context, groupId, newRegisteredMembers
            )

            // TODO Maybe we could simplify this with an endpoint that only does a GET by group id.
            // The list is updated if the operation is successful.
            if (backendResponse is GroupState.Success.GroupUpdated) {
                val currentGroup = groupSession.group.value
                currentGroup?.let { group ->
                    val updatedGroup = group.copy(
                        registeredMembers = invitedRegisteredMembers
                    )
                    groupSession.setGroup(updatedGroup)
                }
            }

            _groupState.value = backendResponse
        }
    }

    // Method to update external members of a group
    fun updateGroupExternalMembers(
        context: Context,
        groupId: Long,
        currentExternalMembers: List<ExternalMemberDTO>,
        newExternalMembers: List<String>
    ) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            // Construct the DTO.
            val invitedExternalMembers = newExternalMembers.map { name ->

                // Check if external member exists.
                val existingMember = currentExternalMembers.find { it.name == name }

                if (existingMember != null) {

                    // External member already exists. Maintain external member ID.
                    ExternalMemberDTO(
                        externalMembersId = existingMember.externalMembersId,
                        name = name
                    )
                } else {
                    // New external member.
                    ExternalMemberDTO(
                        name = name,
                        externalMembersId = null
                    )
                }
            }

            val backendResponse = groupRepository.updateGroupExternalMembers(
                context, groupId, invitedExternalMembers
            )

            // TODO Maybe we could simplify this with an endpoint that only does a GET by group id.
            // The list is updated if the operation is successful.
            if (backendResponse is GroupState.Success.GroupUpdated) {
                val currentGroup = groupSession.group.value
                currentGroup?.let { group ->
                    val updatedGroup = group.copy(
                        externalMembers = invitedExternalMembers
                    )
                    groupSession.setGroup(updatedGroup)
                }
            }

            _groupState.value = backendResponse
        }
    }

    // Method to add registered members.
    fun addRegisteredMember(
        context: Context,
        groupId: Long,
        currentMembers: List<RegisteredMemberDTO>,
        newPhoneNumber: String
    ) {
        val updatedPhoneNumbers = currentMembers.map { it.phoneNumber } + newPhoneNumber
        updateGroupRegisteredMembers(context, groupId, currentMembers, updatedPhoneNumbers)
    }

    // Method to add external members.
    fun addExternalMember(
        context: Context,
        groupId: Long,
        currentMembers: List<ExternalMemberDTO>,
        newName: String
    ) {
        val updatedNames = currentMembers.map { it.name } + newName
        updateGroupExternalMembers(context, groupId, currentMembers, updatedNames)
    }
}
