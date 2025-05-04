package com.copay.app.dto.expense.response

data class RegisteredMemberExpenseDTO(
    val amount: Double,
    val creditorUserId: Long,
    val debtorUserId: Long
)
