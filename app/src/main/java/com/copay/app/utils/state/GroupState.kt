package com.copay.app.utils.state

import com.copay.app.dto.group.response.CreateGroupResponseDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import com.copay.app.dto.group.response.GroupMessageResponseDTO
import com.google.gson.annotations.Expose

/**
 * Different group states for UI handling
 */
sealed class GroupState {

    // Initial state when no action is performed.
    data object Idle : GroupState()

    // Loading state while fetching/updating groups.
    data object Loading : GroupState()

    // Group operations completed successfully
    sealed class Success : GroupState() {
        data class GroupsFetched(val groupsData: GetGroupResponseDTO) : Success()
        data class GroupCreated(val creationData: CreateGroupResponseDTO) : Success()
        data class GroupUpdated(val updateData: GroupMessageResponseDTO) : Success()
        data class GroupDeleted(val deletionData: GroupMessageResponseDTO) : Success()
        data class GroupMemberLeft(val leaveData: GroupMessageResponseDTO) : Success()
        data class GroupMembersUpdated(val membersData: GroupMessageResponseDTO) : Success()
    }

    // An error occurred during group operations.
    data class Error(val message: String) : GroupState()
}