package com.copay.app.utils

import com.copay.app.dto.group.response.CreateGroupResponseDTO
import com.copay.app.model.Group

/**
 * Utility class to map between DTOs and domain models
 */
object GroupMapper {

    /**
     * Maps a GroupResponseDTO to a domain Group model
     */
    fun toGroup(dto: CreateGroupResponseDTO): Group {
        return Group(
            groupId = dto.groupId,
            name = dto.name,
            description = dto.description,
            estimatedPrice = dto.estimatedPrice,
            currency = dto.currency,
            createdAt = dto.createdAt,
            isOwner = dto.userIsOwner,
            ownerId = dto.groupOwner.ownerId,
            ownerName = dto.groupOwner.ownerName,
            registeredMembers = dto.registeredMembers,
            externalMembers = dto.externalMembers,
            expenses = emptyList(),
            imageUrl = null,
            imageProvider = null
        )
    }
}