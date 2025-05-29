package com.copay.app.dto.unsplash

data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val urls: UnsplashUrls,
    val user: UnsplashUser
)