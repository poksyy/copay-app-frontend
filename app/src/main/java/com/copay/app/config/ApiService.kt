package com.copay.app.config

import com.copay.app.dto.expense.response.GetExpenseResponseDTO
import com.copay.app.dto.group.request.CreateGroupRequestDTO
import com.copay.app.dto.group.request.UpdateGroupExternalMembersRequestDTO
import com.copay.app.dto.group.request.UpdateGroupRegisteredMembersRequestDTO
import com.copay.app.dto.group.response.CreateGroupResponseDTO
import com.copay.app.dto.password.ForgotPasswordDTO
import com.copay.app.dto.auth.request.UserLoginRequestDTO
import com.copay.app.dto.auth.request.UserRegisterStepTwoDTO
import com.copay.app.dto.auth.request.UserRegisterStepOneDTO
import com.copay.app.dto.profile.request.UpdateEmailDTO
import com.copay.app.dto.profile.request.UpdatePasswordDTO
import com.copay.app.dto.profile.request.UpdatePhoneNumberDTO
import com.copay.app.dto.profile.request.UpdateUsernameDTO
import com.copay.app.dto.auth.response.RegisterStepOneResponseDTO
import com.copay.app.dto.auth.response.LoginResponseDTO
import com.copay.app.dto.auth.response.RegisterStepTwoResponseDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import com.copay.app.dto.group.response.GroupMessageResponseDTO
import com.copay.app.dto.profile.response.EmailResponseDTO
import com.copay.app.dto.profile.response.PasswordResponseDTO
import com.copay.app.dto.profile.response.PhoneNumberResponseDTO
import com.copay.app.dto.profile.response.UsernameResponseDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
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

    // Forgot Password API Call.
    @POST("${BASE_PATH}forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordDTO): Response<Unit>

    /** API Calls to edit profile data **/
    // Update username
    @PUT("${BASE_PATH}users/edit-username/{id}")
    suspend fun updateUsername(
        @Path("id") userId: Long,
        @Body request: UpdateUsernameDTO
    ): Response<UsernameResponseDTO>

    // Update phone number.
    @PUT("${BASE_PATH}users/edit-phone/{id}")
    suspend fun updatePhoneNumber(
        @Path("id") userId: Long,
        @Body request: UpdatePhoneNumberDTO
    ): Response<PhoneNumberResponseDTO>

    // Update email.
    @PUT("${BASE_PATH}users/edit-email/{id}")
    suspend fun updateEmail(
        @Path("id") userId: Long,
        @Body request: UpdateEmailDTO
    ): Response<EmailResponseDTO>

    // Update password.
    @PUT("${BASE_PATH}reset-password")
    suspend fun updatePassword(
        @Body request: UpdatePasswordDTO,
        @Header("Authorization") token: String
    ): Response<PasswordResponseDTO>

    /** API Calls to Groups **/
    // Retrieve groups by user ID.
    @GET("${BASE_PATH}groups/{userId}")
    suspend fun getGroupsByUser(
        @Path("userId") userId: Long
    ): Response<GetGroupResponseDTO>

    // Create group.
    @POST("${BASE_PATH}groups")
    suspend fun createGroup(
        @Body request: CreateGroupRequestDTO
    ): Response<CreateGroupResponseDTO>

    // Delete group.
    @DELETE("${BASE_PATH}groups/{groupId}")
    suspend fun deleteGroup(
        @Path("groupId") groupId: Long,
        @Header("Authorization") token: String
    ): Response<GroupMessageResponseDTO>

    // Leave group.
    @DELETE("${BASE_PATH}groups/{groupId}/leave")
    suspend fun leaveGroup(
        @Path("groupId") groupId: Long,
        @Header("Authorization") token: String
    ): Response<GroupMessageResponseDTO>

    // Update group.
    @PATCH("${BASE_PATH}groups/{groupId}")
    suspend fun updateGroup(
        @Path("groupId") groupId: Long,
        @Body fieldChanges: Map<String, @JvmSuppressWildcards Any>
    ): Response<GroupMessageResponseDTO>

    // Update group registered members.
    @PATCH("${BASE_PATH}groups/{groupId}/copaymembers")
    suspend fun updateGroupRegisteredMembers(
        @Path("groupId") groupId: Long,
        @Body request: UpdateGroupRegisteredMembersRequestDTO
    ): Response<GroupMessageResponseDTO>

    // Update group external members.
    @PATCH("${BASE_PATH}groups/{groupId}/externalmembers")
    suspend fun updateGroupExternalMembers(
        @Path("groupId") groupId: Long,
        @Body request: UpdateGroupExternalMembersRequestDTO
    ): Response<GroupMessageResponseDTO>

    // Get expenses by group id.
    @GET("${BASE_PATH}expenses/{groupId}")
    suspend fun getExpenses(@Path("groupId") groupId: Long): Response<List<GetExpenseResponseDTO>>
}
