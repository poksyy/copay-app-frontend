package com.copay.app.service

import com.copay.app.config.ApiService
import com.copay.app.dto.MessageResponseDTO
import com.copay.app.dto.group.request.CreateGroupRequestDTO
import com.copay.app.dto.group.request.GetGroupRequestDTO
import com.copay.app.dto.group.request.UpdateGroupExternalMembersRequestDTO
import com.copay.app.dto.group.request.UpdateGroupRegisteredMembersRequestDTO
import com.copay.app.dto.group.response.GroupResponseDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import retrofit2.Response

/**
 * GroupService manages all operations related to groups, including
 * creating, retrieving, updating, and deleting groups, as well as
 * managing group membership for registered and external users.
 */

class GroupService(private val api: ApiService) {

    // Fetches all groups associated with a specific user ID.
    suspend fun getGroupsByUser(request: GetGroupRequestDTO, token: String): Response<GetGroupResponseDTO> {
        return api.getGroupsByUser(request.userId, token)
    }

    // Fetch a single group with a specific group ID.
    suspend fun getGroupsByGroupId(groupId: Long, token: String): Response<GroupResponseDTO> {
        return  api.getGroupByGroupId(groupId, token)
    }

    // Sends a request to create a new group with the provided details.
    suspend fun createGroup(request: CreateGroupRequestDTO, token: String): Response<GroupResponseDTO> {
        return api.createGroup(request, token)
    }

    // Deletes a group by its ID using the user's authentication token.
    suspend fun deleteGroup(groupId: Long, token: String): Response<MessageResponseDTO> {
        return api.deleteGroup(groupId, token)
    }

    // Removes the authenticated user from the specified group.
    suspend fun leaveGroup(groupId: Long, token: String): Response<MessageResponseDTO> {
        return api.leaveGroup(groupId, token)
    }

    // Updates group details using a map of field changes.
    suspend fun updateGroup(
        groupId: Long, fieldChanges: Map<String, Any>, token: String
    ): Response<MessageResponseDTO> {
        return api.updateGroup(groupId, fieldChanges, token)
    }

    // Updates the estimated price in a group.
    suspend fun updateGroupEstimatedPrice(
        groupId: Long,
        request: Map<String, Float>,
        token: String
    ): Response<MessageResponseDTO> {
        return api.updateGroupEstimatedPrice(groupId, request, token)
    }

    // Updates the list of registered members in a group.
    suspend fun updateGroupRegisteredMembers(
        groupId: Long, request: UpdateGroupRegisteredMembersRequestDTO, token: String
    ): Response<MessageResponseDTO> {
        return api.updateGroupRegisteredMembers(groupId, request, token)
    }

    // Updates the list of external members in a group.
    suspend fun updateGroupExternalMembers(
        groupId: Long, request: UpdateGroupExternalMembersRequestDTO, token: String
    ): Response<MessageResponseDTO> {
        return api.updateGroupExternalMembers(groupId, request, token)
    }
}