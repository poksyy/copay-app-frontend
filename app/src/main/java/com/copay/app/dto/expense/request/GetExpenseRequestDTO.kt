package com.copay.app.dto.expense.request

/**
 * Data class representing the request body for getting expenses by group ID.
 */
data class GetExpenseRequestDTO(
    val groupId: Long
)
