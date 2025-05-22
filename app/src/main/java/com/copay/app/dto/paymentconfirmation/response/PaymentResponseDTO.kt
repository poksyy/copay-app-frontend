package com.copay.app.dto.paymentconfirmation.response

/**
 * Data class representing a single payment confirmation response.
 */

data class PaymentResponseDTO(

    val paymentConfirmationId: Long,
    val userExpenseId: Long,
    val confirmationAmount: Float,
    val confirmationDate: String,
    val isConfirmed: Boolean,
    val confirmedAt: String
)