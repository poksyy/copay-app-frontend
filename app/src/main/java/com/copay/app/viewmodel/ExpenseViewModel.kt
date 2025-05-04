package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.expense.response.GetExpenseResponseDTO
import com.copay.app.repository.ExpenseRepository
import com.copay.app.utils.state.ExpenseState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _expenseState = MutableStateFlow<ExpenseState>(ExpenseState.Idle)
    val expenseState: StateFlow<ExpenseState> = _expenseState

    private val _expenses = MutableStateFlow<List<GetExpenseResponseDTO>>(emptyList())
    val expenses: StateFlow<List<GetExpenseResponseDTO>> = _expenses

    fun getExpensesByGroup(context: Context, groupId: Long) {
        viewModelScope.launch {
            _expenseState.value = ExpenseState.Loading

            val result = expenseRepository.getExpenses(context, groupId)

            _expenseState.value = result

            if (result is ExpenseState.Success.ExpensesFetched) {
                _expenses.value = result.expenses
            }
        }
    }

    fun resetState() {
        _expenseState.value = ExpenseState.Idle
    }
}