package com.copay.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.copay.app.navigation.CopayNavHost
import com.copay.app.ui.theme.CopayTheme
import com.copay.app.utils.ConnectionManager
import com.copay.app.utils.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CopayTheme {
                // Initialize NavController to handle navigation.
                val navController = rememberNavController()
                // Pass the NavController to the CopayNavHost for navigation management.
                CopayNavHost(navController = navController)
            }
        }

        // Method to verify the connection with the backend.
        testApiConnection()

        // Method to verify the connection with the backend.
        testDataStore()
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

    private fun testDataStore() {
        lifecycleScope.launch {
            val testToken = "sample_jwt_token"

            // Save the token
            DataStoreManager.saveToken(applicationContext, testToken)
            Log.d("DataStoreTest", "Token saved: $testToken")

            // Retrieve the token
            val retrievedToken = DataStoreManager.getToken(applicationContext).first()
            Log.d("DataStoreTest", "Token retrieved: $retrievedToken")

            // Clear the token
            DataStoreManager.clearToken(applicationContext)
            Log.d("DataStoreTest", "Token cleared")

            // Check if the token is really removed
            val emptyToken = DataStoreManager.getToken(applicationContext).first()
            Log.d("DataStoreTest", "Token after clearing: $emptyToken")
        }
    }
}