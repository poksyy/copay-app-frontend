package com.copay.app.repository

import com.copay.app.network.RetrofitInstance
import com.copay.app.model.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository {

    /** Login user by calling the API */
    suspend fun login(email: String, password: String): Response<UserResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.loginUser(email, password)
        }
    }

    /** Register user by calling the API */
    suspend fun register(username: String, email: String, password: String): Response<UserResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.registerUser(username, email, password)
        }
    }
}
