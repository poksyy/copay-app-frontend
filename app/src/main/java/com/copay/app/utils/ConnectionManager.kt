package com.copay.app.utils

import android.util.Log
import com.copay.app.config.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Response

object ConnectionManager {

    suspend fun testConnection(): String? {
        return try {
            // Hacer la llamada a la API
            val response: Response<ResponseBody> = RetrofitInstance.api.getConnectionResponse()

            // Verificar si la respuesta fue exitosa
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    // Obtener el cuerpo de la respuesta como texto
                    return responseBody.string() // Retorna el texto plano
                } else {
                    Log.e("TestConnection", "Response body is null")
                    return null
                }
            } else {
                Log.e("TestConnection", "Error: ${response.code()}")
                return null
            }
        } catch (e: Exception) {
            // Manejo de excepciones
            Log.e("TestConnection", "Exception occurred: ${e.message}", e)
            return null
        }
    }
}