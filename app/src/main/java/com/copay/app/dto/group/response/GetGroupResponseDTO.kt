package com.copay.app.dto.group.response

import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.GroupOwnerDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO

data class GetGroupResponseDTO(
    val groups: List<CreateGroupResponseDTO>
)