package com.copay.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.paymentconfirmation.request.ConfirmPaymentRequestDTO
import com.copay.app.repository.PaymentConfirmationRepository
import com.copay.app.utils.state.PaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentConfirmationViewModel @Inject constructor(
    private val repository: PaymentConfirmationRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: MutableStateFlow<PaymentState> get() = _paymentState

    fun resetPaymentState() {
        _paymentState.value = PaymentState.Idle
    }

    fun getUserExpenseIds(context: Context, groupId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val response = repository.getUserExpenseIdsForGroup(context, groupId)

            _paymentState.value = response
        }
    }

    fun getAllUserExpensesByGroup(context: Context, groupId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val response = repository.getAllUserExpensesByGroup(context, groupId)

            _paymentState.value = response
        }
    }

    fun getUnconfirmedPaymentConfirmations(context: Context, groupId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val response = repository.getUnconfirmedPaymentConfirmations(context, groupId)

            _paymentState.value = response
        }
    }

    fun requestPayment(context: Context, request: ConfirmPaymentRequestDTO) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val response = repository.requestPaymentFromUser(context, request)

            _paymentState.value = response
        }
    }

    fun confirmPayment(context: Context, request: ConfirmPaymentRequestDTO) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val response = repository.confirmPayment(context, request)

            _paymentState.value = response
        }
    }

    fun markAsConfirmed(context: Context, confirmationId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val response = repository.markPaymentAsConfirmed(context, confirmationId)

            _paymentState.value = response
        }
    }

    fun deletePaymentConfirmation(context: Context, confirmationId: Long) {
        viewModelScope.launch {

            _paymentState.value = PaymentState.Loading

            val response = repository.deletePaymentConfirmation(context, confirmationId)

            _paymentState.value = response
        }
    }
}
