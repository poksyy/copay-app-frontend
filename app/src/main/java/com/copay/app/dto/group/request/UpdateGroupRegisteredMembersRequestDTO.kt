package com.copay.app.dto.group.request

/**
 * Data class representing the request body for updating registered members in a group.
 */
data class UpdateGroupRegisteredMembersRequestDTO(
    val invitedRegisteredMembers: List<String>
)
