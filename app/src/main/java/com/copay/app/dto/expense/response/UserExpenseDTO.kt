package com.copay.app.dto.expense.response

data class UserExpenseDTO(
    val userExpenseId: Long,
    val debtorUserId: Long,
    val amount: Float
)