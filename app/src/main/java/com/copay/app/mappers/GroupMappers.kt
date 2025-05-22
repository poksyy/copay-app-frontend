package com.copay.app.mappers

import com.copay.app.dto.group.response.GroupResponseDTO
import com.copay.app.model.Group

/**
 * Maps a GroupResponseDTO to a domain Group model
 */
fun GroupResponseDTO.toGroup(): Group {
    return Group(
        groupId = this.groupId,
        name = this.name,
        description = this.description,
        estimatedPrice = this.estimatedPrice,
        currency = this.currency,
        createdAt = this.createdAt,
        isOwner = this.userIsOwner,
        ownerId = this.groupOwner.ownerId,
        ownerName = this.groupOwner.ownerName,
        registeredMembers = this.registeredMembers,
        externalMembers = this.externalMembers,
        expenses = emptyList(),
        imageUrl = null,
        imageProvider = null
    )
}
