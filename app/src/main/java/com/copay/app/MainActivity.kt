package com.copay.app.ui

import android.os.Bundle
import android.app.Activity
import android.util.Log
import com.copay.app.utils.ConnectionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.R.layout.simple_list_item_1)

        // Method to verify the connection with the backend.
        testApiConnection();
    }

    private fun testApiConnection() {
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