package com.copay.app.dto.group.response

/**
 * Data class representing the response containing a list of groups.
 */
data class GetGroupResponseDTO(
    val groups: List<GroupResponseDTO>
)
