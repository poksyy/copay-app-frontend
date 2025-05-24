package com.copay.app.dto.paymentconfirmation.response

/**
 * Data class representing the response for unconfirmed payment confirmations.
 */

data class ListUnconfirmedPaymentConfirmationResponseDTO(

    val paymentConfirmationId: Long,
    val userExpenseId: Long,
    val confirmationAmount: Float,
    val username: String
)