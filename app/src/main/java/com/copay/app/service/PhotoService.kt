package com.copay.app.service

import com.copay.app.config.ApiService
import com.copay.app.dto.unsplash.UnsplashResponse

/**
 * PhotoServices handles all profile-image related.
 * It serves as a clean interface between the ViewModel and ApiService.
 */

class PhotoService(private val api: ApiService) {

    // Search through Unsplash images.
    suspend fun searchImage(
        query: String,
        page: Int = 1,
        perPage: Int = 20
    ): UnsplashResponse {
        return api.searchPhotos(
            query = query,
            page = page,
            perPage = perPage
        )
    }

    suspend fun searchGroupImages(
        query: String = "group friends",
        page: Int = 1,
        perPage: Int = 20
    ): UnsplashResponse {
        return api.searchPhotos(
            query = query,
            page = page,
            perPage = perPage
        )
    }
}