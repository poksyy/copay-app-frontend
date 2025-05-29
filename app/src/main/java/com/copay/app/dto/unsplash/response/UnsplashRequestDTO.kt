package com.copay.app.dto.unsplash.response

data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val urls: UnsplashUrls,
    val user: UnsplashUser
)