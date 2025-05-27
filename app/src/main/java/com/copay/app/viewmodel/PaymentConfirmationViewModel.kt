package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.paymentconfirmation.request.ConfirmPaymentRequestDTO
import com.copay.app.dto.paymentconfirmation.response.ListUnconfirmedPaymentConfirmationResponseDTO
import com.copay.app.repository.PaymentConfirmationRepository
import com.copay.app.utils.state.PaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentConfirmationViewModel @Inject constructor(
    private val repository: PaymentConfirmationRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: MutableStateFlow<PaymentState> get() = _paymentState

    // List of unconfirmedPayments.
    private val _unconfirmedPayments = MutableStateFlow<List<ListUnconfirmedPaymentConfirmationResponseDTO>>(emptyList())
    val unconfirmedPayments: StateFlow<List<ListUnconfirmedPaymentConfirmationResponseDTO>> = _unconfirmedPayments

    fun resetPaymentState() {
        _paymentState.value = PaymentState.Idle
    }

    fun getUserExpenseIds(context: Context, groupId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val backendResponse = repository.getUserExpenseIdsForGroup(context, groupId)

            _paymentState.value = backendResponse
        }
    }

    fun getUnconfirmedPaymentConfirmations(context: Context, groupId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val backendResponse = repository.getUnconfirmedPaymentConfirmations(context, groupId)

            if (backendResponse is PaymentState.Success.UnconfirmedPayments) {
                _paymentState.value = backendResponse
                _unconfirmedPayments.value = backendResponse.unconfirmedPayments
            }
        }
    }

    fun requestPayment(context: Context, request: ConfirmPaymentRequestDTO) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val backendResponse = repository.requestPaymentFromUser(context, request)

            _paymentState.value = backendResponse
        }
    }

    fun confirmPayment(context: Context, request: ConfirmPaymentRequestDTO) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val backendResponse = repository.confirmPayment(context, request)

            _paymentState.value = backendResponse
        }
    }

    fun markAsConfirmed(context: Context, confirmationId: Long, groupId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val backendResponse = repository.markPaymentAsConfirmed(context, confirmationId)

            _paymentState.value = backendResponse

            getUnconfirmedPaymentConfirmations(context, groupId)
        }
    }

    fun deletePaymentConfirmation(context: Context, confirmationId: Long, groupId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val backendResponse = repository.deletePaymentConfirmation(context, confirmationId)

            _paymentState.value = backendResponse

            getUnconfirmedPaymentConfirmations(context, groupId)
        }
    }
}
