package com.copay.app.dto.group.request

import com.copay.app.dto.group.auxiliary.ExternalMemberDTO

/**
 * Data class representing the request body for updating external members in a group.
 */
data class UpdateGroupExternalMembersRequestDTO(
    val invitedExternalMembers : List<ExternalMemberDTO>
)
