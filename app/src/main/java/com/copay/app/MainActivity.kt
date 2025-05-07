package com.copay.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.copay.app.navigation.CopayNavHost
import com.copay.app.ui.theme.CopayTheme
import com.copay.app.utils.ConnectionManager
import com.copay.app.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // State to handle theme.
            var isDarkTheme by remember { mutableStateOf(false) }

            CopayTheme(darkTheme = isDarkTheme) {
                // Initialize NavController to handle navigation.
                val navController = rememberNavController()
                // Pass the NavController to the CopayNavHost for navigation management.
                CopayNavHost(
                    navController = navController,
                    toggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }

        // Method to verify the connection with the backend.
        ConnectionManager.testApiConnection()

        // Method to verify the connection with the backend.
        lifecycleScope.launch {
            DataStoreManager.testDataStore(applicationContext)
        }

    }

}