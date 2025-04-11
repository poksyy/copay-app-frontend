package com.copay.app.config


import com.copay.app.dto.request.ForgotPasswordDTO
import com.copay.app.dto.request.UserLoginRequestDTO
import com.copay.app.dto.request.UserRegisterStepTwoDTO
import com.copay.app.dto.request.UserRegisterStepOneDTO
import com.copay.app.dto.request.profile.UpdateEmailDTO
import com.copay.app.dto.request.profile.UpdatePhoneNumberDTO
import com.copay.app.dto.request.profile.UpdateUsernameDTO
import com.copay.app.dto.response.RegisterStepOneResponseDTO
import com.copay.app.dto.response.LoginResponseDTO
import com.copay.app.dto.response.RegisterStepTwoResponseDTO
import com.copay.app.dto.response.profile.EmailResponseDTO
import com.copay.app.dto.response.profile.PhoneNumberResponseDTO
import com.copay.app.dto.response.profile.UsernameResponseDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    companion object {
        private const val BASE_PATH = "api/"
    }

    // Method to test the connection with the client.
    @GET("${BASE_PATH}response")
    suspend fun getConnectionResponse(): Response<ResponseBody>

    // Login API Call.
    @POST("${BASE_PATH}auth/login")
    suspend fun loginUser(@Body request: UserLoginRequestDTO): Response<LoginResponseDTO>

    // Register Step One API Call.
    @POST("${BASE_PATH}auth/register/step-one")
    suspend fun registerStepOne(@Body request: UserRegisterStepOneDTO): Response<RegisterStepOneResponseDTO>

    // Register Step Two API Call.
    @POST("${BASE_PATH}auth/register/step-two")
    suspend fun registerStepTwo(
        @Body request: UserRegisterStepTwoDTO,
        @Header("Authorization") token: String
    ): Response<RegisterStepTwoResponseDTO>

    // Logout API Call.
    @POST("${BASE_PATH}auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Unit>

    // Forgot Password API Call
    @POST("${BASE_PATH}forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordDTO): Response<Unit>

    // API Calls to edit profile data
    // Update username
    @PUT("${BASE_PATH}users/edit-username/{id}")
    suspend fun updateUsername(
        @Path("id") userId: Long,
        @Body request: UpdateUsernameDTO
    ): Response<UsernameResponseDTO>

    // Update phone number
    @PUT("${BASE_PATH}users/edit-phone/{id}")
    suspend fun updatePhoneNumber(
        @Path("id") userId: Long,
        @Body request: UpdatePhoneNumberDTO
    ): Response<PhoneNumberResponseDTO>

    // Update email
    @PUT("${BASE_PATH}users/edit-email/{id}")
    suspend fun updateEmail(
        @Path("id") userId: Long,
        @Body request: UpdateEmailDTO
    ): Response<EmailResponseDTO>
}
