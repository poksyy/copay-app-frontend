package com.copay.app.dto.unsplash.response

data class UnsplashResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashPhoto>
)