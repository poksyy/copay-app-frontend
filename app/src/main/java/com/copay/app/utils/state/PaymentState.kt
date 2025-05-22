package com.copay.app.utils.state

import com.copay.app.dto.paymentconfirmation.response.ListUnconfirmedPaymentConfirmationResponseDTO
import com.copay.app.dto.paymentconfirmation.response.PaymentResponseDTO

/**
 * Different payment confirmation states for UI handling.
 */
sealed class PaymentState {

    // Initial idle state.
    data object Idle : PaymentState()

    // Loading state while performing payment-related operations.
    data object Loading : PaymentState()

    // Payment operations completed successfully.
    sealed class Success : PaymentState() {

        // When a single payment confirmation is returned.
        data class SingleResult(val data: PaymentResponseDTO) : Success()

        // When a generic message is returned (e.g., deletion confirmation).
        data class Message(val message: String) : Success()

        // List of confirmed or all payment confirmations.
        data class ConfirmedPayments(val payments: List<PaymentResponseDTO>) : Success()

        // List of unconfirmed payment confirmations (pending).
        data class UnconfirmedPayments(val unconfirmed: List<ListUnconfirmedPaymentConfirmationResponseDTO>) : Success()
    }

    // An error occurred during payment operations.
    data class Error(val message: String) : PaymentState()
}
