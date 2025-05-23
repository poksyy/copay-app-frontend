package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.expense.request.GetExpenseRequestDTO
import com.copay.app.dto.expense.response.GetExpenseResponseDTO
import com.copay.app.dto.expense.response.UserExpenseDTO
import com.copay.app.dto.paymentconfirmation.response.ListUnconfirmedPaymentConfirmationResponseDTO
import com.copay.app.dto.paymentconfirmation.response.PaymentResponseDTO
import com.copay.app.service.ExpenseService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.ExpenseState
import com.copay.app.utils.state.PaymentState
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response

/**
 * ExpenseRepository is responsible for managing expense-related operations by interacting with the ExpenseService.
 * It makes API calls to fetch expenses and handles responses by processing success and error cases.
 * This class encapsulates the logic of managing expenses, ensuring that the UI receives the appropriate state.
 */

class ExpenseRepository(private val expenseService: ExpenseService) {

    // Fetches the list of expenses for a given group.
    suspend fun getExpenses(
        context: Context,
        groupId: Long
    ): ExpenseState {

        val request = GetExpenseRequestDTO(groupId = groupId)

        val token = DataStoreManager.getFormattedToken(context)

        return handleApiResponse(context) { expenseService.getExpenses(request, token) }
    }

    suspend fun getAllUserExpensesByGroup(context: Context, groupId: Long): Response<List<UserExpenseDTO>> {
        val token = DataStoreManager.getFormattedToken(context)
        return expenseService.getAllUserExpensesByGroup(groupId, token)
    }

    // Handles the API response for expense operations.
    private suspend fun <T> handleApiResponse(
        context: Context,
        apiCall: suspend () -> Response<T>
    ): ExpenseState {
        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()

                when (body) {
                    is List<*> -> when {
                        body.all { it is UserExpenseDTO } -> {
                            ExpenseState.Success.ExpenseMembersIds(
                                body.filterIsInstance<UserExpenseDTO>()
                            )
                        }

                        body.all { it is GetExpenseResponseDTO } -> {
                            ExpenseState.Success.ExpensesFetched(
                                body.filterIsInstance<GetExpenseResponseDTO>()
                            )
                        }

                        else -> ExpenseState.Error("Unsupported list response type")
                    }

                    else -> ExpenseState.Error("Unexpected response type")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val message = extractErrorMessage(errorBody) ?: run {
                    "Error ${response.code()}: ${response.message()}"
                }
                ExpenseState.Error(message)
            }
        } catch (e: Exception) {
            Log.e("ExpenseRepository", "Connection error: ${e.message}")
            ExpenseState.Error("Connection error: ${e.message}")
        }
    }

    private fun extractErrorMessage(errorJson: String?): String? {
        if (errorJson.isNullOrEmpty()) return null

        return try {
            val jsonObject = Gson().fromJson(errorJson, JsonObject::class.java)
            jsonObject.get("message")?.asString
        } catch (e: Exception) {
            Log.e("ExpenseRepository", "Error parsing error message: ${e.message}")
            null
        }
    }
}
