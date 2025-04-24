package com.copay.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.model.Group
import com.copay.app.model.User
import com.copay.app.repository.GroupRepository
import com.copay.app.service.GroupService
import com.copay.app.utils.session.GroupSession
import com.copay.app.utils.session.UserSession
import com.copay.app.utils.state.GroupState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    val group: StateFlow<Group?> = groupSession.group

    // Resets the UI state to idle.
    fun resetGroupState() {
        _groupState.value = GroupState.Idle
    }

    fun selectGroup(group: Group) {
        groupSession.setGroup(
            group.groupId,
            group.name,
            group.description,
            group.currency,
            group.registeredMembers,
            group.externalMembers
        )
    }

    // Fetches groups for the current user.
    fun getGroupsByUser(context: Context) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val userId = userSession.user.value?.userId ?: run {
                _groupState.value = GroupState.Error("User not logged in")
                return@launch
            }

            Log.d("GroupViewModel", "hola + ${userId}")

            when (val backendResponse = groupRepository.getGroupsByUser(context, userId)) {
                is GroupState.Success.GroupsFetched -> {

                    val groups = groupService.extractGroups(backendResponse)

                    groups.forEach { group ->
                        groupSession.setGroup(
                            group.groupId,
                            group.name,
                            group.description,
                            group.currency,
                            group.registeredMembers,
                            group.externalMembers
                        )
                    }
                    _groupState.value = backendResponse
                }
                is GroupState.Error -> {
                    _groupState.value = backendResponse
                }
                else -> {
                    _groupState.value = GroupState.Error("Unexpected response type")
                }
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
        invitedExternalMembers: List<String>,
        invitedRegisteredMembers: List<String>,
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
    fun deleteGroup(context: Context, groupId: Long) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.deleteGroup(context, groupId)

            _groupState.value = backendResponse
        }
    }

    // Method to leave a group
    fun leaveGroup(context: Context, groupId: Long) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.leaveGroup(context, groupId)

            _groupState.value = backendResponse
        }
    }

    // Method to update a group
    fun updateGroup(context: Context, groupId: Long, fieldChanges: Map<String, Any>) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.updateGroup(context, groupId, fieldChanges)

            _groupState.value = backendResponse
        }
    }

    // Method to update registered members of a group
    fun updateGroupRegisteredMembers(
        context: Context,
        groupId: Long,
        invitedRegisteredMembers: List<String>
    ) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.updateGroupRegisteredMembers(
                context,
                groupId,
                invitedRegisteredMembers
            )

            _groupState.value = backendResponse
        }
    }

    // Method to update external members of a group
    fun updateGroupExternalMembers(
        context: Context,
        groupId: Long,
        invitedExternalMembers: List<String>
    ) {
        viewModelScope.launch {
            _groupState.value = GroupState.Loading

            val backendResponse = groupRepository.updateGroupExternalMembers(
                context,
                groupId,
                invitedExternalMembers
            )

            _groupState.value = backendResponse
        }
    }
}
