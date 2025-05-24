package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.expense.response.GetExpenseResponseDTO
import com.copay.app.dto.expense.response.UserExpenseDTO
import com.copay.app.repository.ExpenseRepository
import com.copay.app.utils.state.ExpenseState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
) : ViewModel() {

    // General expense state.
    private val _expenseState = MutableStateFlow<ExpenseState>(ExpenseState.Idle)
    val expenseState: StateFlow<ExpenseState> = _expenseState

    // List of group expenses (expenses)
    private val _expenses = MutableStateFlow<List<GetExpenseResponseDTO>>(emptyList())
    val expenses: StateFlow<List<GetExpenseResponseDTO>> = _expenses

    // List of expenses per user (userExpenses)
    private val _userExpenses = MutableStateFlow<List<UserExpenseDTO>>(emptyList())
    val userExpenses: StateFlow<List<UserExpenseDTO>> = _userExpenses

    fun getExpensesByGroup(context: Context, groupId: Long) {
        viewModelScope.launch {
            _expenseState.value = ExpenseState.Loading

            val backendResponse = expenseRepository.getExpenses(context, groupId)

            _expenseState.value = backendResponse

            if (backendResponse is ExpenseState.Success.ExpensesFetched) {
                _expenses.value = backendResponse.expenses
                _expenseState.value = backendResponse
            }
        }
    }

    fun getAllUserExpensesByGroup(context: Context, groupId: Long) {
        viewModelScope.launch {
            _expenseState.value = ExpenseState.Loading

            val backendResponse = expenseRepository.getAllUserExpensesByGroup(context, groupId)

            if (backendResponse.isSuccessful) {
                val userExpenses = backendResponse.body() ?: emptyList()
                _userExpenses.value = userExpenses
                _expenseState.value = ExpenseState.Success.ExpenseMembersIds(userExpenses)
            }
        }
    }

    fun resetState() {
        _expenseState.value = ExpenseState.Idle
    }
}