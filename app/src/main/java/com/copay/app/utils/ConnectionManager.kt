package com.copay.app.utils

import android.util.Log
import com.copay.app.config.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

object ConnectionManager {

    private suspend fun testConnection(): String? {
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

    fun testApiConnection() {
        // Using CoroutineScope to handle the asynchronous call on the IO thread.
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ConnectionManager.testConnection()

                // Returning to the main thread to log the result.
                withContext(Dispatchers.Main) {

                    if (response != null) {
                        Log.d("TestConnection", "Connection Successful")
                    } else {
                        Log.e("TestConnection", "Error: Response body is null")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("TestConnection", "Exception occurred: ${e.message}", e)
                }
            }
        }
    }
}