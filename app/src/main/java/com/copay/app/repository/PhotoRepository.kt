package com.copay.app.repository

import android.content.Context
import com.copay.app.service.PhotoService
import com.copay.app.utils.state.PhotoState

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
            PhotoState.Success(response)
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
            PhotoState.Success(response)
        } catch (e: Exception) {
            PhotoState.Error(e.message ?: "Error searching group images")
        }
    }
}