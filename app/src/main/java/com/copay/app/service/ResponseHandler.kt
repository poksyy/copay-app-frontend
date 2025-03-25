package com.copay.app.service

import android.util.Log
import com.copay.app.dto.response.JwtResponse
import com.google.gson.Gson
import retrofit2.Response

/**
 * ResponseHandler is responsible for handling API response errors.
 * It processes the error responses returned by the API, converts them
 * into meaningful data, and ensures the app can handle failure scenarios
 */

object ResponseHandler {

    // Parses the error response and returns a JwtResponse object.
    fun <T> handleErrorResponse(response: Response<T>): JwtResponse {

        val errorBody = response.errorBody()?.string()
        return try {
            Gson().fromJson(errorBody, JwtResponse::class.java)
        } catch (e: Exception) {
            Log.e("ResponseHandler", "Error parsing response: ${e.message}")
            JwtResponse(message = "Unknown error", token = null, expiresIn = null, type = null)
        }.also {
            Log.e("ResponseHandler", "Request failed: ${it.message}")
        }
    }
}
