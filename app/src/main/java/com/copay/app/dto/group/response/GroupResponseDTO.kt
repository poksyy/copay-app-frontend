package com.copay.app.dto.group.response

import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.GroupOwnerDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.mappers.toGroup

/**
 * Data class representing detailed information about a group.
 */
data class GroupResponseDTO(
    val groupId: Long,
    val name: String,
    val description: String,
    val estimatedPrice: Float,
    val imageUrl: String,
    val imageProvider: String,
    val currency: String,
    val createdAt: String,
    val userIsOwner: Boolean,
    val groupOwner: GroupOwnerDTO,
    val registeredMembers: List<RegisteredMemberDTO>,
    val externalMembers: List<ExternalMemberDTO>,

    // Message in case of any error in the backend.
    val message: String? = null
)
