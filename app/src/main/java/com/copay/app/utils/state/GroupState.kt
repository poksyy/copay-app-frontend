package com.copay.app.utils.state

import com.copay.app.dto.group.response.CreateGroupResponseDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import com.copay.app.dto.group.response.GroupMessageResponseDTO
import com.copay.app.mappers.toGroup
import com.copay.app.model.Group

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
        data class GroupsFetched(val groupsData: GetGroupResponseDTO) : Success() {
            // Converts DTOs to domain models.
            val groups: List<Group>
                get() = groupsData.groups.map { it.toGroup() }
        }
        data class GroupCreated(val creationData: CreateGroupResponseDTO) : Success()
        data class GroupUpdated(val updateData: GroupMessageResponseDTO) : Success()
    }

    // An error occurred during group operations.
    data class Error(val message: String) : GroupState()
}