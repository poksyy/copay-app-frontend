package com.copay.app.dto.paymentconfirmation.request

/**
 * Data class representing the request body for creating a payment confirmation.
 */
data class CreatePaymentConfirmationRequestDTO(

    val groupId: Long,
    val expenseId: Long,
    val userExpenseId: Long,
    val confirmationAmount: Float,

    // Optional field. Can be null
    val smsCode: String? = null
)
