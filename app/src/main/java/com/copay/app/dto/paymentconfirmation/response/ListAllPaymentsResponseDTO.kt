package com.copay.app.dto.paymentconfirmation.response

/**
 * Data class representing the response for listing all payment confirmations.
 */

data class ListAllPaymentsResponseDTO(

    val paymentConfirmationId: Long,
    val userExpenseId: Long,
    val confirmationAmount: Float,
    val confirmationDate: String,
    val isConfirmed: Boolean,
    val confirmedAt: String
)