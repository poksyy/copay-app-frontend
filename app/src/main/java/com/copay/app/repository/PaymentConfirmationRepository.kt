package com.copay.app.repository

import android.content.Context
import android.util.Log
import com.copay.app.dto.MessageResponseDTO
import com.copay.app.dto.paymentconfirmation.request.ConfirmPaymentRequestDTO
import com.copay.app.dto.paymentconfirmation.request.DeletePaymentConfirmationRequestDTO
import com.copay.app.dto.paymentconfirmation.response.ListUnconfirmedPaymentConfirmationResponseDTO
import com.copay.app.dto.paymentconfirmation.response.PaymentResponseDTO
import com.copay.app.service.PaymentConfirmationService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.PaymentState
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response

/**
 * PaymentConfirmationRepository handles operations related to payment confirmations
 * by interacting with the PaymentConfirmationService. It includes methods for requesting,
 * confirming, retrieving, and deleting payment confirmations.
 */

class PaymentConfirmationRepository(private val service: PaymentConfirmationService) {

    suspend fun getUserExpenseIdsForGroup(context: Context, groupId: Long): PaymentState {
        val token = DataStoreManager.getFormattedToken(context)
        return handleApiResponse(context) {
            service.getUserExpenseIds(groupId, token)
        }
    }

    suspend fun getUnconfirmedPaymentConfirmations(context: Context, groupId: Long): PaymentState {
        return handleApiResponse(context) {
            service.getUnconfirmedPaymentConfirmations(groupId)
        }
    }

    suspend fun requestPaymentFromUser(
        context: Context,
        request: ConfirmPaymentRequestDTO
    ): PaymentState {
        val token = DataStoreManager.getFormattedToken(context)
        return handleApiResponse(context) {
            service.requestPaymentConfirmation(request, token)
        }
    }

    suspend fun confirmPayment(
        context: Context,
        request: ConfirmPaymentRequestDTO
    ): PaymentState {
        val token = DataStoreManager.getFormattedToken(context)
        return handleApiResponse(context) {
            service.confirmPayment(request, token)
        }
    }

    suspend fun markPaymentAsConfirmed(
        context: Context,
        confirmationId: Long
    ): PaymentState {
        val token = DataStoreManager.getFormattedToken(context)
        return handleApiResponse(context) {
            service.markPaymentAsConfirmed(confirmationId, token)
        }
    }

    suspend fun deletePaymentConfirmation(
        context: Context,
        confirmationId: Long
    ): PaymentState {
        val token = DataStoreManager.getFormattedToken(context)

        val request = DeletePaymentConfirmationRequestDTO(paymentConfirmationId = confirmationId)

        return handleApiResponse(context) {
            service.deletePaymentConfirmation(confirmationId, request, token)
        }
    }

    private suspend fun <T> handleApiResponse(
        context: Context, apiCall: suspend () -> Response<T>
    ): PaymentState {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    when (body) {
                        is List<*> -> {
                            when {
                                body.all { it is PaymentResponseDTO } ->
                                    PaymentState.Success.ConfirmedPayments(
                                        body.filterIsInstance<PaymentResponseDTO>()
                                    )

                                body.all { it is ListUnconfirmedPaymentConfirmationResponseDTO } ->
                                    PaymentState.Success.UnconfirmedPayments(
                                        body.filterIsInstance<ListUnconfirmedPaymentConfirmationResponseDTO>()
                                    )

                                else -> PaymentState.Error("Unsupported list response type")
                            }
                        }

                        is PaymentResponseDTO -> PaymentState.Success.SingleResult(body)

                        is MessageResponseDTO -> PaymentState.Success.Message(body.message)

                        else -> PaymentState.Error("Unexpected response type")
                    }
                } ?: PaymentState.Error("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("PaymentRepository", "Error Body: $errorBody")
                val message = extractErrorMessage(errorBody)
                PaymentState.Error(message ?: "Unknown error")
            }
        } catch (e: Exception) {
            Log.e("PaymentRepository", "Connection error: ${e.message}")
            PaymentState.Error("Connection error: ${e.message}")
        }
    }

    private fun extractErrorMessage(errorJson: String?): String? {
        if (errorJson.isNullOrEmpty()) return null
        return try {
            val jsonObject = Gson().fromJson(errorJson, JsonObject::class.java)
            jsonObject.get("message")?.asString
        } catch (e: Exception) {
            Log.e("PaymentRepository", "Error parsing error message: ${e.message}")
            null
        }
    }
}
