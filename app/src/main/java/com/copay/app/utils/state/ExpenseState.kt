package com.copay.app.utils.state

import com.copay.app.dto.expense.response.GetExpenseResponseDTO

sealed class ExpenseState {
    object Idle : ExpenseState()
    object Loading : ExpenseState()

    sealed class Success : ExpenseState() {
        data class ExpensesFetched(val expenses: List<GetExpenseResponseDTO>) : Success()
    }

    data class Error(val message: String) : ExpenseState()
}