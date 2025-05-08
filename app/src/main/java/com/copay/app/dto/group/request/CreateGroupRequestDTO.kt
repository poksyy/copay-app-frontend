package com.copay.app.dto.group.request

import com.copay.app.dto.group.auxiliary.InvitedExternalMemberDTO
import com.copay.app.dto.group.auxiliary.InvitedRegisteredMemberDTO

/**
 * Data class representing the request body for group creation.
 */
data class CreateGroupRequestDTO(

    val createdBy: Long,
    val name: String,
    val description: String,
    val estimatedPrice: Float,
    val currency: String,
    val invitedExternalMembers : List<InvitedExternalMemberDTO>,
    val invitedRegisteredMembers: List<InvitedRegisteredMemberDTO>,
    val imageUrl: String,
    val imageProvider: String
)
