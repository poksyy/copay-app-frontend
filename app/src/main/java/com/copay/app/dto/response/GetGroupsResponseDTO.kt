package com.copay.app.dto.response
import java.time.LocalDateTime

data class GetGroupsResponseDTO(

    val groupId: Long,
    val groupName: String,
    val createdBy: Long,
    val createdAt: LocalDateTime,
    val memberIds: List<Long>
)
