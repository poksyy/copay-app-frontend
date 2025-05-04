package com.copay.app.dto.expense.response

data class ExternalMemberExpenseDTO(
    val debtorExternalMemberId: Long,
    val amount: Double,
    val creditorUserId: Long
)