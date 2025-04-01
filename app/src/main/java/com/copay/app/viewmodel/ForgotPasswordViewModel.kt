package com.copay.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.dto.request.ForgotPasswordDTO
import com.copay.app.repository.PasswordRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ForgotPasswordViewModel(private val repository: PasswordRepository) : ViewModel() {

    fun forgotPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.forgotPassword(ForgotPasswordDTO(email))

                if (response.isSuccessful) {
                    Log.d("ForgotPassword", "Email sent successfully.")
                    onSuccess()
                } else {
                    val errorMessage = "Error: ${response.code()} - ${response.message()}"
                    Log.e("ForgotPassword", errorMessage)
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("ForgotPassword", "Network error: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }
    }
}
