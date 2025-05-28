package com.copay.app.dto.expense.response

/**
 * Data class representing expense information for external members.
 */
data class ExternalMemberExpenseDTO(
    val debtorExternalMemberId: Long,
    val amount: Double,
    val creditorUserId: Long
)
