package com.copay.app.dto.expense.response

data class GetExpenseResponseDTO(
    val id: Long,
    val totalAmount: Double,
    val groupId: Long,
    val creditorUserId: Long?,
    val creditorExternalMemberId: Long?,
    val registeredMembers: List<RegisteredMemberExpenseDTO>,
    val externalMembers: List<ExternalMemberExpenseDTO>
)