package com.copay.app.dto.group.request

import com.copay.app.dto.group.auxiliary.ExternalMemberDTO

data class UpdateGroupExternalMembersRequestDTO(
    val invitedExternalMembers : List<ExternalMemberDTO>
)
