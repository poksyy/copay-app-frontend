package com.copay.app.service

import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.group.request.CreateGroupRequestDTO
import com.copay.app.dto.group.request.GetGroupRequestDTO
import com.copay.app.dto.group.request.UpdateGroupExternalMembersRequestDTO
import com.copay.app.dto.group.request.UpdateGroupRegisteredMembersRequestDTO
import com.copay.app.dto.group.response.CreateGroupResponseDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import com.copay.app.dto.group.response.GroupMessageResponseDTO
import com.copay.app.model.Group
import com.copay.app.utils.state.GroupState
import com.google.gson.Gson
import retrofit2.Response

class GroupService(private val api: ApiService) {

    suspend fun getGroupsByUser(request: GetGroupRequestDTO): Response<GetGroupResponseDTO> {
        return api.getGroupsByUser(request.userId)
    }

    suspend fun createGroup(request: CreateGroupRequestDTO): Response<CreateGroupResponseDTO> {
        return api.createGroup(request)
    }

    suspend fun deleteGroup(groupId: Long, token: String): Response<GroupMessageResponseDTO> {
        return api.deleteGroup(groupId, "Bearer $token")
    }

    suspend fun leaveGroup(groupId: Long, token: String): Response<GroupMessageResponseDTO> {
        return api.leaveGroup(groupId, "Bearer $token")
    }

    suspend fun updateGroup(
        groupId: Long, fieldChanges: Map<String, Any>
    ): Response<GroupMessageResponseDTO> {
        return api.updateGroup(groupId, fieldChanges)
    }

    suspend fun updateGroupRegisteredMembers(
        groupId: Long, request: UpdateGroupRegisteredMembersRequestDTO
    ): Response<GroupMessageResponseDTO> {
        return api.updateGroupRegisteredMembers(groupId, request)
    }

    suspend fun updateGroupExternalMembers(
        groupId: Long, request: UpdateGroupExternalMembersRequestDTO
    ): Response<GroupMessageResponseDTO> {
        return api.updateGroupExternalMembers(groupId, request)
    }

    private val gson = Gson()

    // Extract groups from GroupsFetched state
    fun extractGroups(response: GroupState.Success.GroupsFetched): List<Group> {
        Log.d("GroupService", "Backend Groups Response: ${gson.toJson(response)}")

        return response.groupsData.groups.map { groupDto ->
            extractSingleGroup(groupDto).also {
                Log.d(
                    "GroupService",
                    "Processed group - ID: ${it.groupId}, " + "Name: ${it.name}, Owner: ${it.ownerName}"
                )
            }
        }
    }

    // TODO Extract groups from GroupsCreated state
    fun extractCreateGroup(response: GroupState.Success.GroupCreated): Group {
        Log.d("GroupService", "Backend Group Created: ${gson.toJson(response)}")

        return extractSingleGroup(response.creationData).also {
            Log.d(
                "GroupService",
                "Created group - ID: ${it.groupId}, " + "Name: ${it.name}, Members: ${(it.registeredMembers?.size ?: 0) + (it.externalMembers?.size ?: 0)}"
            )
        }
    }
    private fun extractSingleGroup(dto: CreateGroupResponseDTO): Group {
        Log.d(
            "GroupService",
            "Processing group DTO: " + "ID=${dto.groupId}, Name=${dto.name}, " + "Owner=${dto.groupOwner.ownerName}"
        )

        return Group(
            groupId = dto.groupId,
            name = dto.name,
            description = dto.description,
            estimatedPrice = dto.estimatedPrice,
            currency = dto.currency,
            createdAt = dto.createdAt,
            isOwner = dto.userIsOwner,
            ownerId = dto.groupOwner.ownerId,
            ownerName = dto.groupOwner.ownerName,
            registeredMembers = dto.registeredMembers,
            externalMembers = dto.externalMembers,
            expenses = emptyList(),
            imageUrl = null,
            imageProvider = null
        )
    }

    // TODO Find an optimized version if possible
    fun extractAnyGroup(response: GroupState.Success): Group? {
        Log.d("GroupService", "Backend Response: ${gson.toJson(response)}")

        return when (response) {
            is GroupState.Success.GroupsFetched -> {
                Log.w("GroupService", "GroupsFetched contains multiple groups, using first")
                response.groupsData.groups.firstOrNull()?.let { extractSingleGroup(it) }
            }

            is GroupState.Success.GroupCreated -> extractSingleGroup(response.creationData)
            is GroupState.Success.GroupUpdated -> null
            is GroupState.Success.GroupDeleted -> null
            is GroupState.Success.GroupMemberLeft -> null
            is GroupState.Success.GroupMembersUpdated -> null
        }.also {
            if (it == null) Log.d(
                "GroupService", "No group data to extract from ${response::class.simpleName}"
            )
        }
    }
}