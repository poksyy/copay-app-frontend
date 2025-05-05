package com.copay.app.service;

import com.copay.app.config.ApiService;
import com.copay.app.dto.expense.request.GetExpenseRequestDTO
import com.copay.app.dto.expense.response.GetExpenseResponseDTO;
import retrofit2.Response

/**
 * ExpenseService handles all operations related to retrieving expense data
 * from the backend. It serves as an abstraction layer between the ViewModel
 * and the API service.
 */

class ExpenseService(private val api:ApiService)  {

    // Retrieves a list of expenses associated with the specified group ID.
    suspend fun getExpenses(request: GetExpenseRequestDTO): Response<List<GetExpenseResponseDTO>> {
        return api.getExpenses(request.groupId)
    }
}