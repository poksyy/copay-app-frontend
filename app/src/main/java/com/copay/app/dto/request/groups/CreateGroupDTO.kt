package com.copay.app.dto.request.groups

data class CreateGroupDTO(

    val createdBy: Long,
    val groupName: String,
    val groupDescription: String?,
    val estimatedPrice: Float,
    val currency: String,
    val invitedMembers: List<String>
)
