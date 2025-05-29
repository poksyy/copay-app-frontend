package com.copay.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.InvitedExternalMemberDTO
import com.copay.app.dto.group.auxiliary.InvitedRegisteredMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import com.copay.app.mappers.toGroup
import com.copay.app.model.Group
import com.copay.app.repository.GroupRepository
import com.copay.app.repository.PhotoRepository
import com.copay.app.utils.session.GroupSession
import com.copay.app.utils.session.UserSession
import com.copay.app.utils.state.GroupState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val groupRepository: GroupRepository,
    private val userSession: UserSession,
    private val groupSession: GroupSession
) : ViewModel() {

    private val _groupState = MutableStateFlow<GroupState>(GroupState.Idle)
    val groupState: MutableStateFlow<GroupState> get() = _groupState

    val group = groupSession.group

    // Save the list of groups to avoid unnecessary reloads (Caché).
    private var cachedGroupsDTO: GetGroupResponseDTO? = null

    // Notice if there are new fetched groups different from cached .
    val hasNewGroups = MutableStateFlow(false)

    // Resets the UI state to idle.
    fun resetGroupState() {
        _groupState.value = GroupState.Idle
    }

    // Updates the UI state with a success message
    fun updateGroupStateWithMessage(message: String) {
        _groupState.value = GroupState.Success.GroupUpdated(
            com.copay.app.dto.MessageResponseDTO(message)
        )
    }

    fun resetGroupSession() {
        groupSession.clearGroup()
    }

    fun selectGroup(group: Group) {
        groupSession.setGroup(group)
    }

    fun getCurrentUserId(): Long? {
        return userSession.user.value?.userId
    }

    // Fetches groups for the current user.
    fun getGroupsByUser(context: Context, forceRefresh: Boolean = false) {
        viewModelScope.launch {

            val userId = userSession.user.value?.userId ?: run {
                _groupState.value = GroupState.Error("User not logged in")
                return@launch
            }

            // If cache is available, use it and silently refresh in the background.
            if (!forceRefresh && cachedGroupsDTO != null) {
                _groupState.value = GroupState.Success.GroupsFetched(cachedGroupsDTO!!)
                refreshGroupsSilently(context, userId)
                return@launch
            }

            // Full fetch if there's no cached data
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.getGroupsByUser(context, userId)

            if (backendResponse is GroupState.Success.GroupsFetched) {
                cachedGroupsDTO = backendResponse.groupsData
                hasNewGroups.value = false
            }

            _groupState.value = backendResponse
        }
    }

    // Method to fetch a single group by its group ID.
    fun getGroupByGroupId(context: Context, groupId: Long) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.getGroupByGroupId(context, groupId)

            if (backendResponse is GroupState.Success.GroupResponse) {
                groupSession.setGroup(backendResponse.groupData.toGroup())
                getGroupsByUser(context, forceRefresh = true)
            } else {
                _groupState.value = backendResponse
            }
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

            // Update the view of the list of groups after creating a new group.
            if (backendResponse is GroupState.Success.GroupResponse) {
                getGroupsByUser(context, forceRefresh = true)
            }

            _groupState.value = backendResponse
        }
    }

    // Method to delete a group
    fun deleteGroup(context: Context, groupId: Long) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.deleteGroup(context, groupId)

            if (backendResponse is GroupState.Success.GroupUpdated) {
                getGroupsByUser(context, forceRefresh = true)
            } else {
                _groupState.value = backendResponse
            }
        }
    }

    // Method to leave a group
    fun leaveGroup(context: Context, groupId: Long) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.leaveGroup(context, groupId)

            if (backendResponse is GroupState.Success.GroupUpdated) {
                getGroupsByUser(context, forceRefresh = true)
            } else {
                _groupState.value = backendResponse
            }
        }
    }

    // Method to update a group
    fun updateGroup(context: Context, groupId: Long, fieldChanges: Map<String, Any>) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.updateGroup(context, groupId, fieldChanges)

            // Set the state to the backend response first
            _groupState.value = backendResponse

            // If the update was successful, schedule a background refresh
            if (backendResponse is GroupState.Success.GroupUpdated) {
                // Schedule a background refresh of the group data without affecting the UI state
                viewModelScope.launch {
                    // Small delay to ensure the UI has time to process the success state
                    kotlinx.coroutines.delay(500)
                    getGroupByGroupId(context, groupId)
                }
            }
        }
    }

    fun updateGroupEstimatedPrice(context: Context, groupId: Long, estimatedPrice: Float) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val request = mapOf("estimatedPrice" to estimatedPrice)
            val result = groupRepository.updateGroupEstimatedPrice(context, groupId, request)

            // Set the state to the result first
            _groupState.value = result

            // If the update was successful, schedule a background refresh
            if (result is GroupState.Success.GroupUpdated) {
                // Schedule a background refresh of the group data without affecting the UI state
                viewModelScope.launch {
                    // Small delay to ensure the UI has time to process the success state
                    kotlinx.coroutines.delay(500)
                    getGroupByGroupId(context, groupId)
                }
            }
        }
    }

    fun searchPhotos(context: Context, query: String, page: Int = 1, perPage: Int = 21) {
        _groupState.value = GroupState.Loading
        viewModelScope.launch {
            _groupState.value = photoRepository.searchPhotos(context, query, page, perPage)
        }
    }

    fun searchGroupImages(context: Context, page: Int = 1, perPage: Int = 21) {
        _groupState.value = GroupState.Loading
        viewModelScope.launch {
            _groupState.value = photoRepository.searchGroupImages(context, page, perPage)
        }
    }

    fun setGroupPhoto(context: Context, groupId: Long, photoUrl: String, provider: String = "Unsplash") {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val result = photoRepository.setGroupPhoto(context, groupId, photoUrl, provider)

            _groupState.value = result

            // If the update was successful, schedule a background refresh
            if (result is GroupState.Success.GroupUpdated) {
                // Schedule a background refresh of the group data without affecting the UI state
                viewModelScope.launch {
                    // Small delay to ensure the UI has time to process the success state
                    kotlinx.coroutines.delay(500)
                    getGroupByGroupId(context, groupId)
                }
            }
        }
    }


    fun removeGroupPhoto(context: Context, groupId: Long) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val result = photoRepository.removeGroupPhoto(context, groupId)

            // Set the state to the result first
            _groupState.value = result

            // If the update was successful, schedule a background refresh
            if (result is GroupState.Success.GroupUpdated) {
                // Schedule a background refresh of the group data without affecting the UI state
                viewModelScope.launch {
                    // Small delay to ensure the UI has time to process the success state
                    kotlinx.coroutines.delay(500)
                    getGroupByGroupId(context, groupId)
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

            val backendResponse = groupRepository.updateGroupRegisteredMembers(
                context, groupId, newRegisteredMembers
            )

            // Set the state to the backend response first
            _groupState.value = backendResponse

            // If the update was successful, schedule a background refresh
            if (backendResponse is GroupState.Success.GroupUpdated) {
                // Schedule a background refresh of the group data without affecting the UI state
                viewModelScope.launch {
                    // Small delay to ensure the UI has time to process the success state
                    kotlinx.coroutines.delay(500)
                    getGroupByGroupId(context, groupId)
                }
            }
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

            // Set the state to the backend response first
            _groupState.value = backendResponse

            // If the update was successful, schedule a background refresh
            if (backendResponse is GroupState.Success.GroupUpdated) {
                // Schedule a background refresh of the group data without affecting the UI state
                viewModelScope.launch {
                    // Small delay to ensure the UI has time to process the success state
                    kotlinx.coroutines.delay(500)
                    getGroupByGroupId(context, groupId)
                }
            }
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

    // Method to refresh group in background.
    private fun refreshGroupsSilently(context: Context, userId: Long) {
        viewModelScope.launch {
            val backendResponse = groupRepository.getGroupsByUser(context, userId)

            if (backendResponse is GroupState.Success.GroupsFetched) {
                val newGroups = backendResponse.groupsData

                // Compares group IDs to detect if there are new groups.
                val newGroupIds = newGroups.groups.map { it.groupId }.toSet()
                val cachedIds = cachedGroupsDTO?.groups?.map { it.groupId }?.toSet() ?: emptySet()

                if (newGroupIds != cachedIds) {
                    hasNewGroups.value = true
                }

            }
        }
    }

    // Method to auto-refresh continuously every 5 seconds.
    fun autoRefreshGroups(context: Context) {
        viewModelScope.launch {
            val userId = getCurrentUserId() ?: return@launch

            while (true) {
                refreshGroupsSilently(context, userId)
                kotlinx.coroutines.delay(5_000)
            }
        }
    }
}
