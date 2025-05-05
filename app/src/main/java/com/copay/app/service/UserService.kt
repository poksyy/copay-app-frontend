package com.copay.app.service

import android.util.Log
import com.google.gson.Gson
import com.copay.app.dto.auth.response.LoginResponseDTO
import com.copay.app.dto.auth.response.RegisterStepTwoResponseDTO
import com.copay.app.model.User
import com.copay.app.utils.state.AuthState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor() {

    // Receives the Success response with all the backend Json information.
    fun extractUser(response: AuthState.Success): User? {

        val gson = Gson()

        // Log the complete received response.
        Log.d("UserService", "Backend Response: ${gson.toJson(response)}")

        // Convert `user` object to JSON (debugging if needed).
        val userJson = gson.toJson(response.user)
        Log.d("UserService", "User JSON: $userJson")

        return when (val data = response.user) {
            // Backend response when an user log in.
            is LoginResponseDTO -> {
                Log.d("UserService", "LoginResponseDTO received: " +
                        "phoneNumber=${data.phoneNumber}, userId=${data.userId}, " +
                        "username=${data.username}, email=${data.email}")
                User(
                    userId = data.userId,
                    username = data.username,
                    email = data.email,
                    phoneNumber = data.phoneNumber
                )
            }
            // Backend response when an user registers.
            is RegisterStepTwoResponseDTO -> {
                Log.d("UserService", "RegisterStepTwoResponseDTO received: " +
                        "phoneNumber=${data.phoneNumber}, userId=${data.userId}, " +
                        "username=${data.username}, email=${data.email}")
                User(
                    userId = data.userId,
                    username = data.username,
                    email = data.email,
                    phoneNumber = data.phoneNumber
                )
            }
            else -> {
                Log.d("UserService", "Unexpected response type: $data")
                null
            }
        }
    }
}
