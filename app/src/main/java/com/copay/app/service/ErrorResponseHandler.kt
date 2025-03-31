package com.copay.app.service

import android.util.Log
import com.copay.app.dto.response.JwtResponse
import com.copay.app.dto.response.LoginResponseDTO
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response

/**
 * ErrorResponseHandler is responsible for handling API response errors.
 * It processes the error responses returned by the API, converts them
 * into meaningful data, and ensures the app can handle failure scenarios.
 */

object ErrorResponseHandler {

    // Manage the error response and always map it to the received DTO response.
    inline fun <reified T> handleErrorResponse(response: Response<*>): T {
        val errorBody = response.errorBody()?.string()

        return try {
            Gson().fromJson(errorBody, T::class.java) ?: createDefaultErrorResponse()
        } catch (e: Exception) {
            Log.e("ResponseHandler", "Error parsing response: ${e.message}")

            // Extracts the error message from the backend DTO response if possible.
            val errorMessage = try {
                Gson().fromJson(errorBody, JsonObject::class.java)
                    ?.get("message")?.asString ?: "Unknown error"
            } catch (ex: Exception) {
                "Unknown error"
            }

            createDefaultErrorResponse(errorMessage)
        }
    }

    // Creates a default error response for known DTOs.
    inline fun <reified T> createDefaultErrorResponse(message: String = "An error occurred"): T {
        return when (T::class) {

            JwtResponse::class -> JwtResponse(
                message = message,
                token = null,
                expiresIn = null,
                type = null
            ) as T

            LoginResponseDTO::class -> LoginResponseDTO(
                message = message,
                token = null,
                type = null,
                expiresIn = null,
                username = null,
                email = null
            ) as T

            else -> throw IllegalArgumentException("Unsupported response type: ${T::class}")
        }
    }
}