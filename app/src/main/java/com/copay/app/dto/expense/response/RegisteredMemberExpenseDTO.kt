package com.copay.app.dto.expense.response

/**
 * Data class representing expense information for registered members.
 */
data class RegisteredMemberExpenseDTO(
    val amount: Double,
    val creditorUserId: Long,
    val debtorUserId: Long
)
