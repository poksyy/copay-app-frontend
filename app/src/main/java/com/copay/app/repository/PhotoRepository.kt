package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.MessageResponseDTO
import com.copay.app.dto.unsplash.request.PhotoRequestDTO
import com.copay.app.service.PhotoService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.PhotoState
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response

class PhotoRepository(private val service: PhotoService) {

    suspend fun searchPhotos(
        context: Context,
        query: String,
        page: Int = 1,
        perPage: Int = 20
    ): PhotoState {
        return try {
            val response = service.searchImage(
                query = query,
                page = page,
                perPage = perPage
            )
            PhotoState.Success.PhotoList(response)
        } catch (e: Exception) {
            PhotoState.Error(e.message ?: "Error searching photos")
        }
    }

    suspend fun searchGroupImages(
        context: Context,
        page: Int = 1,
        perPage: Int = 20
    ): PhotoState {
        return try {
            val response = service.searchGroupImages(
                page = page,
                perPage = perPage
            )
            PhotoState.Success.PhotoList(response)
        } catch (e: Exception) {
            PhotoState.Error(e.message ?: "Error searching group images")
        }
    }

    suspend fun setGroupPhoto(
        context: Context,
        groupId: Long,
        photoUrl: String,
        provider: String = "Unsplash"
    ): PhotoState {
        val token = DataStoreManager.getFormattedToken(context)
        val photoRequestDTO = PhotoRequestDTO(imageUrl = photoUrl, imageProvider = provider)

        return handleApiResponse {
            service.setGroupPhoto(groupId, photoRequestDTO, token)
        }
    }

    suspend fun removeGroupPhoto(
        context: Context,
        groupId: Long
    ): PhotoState {
        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse {
            service.removeGroupPhoto(groupId, token)
        }
    }

    private suspend fun <T> handleApiResponse(
        apiCall: suspend () -> Response<T>
    ): PhotoState {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    when (body) {
                        is MessageResponseDTO -> {
                            if (response.raw().request.method == "DELETE") {
                                PhotoState.Success.PhotoRemoved(body.message)
                            } else {
                                PhotoState.Success.PhotoSet(body.message)
                            }
                        }
                        else -> PhotoState.Error("Unexpected response type")
                    }
                } ?: PhotoState.Error("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("PhotoRepository", "Error Body: $errorBody")
                val message = extractErrorMessage(errorBody)
                PhotoState.Error(message ?: "Unknown error")
            }
        } catch (e: Exception) {
            Log.e("PhotoRepository", "Connection error: ${e.message}")
            PhotoState.Error("Connection error: ${e.message}")
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
