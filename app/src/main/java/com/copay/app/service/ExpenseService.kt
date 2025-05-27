package com.copay.app.service;

import com.copay.app.config.ApiService;
import com.copay.app.dto.expense.request.GetExpenseRequestDTO
import com.copay.app.dto.expense.response.GetExpenseResponseDTO;
import com.copay.app.dto.expense.response.TotalDebtResponseDTO
import com.copay.app.dto.expense.response.TotalSpentResponseDTO
import com.copay.app.dto.expense.response.UserExpenseDTO
import com.copay.app.dto.paymentconfirmation.response.PaymentResponseDTO
import retrofit2.Response

/**
 * ExpenseService handles all operations related to retrieving expense data
 * from the backend. It serves as an abstraction layer between the ViewModel
 * and the API service.
 */

class ExpenseService(private val api:ApiService)  {

    // Retrieves a list of expenses associated with the specified group ID.
    suspend fun getExpenses(
        request: GetExpenseRequestDTO, token: String
    ): Response<List<GetExpenseResponseDTO>> {
        return api.getExpenses(request.groupId, token)
    }

    // Get all user expenses by group ID
    suspend fun getAllUserExpensesByGroup(
        groupId: Long, token: String
    ): Response<List<UserExpenseDTO>> {
        return api.getUserExpensesByGroupId(groupId, token)
    }

    // Get total debt of a user across all groups
    suspend fun getTotalUserDebt(
        userId: Long,
        token: String
    ): Response<TotalDebtResponseDTO> {
        return api.getTotalUserDebt(userId, token)
    }

    // Get total amount spent by a user across all groups
    suspend fun getTotalUserSpent(
        userId: Long,
        token: String
    ): Response<TotalSpentResponseDTO> {
        return api.getTotalUserSpent(userId, token)
    }
}