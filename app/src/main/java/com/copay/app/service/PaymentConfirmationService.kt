package com.copay.app.service

import com.copay.app.config.ApiService
import com.copay.app.dto.MessageResponseDTO
import com.copay.app.dto.paymentconfirmation.request.ConfirmPaymentRequestDTO
import com.copay.app.dto.paymentconfirmation.request.DeletePaymentConfirmationRequestDTO
import com.copay.app.dto.paymentconfirmation.response.ListUnconfirmedPaymentConfirmationResponseDTO
import com.copay.app.dto.paymentconfirmation.response.PaymentResponseDTO
import retrofit2.Response

/**
 * PaymentConfirmationService manages all operations related to payment confirmations,
 * including requests, confirmations, queries, and deletions.
 */
class PaymentConfirmationService(private val api: ApiService) {

    // Get userExpenseIds for current user in a group
    suspend fun getUserExpenseIds(
        groupId: Long, token: String
    ): Response<List<PaymentResponseDTO>> {
        return api.getUserExpenseIds(groupId, token)
    }

    // Get all unconfirmed payment confirmations for a group
    suspend fun getUnconfirmedPaymentConfirmations(
        groupId: Long, token: String
    ): Response<List<ListUnconfirmedPaymentConfirmationResponseDTO>> {
        return api.getUnconfirmedPaymentConfirmations(groupId, token)
    }

    // Request a payment confirmation via CreatePaymentConfirmationRequestDTO
    suspend fun requestPaymentConfirmation(
        request: ConfirmPaymentRequestDTO, token: String
    ): Response<PaymentResponseDTO> {
        return api.requestPaymentConfirmation(request, token)
    }

    // Confirm a payment via ConfirmPaymentRequestDTO
    suspend fun confirmPayment(
        request: ConfirmPaymentRequestDTO, token: String
    ): Response<PaymentResponseDTO> {
        return api.confirmPayment(request, token)
    }

    // Mark a specific confirmation as confirmed
    suspend fun markPaymentAsConfirmed(
        confirmationId: Long, token: String
    ): Response<PaymentResponseDTO> {
        return api.markPaymentAsConfirmed(confirmationId, token)
    }

    // Delete a payment confirmation (passing the ID manually in the body map)
    suspend fun deletePaymentConfirmation(
        paymentConfirmationId: Long, token: String
    ): Response<MessageResponseDTO> {
        return api.deletePaymentConfirmation(paymentConfirmationId, token)
    }
}
