package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.group.request.CreateGroupRequestDTO
import com.copay.app.dto.group.request.GetGroupRequestDTO
import com.copay.app.dto.group.request.UpdateGroupExternalMembersRequestDTO
import com.copay.app.dto.group.request.UpdateGroupRegisteredMembersRequestDTO
import com.copay.app.dto.group.response.CreateGroupResponseDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import com.copay.app.dto.group.response.GroupMessageResponseDTO
import com.copay.app.service.GroupService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.GroupState
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import retrofit2.Response

class GroupRepository(private val groupService: GroupService) {

    suspend fun getGroupsByUser(
        context: Context,
        userId: Long
    ): GroupState {
        val request = GetGroupRequestDTO(userId = userId)

        return handleApiResponse(context) { groupService.getGroupsByUser(request) }
    }

    suspend fun createGroup(
        context: Context,
        createdBy: Long,
        name: String,
        description: String,
        estimatedPrice: Float,
        currency: String,
        invitedExternalMembers: List<String>,
        invitedRegisteredMembers: List<String>,
        imageUrl: String,
        imageProvider: String
    ): GroupState {
        val request = CreateGroupRequestDTO(
            createdBy = createdBy,
            name = name,
            description = description,
            estimatedPrice = estimatedPrice,
            currency = currency,
            invitedExternalMembers = invitedExternalMembers,
            invitedRegisteredMembers = invitedRegisteredMembers,
            imageUrl = imageUrl,
            imageProvider = imageProvider
        )

        return handleApiResponse(context) {
            groupService.createGroup(request)
        }
    }

    suspend fun deleteGroup(
        context: Context, groupId: Long
    ): GroupState {
        val token = DataStoreManager.getToken(context).first()
        if (token.isNullOrEmpty()) {
            return GroupState.Error("Authentication token not found")
        }

        val formattedToken = "Bearer $token"

        return handleApiResponse(context) {
            groupService.deleteGroup(groupId, formattedToken)
        }
    }

    suspend fun leaveGroup(
        context: Context, groupId: Long
    ): GroupState {
        val token = DataStoreManager.getToken(context).first()
        if (token.isNullOrEmpty()) {
            return GroupState.Error("Authentication token not found")
        }

        val formattedToken = "Bearer $token"

        return handleApiResponse(context) {
            groupService.leaveGroup(groupId, formattedToken)
        }
    }

    suspend fun updateGroup(
        context: Context,
        groupId: Long,
        fieldChanges: Map<String, @JvmSuppressWildcards Any>
    ): GroupState {

        return handleApiResponse(context) {
            groupService.updateGroup(groupId, fieldChanges)
        }
    }

    suspend fun updateGroupRegisteredMembers(
        context: Context, groupId: Long, invitedRegisteredMembers: List<String>
    ): GroupState {
        val request = UpdateGroupRegisteredMembersRequestDTO(
            invitedRegisteredMembers = invitedRegisteredMembers
        )

        return handleApiResponse(context) {
            groupService.updateGroupRegisteredMembers(groupId, request)
        }
    }

    suspend fun updateGroupExternalMembers(
        context: Context, groupId: Long, invitedExternalMembers: List<String>
    ): GroupState {
        val request = UpdateGroupExternalMembersRequestDTO(
            invitedExternalMembers = invitedExternalMembers
        )

        return handleApiResponse(context) {
            groupService.updateGroupExternalMembers(groupId, request)
        }
    }

    private suspend fun <T> handleApiResponse(
        context: Context, apiCall: suspend () -> Response<T>
    ): GroupState {
        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    when (body) {
                        is GetGroupResponseDTO -> GroupState.Success.GroupsFetched(body)
                        is CreateGroupResponseDTO -> GroupState.Success.GroupCreated(body)
                        is GroupMessageResponseDTO -> GroupState.Success.GroupUpdated(body)
                        else -> GroupState.Error("Unexpected response type")
                    }
                } ?: GroupState.Error("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val message = extractErrorMessage(errorBody)
                GroupState.Error(message ?: "Unknown error")
            }
        } catch (e: Exception) {
            Log.e("GroupRepository", "Connection error: ${e.message}")
            GroupState.Error("Connection error: ${e.message}")
        }
    }

    private fun extractErrorMessage(errorJson: String?): String? {
        if (errorJson.isNullOrEmpty()) return null

        return try {
            val jsonObject = Gson().fromJson(errorJson, JsonObject::class.java)
            jsonObject.get("message")?.asString
        } catch (e: Exception) {
            Log.e("GroupRepository", "Error parsing error message: ${e.message}")
            null
        }
    }
}