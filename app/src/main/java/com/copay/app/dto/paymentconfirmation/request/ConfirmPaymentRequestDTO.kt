package com.copay.app.dto.paymentconfirmation.request

/**
 * Data class representing the request body for confirming a payment.
 */
data class ConfirmPaymentRequestDTO(

    val userExpenseId: Long,
    val confirmationAmount: Float
)