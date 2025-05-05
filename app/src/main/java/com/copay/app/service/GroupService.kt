package com.copay.app.service

import com.copay.app.config.ApiService
import com.copay.app.dto.group.request.CreateGroupRequestDTO
import com.copay.app.dto.group.request.GetGroupRequestDTO
import com.copay.app.dto.group.request.UpdateGroupExternalMembersRequestDTO
import com.copay.app.dto.group.request.UpdateGroupRegisteredMembersRequestDTO
import com.copay.app.dto.group.response.CreateGroupResponseDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import com.copay.app.dto.group.response.GroupMessageResponseDTO
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
}