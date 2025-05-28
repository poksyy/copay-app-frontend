package com.copay.app.dto.expense.response

/**
 * Data class representing the response received from getting expenses by group ID.
 */
data class GetExpenseResponseDTO(
    val id: Long,
    val totalAmount: Double,
    val groupId: Long,
    val creditorUserId: Long?,
    val creditorExternalMemberId: Long?,
    val registeredMembers: List<RegisteredMemberExpenseDTO>,
    val externalMembers: List<ExternalMemberExpenseDTO>
)
