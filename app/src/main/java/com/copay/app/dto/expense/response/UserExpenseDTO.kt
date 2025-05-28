package com.copay.app.dto.expense.response

/**
 * Data class representing expense information for a specific user.
 */
data class UserExpenseDTO(
    val userExpenseId: Long,
    val debtorUserId: Long,
    val debtorExternalId: Long,
    val amount: Float
)
