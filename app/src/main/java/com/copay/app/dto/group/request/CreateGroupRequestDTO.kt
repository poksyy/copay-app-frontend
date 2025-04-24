package com.copay.app.dto.group.request

/**
 * Data class representing the request body for group creation.
 */
data class CreateGroupRequestDTO(

    val createdBy: Long,
    val name: String,
    val description: String,
    val estimatedPrice: Float,
    val currency: String,
    val invitedExternalMembers : List<String>,
    val invitedRegisteredMembers: List<String>,
    val imageUrl: String,
    val imageProvider: String
)
