package com.copay.app.dto.unsplash

data class UnsplashResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashPhoto>
)