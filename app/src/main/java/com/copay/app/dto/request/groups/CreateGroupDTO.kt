package com.copay.app.dto.request.groups

/**
 * Data class representing the request body for group creation.
 */
data class CreateGroupDTO(

    val userId: Long,
    val groupName: String,
    val groupDescription: String,
    val estimatedPrice: Float,
    val currency: String,
    val invitedExternalMembers : List<String>,
    val invitedCopayMembers: List<String>
)
