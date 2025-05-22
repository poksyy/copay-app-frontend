package com.copay.app.dto.paymentconfirmation.response

/**
 * Data class representing the response for unconfirmed payment confirmations.
 */

data class ListUnconfirmedPaymentConfirmationResponseDTO(

    val userExpenseId: Long,
    val confirmationAmount: Float
)