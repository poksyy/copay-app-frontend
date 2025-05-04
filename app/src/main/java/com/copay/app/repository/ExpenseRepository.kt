package com.copay.app.repository

import android.content.Context
import com.copay.app.config.ApiService
import com.copay.app.utils.state.ExpenseState
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenseRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getExpenses(context: Context, groupId: Long): ExpenseState {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getExpenses(groupId)
                if (response.isSuccessful && response.body() != null) {
                    ExpenseState.Success.ExpensesFetched(response.body()!!)
                } else {
                    ExpenseState.Error("Failed to fetch expenses: ${response.message()}")
                }
            } catch (e: Exception) {
                ExpenseState.Error("Error fetching expenses: ${e.message}")
            }
        }
    }
}