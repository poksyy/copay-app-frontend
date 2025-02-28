package com.copay.app.utils

import android.util.Log
import com.copay.app.config.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Response

object ConnectionManager {

    suspend fun testConnection(): String? {
        try {
            // Make the API call using Retrofit.
            val response: Response<ResponseBody> = RetrofitInstance.api.getConnectionResponse()

            // Response management.
            if (response.isSuccessful) {
                val responseBody = response.body()

                if (responseBody != null) {
                    // Return the response body as plain text.
                    return responseBody.string()
                } else {
                    Log.e("TestConnection", "Response body is null")
                    return null
                }
            } else {
                Log.e("TestConnection", "Error: ${response.code()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("TestConnection", "Exception occurred: ${e.message}", e)
            return null
        }
    }
}