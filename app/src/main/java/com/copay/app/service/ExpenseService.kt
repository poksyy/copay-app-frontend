package com.copay.app.service;

import com.copay.app.config.ApiService;
import com.copay.app.dto.expense.request.GetExpenseRequestDTO
import com.copay.app.dto.expense.response.GetExpenseResponseDTO;
import retrofit2.Response

class ExpenseService(private val api:ApiService)  {

    suspend fun getExpenses(request: GetExpenseRequestDTO): Response<List<GetExpenseResponseDTO>>
    {
        return api.getExpenses(request.groupId)
    }
}