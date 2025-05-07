package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
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
import retrofit2.Response

/**
 * GroupRepository is responsible for managing group operations by interacting with the GroupService.
 * It handles group creation, deletion, updates, and member management. This class encapsulates all
 * logic related to groups and returns UI-ready GroupState responses.
 */

class GroupRepository(private val groupService: GroupService) {

    // Fetches all groups associated with a specific user.
    suspend fun getGroupsByUser(
        context: Context, userId: Long
    ): GroupState {
        val request = GetGroupRequestDTO(userId = userId)

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) { groupService.getGroupsByUser(request, token) }
    }

    // Creates a new group with specified details and invited members.
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

            val token = DataStoreManager.getFormattedToken(context)

            groupService.createGroup(request, token)
        }
    }

    // Deletes a group by its ID using an authenticated request.
    suspend fun deleteGroup(
        context: Context, groupId: Long
    ): GroupState {

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            groupService.deleteGroup(groupId, token)
        }
    }

    // Leaves a group by its ID using an authenticated request.
    suspend fun leaveGroup(
        context: Context, groupId: Long
    ): GroupState {

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            groupService.leaveGroup(groupId, token)
        }
    }

    // Updates specific fields of a group.
    suspend fun updateGroup(
        context: Context, groupId: Long, fieldChanges: Map<String, @JvmSuppressWildcards Any>
    ): GroupState {

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            groupService.updateGroup(groupId, fieldChanges, token)
        }
    }

    // Updates the list of registered members in a group.
    suspend fun updateGroupRegisteredMembers(
        context: Context, groupId: Long, invitedRegisteredMembers: List<String>
    ): GroupState {

        val request = UpdateGroupRegisteredMembersRequestDTO(
            invitedRegisteredMembers = invitedRegisteredMembers
        )

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            groupService.updateGroupRegisteredMembers(groupId, request, token)
        }
    }

    // Updates the list of external members in a group.
    suspend fun updateGroupExternalMembers(
        context: Context,
        groupId: Long,
        invitedExternalMembers: List<ExternalMemberDTO>
    ): GroupState {

        val request = UpdateGroupExternalMembersRequestDTO(
            invitedExternalMembers = invitedExternalMembers
        )

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) {
            groupService.updateGroupExternalMembers(groupId, request, token)
        }
    }

    // Handles the API response for all group-related operations.
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

    // Extracts error message from JSON error response.
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