package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.MessageResponseDTO
import com.copay.app.dto.unsplash.request.PhotoRequestDTO
import com.copay.app.service.PhotoService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.GroupState
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response

class PhotoRepository(private val service: PhotoService) {

    suspend fun searchGroupImages(
        context: Context,
        page: Int = 1,
        perPage: Int = 20
    ): GroupState {
        return try {
            val response = service.searchGroupImages(page = page, perPage = perPage)
            GroupState.Success.PhotoList(response)
        } catch (e: Exception) {
            GroupState.Error(e.message ?: "Error searching group images")
        }
    }

    suspend fun setGroupPhoto(
        context: Context,
        groupId: Long,
        photoUrl: String,
        provider: String = "Unsplash"
    ): GroupState {
        val token = DataStoreManager.getFormattedToken(context)
        val photoRequestDTO = PhotoRequestDTO(imageUrl = photoUrl, imageProvider = provider)

        return handleApiResponse {
            service.setGroupPhoto(groupId, photoRequestDTO, token)
        }
    }

    suspend fun removeGroupPhoto(
        context: Context,
        groupId: Long
    ): GroupState {
        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse {
            service.removeGroupPhoto(groupId, token)
        }
    }

    private suspend fun <T> handleApiResponse(
        apiCall: suspend () -> Response<T>
    ): GroupState {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Log.d("PhotoRepository", "Body class: ${body.javaClass}, content: $body")
                    if (body is MessageResponseDTO) {
                        GroupState.Success.GroupUpdated(body)
                    } else {
                        Log.e("PhotoRepository", "Unexpected response type: ${body.javaClass}")
                        GroupState.Error("Unexpected response type")
                    }
                } ?: GroupState.Error("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("PhotoRepository", "Error Body: $errorBody")
                val message = extractErrorMessage(errorBody)
                GroupState.Error(message ?: "Unknown error")
            }
        } catch (e: Exception) {
            Log.e("PhotoRepository", "Connection error: ${e.message}")
            GroupState.Error("Connection error: ${e.message}")
        }
    }

    private fun extractErrorMessage(errorJson: String?): String? {
        if (errorJson.isNullOrEmpty()) return null
        return try {
            val jsonObject = Gson().fromJson(errorJson, JsonObject::class.java)
            jsonObject.get("message")?.asString
        } catch (e: Exception) {
            Log.e("PhotoRepository", "Error parsing error message: ${e.message}")
            null
        }
    }
}