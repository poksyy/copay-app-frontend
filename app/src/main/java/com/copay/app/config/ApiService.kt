package com.copay.app.config

import com.copay.app.dto.MessageResponseDTO
import com.copay.app.dto.auth.request.UserGoogleLoginRequestDTO
import com.copay.app.dto.expense.response.GetExpenseResponseDTO
import com.copay.app.dto.group.request.CreateGroupRequestDTO
import com.copay.app.dto.group.request.UpdateGroupExternalMembersRequestDTO
import com.copay.app.dto.group.request.UpdateGroupRegisteredMembersRequestDTO
import com.copay.app.dto.group.response.GroupResponseDTO
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
import com.copay.app.dto.expense.response.TotalDebtResponseDTO
import com.copay.app.dto.expense.response.TotalSpentResponseDTO
import com.copay.app.dto.expense.response.UserExpenseDTO
import com.copay.app.dto.group.response.GetGroupResponseDTO
import com.copay.app.dto.notification.response.NotificationListResponseDTO
import com.copay.app.dto.paymentconfirmation.request.ConfirmPaymentRequestDTO
import com.copay.app.dto.paymentconfirmation.response.ListUnconfirmedPaymentConfirmationResponseDTO
import com.copay.app.dto.paymentconfirmation.response.PaymentResponseDTO
import com.copay.app.dto.profile.response.EmailResponseDTO
import com.copay.app.dto.profile.response.PasswordResponseDTO
import com.copay.app.dto.profile.response.PhoneNumberResponseDTO
import com.copay.app.dto.profile.response.UsernameResponseDTO
import com.copay.app.dto.unsplash.request.PhotoRequestDTO
import com.copay.app.dto.unsplash.response.UnsplashResponse
import com.copay.app.dto.user.UserResponseDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

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

    // Google login API Call.
    @POST("${BASE_PATH}auth/google/login")
    suspend fun loginUserWithGoogle(@Body request: UserGoogleLoginRequestDTO): Response<LoginResponseDTO>

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

    /** API Calls to get specific user **/
    // Get user by phone number
    @GET("${BASE_PATH}users/phone/{phoneNumber}")
    suspend fun getUserByPhone(
        @Path("phoneNumber") phoneNumber: String,
        @Header("Authorization") token: String
    ): Response<UserResponseDTO>

    /** API Calls to edit profile data **/
    // Update username
    @PUT("${BASE_PATH}users/edit-username/{id}")
    suspend fun updateUsername(
        @Path("id") userId: Long,
        @Body request: UpdateUsernameDTO,
        @Header("Authorization") token: String
    ): Response<UsernameResponseDTO>

    // Update phone number.
    @PUT("${BASE_PATH}users/edit-phone/{id}")
    suspend fun updatePhoneNumber(
        @Path("id") userId: Long,
        @Body request: UpdatePhoneNumberDTO,
        @Header("Authorization") token: String
    ): Response<PhoneNumberResponseDTO>

    // Update email.
    @PUT("${BASE_PATH}users/edit-email/{id}")
    suspend fun updateEmail(
        @Path("id") userId: Long,
        @Body request: UpdateEmailDTO,
        @Header("Authorization") token: String
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
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<GetGroupResponseDTO>

    // Retrieve a group by its group ID.
    @GET("${BASE_PATH}groups/{groupId}/group")
    suspend fun getGroupByGroupId(
        @Path("groupId") groupId: Long,
        @Header("Authorization") token: String
    ): Response<GroupResponseDTO>

    // Create group.
    @POST("${BASE_PATH}groups")
    suspend fun createGroup(
        @Body request: CreateGroupRequestDTO,
        @Header("Authorization") token: String
    ): Response<GroupResponseDTO>

    // Delete group.
    @DELETE("${BASE_PATH}groups/{groupId}")
    suspend fun deleteGroup(
        @Path("groupId") groupId: Long,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Leave group.
    @DELETE("${BASE_PATH}groups/{groupId}/leave")
    suspend fun leaveGroup(
        @Path("groupId") groupId: Long,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Update group.
    @PATCH("${BASE_PATH}groups/{groupId}")
    suspend fun updateGroup(
        @Path("groupId") groupId: Long,
        @Body fieldChanges: Map<String, @JvmSuppressWildcards Any>,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Unsplash search photo
    @GET("api/photos/search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 21
    ): UnsplashResponse

    // Update photo group.
    @POST("api/photos/group/{groupId}")
    suspend fun setGroupPhoto(
        @Path("groupId") groupId: Long,
        @Body photoRequestDTO: PhotoRequestDTO,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Delete photo group.
    @DELETE("api/photos/group/{groupId}")
    suspend fun removeGroupPhoto(
        @Path("groupId") groupId: Long,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Update estimated price group.
    @PATCH("${BASE_PATH}groups/{groupId}/estimatedprice")
    suspend fun updateGroupEstimatedPrice(
        @Path("groupId") groupId: Long,
        @Body request: Map<String, Float>,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Update group registered members.
    @PATCH("${BASE_PATH}groups/{groupId}/registeredmembers")
    suspend fun updateGroupRegisteredMembers(
        @Path("groupId") groupId: Long,
        @Body request: UpdateGroupRegisteredMembersRequestDTO,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Update group external members.
    @PATCH("${BASE_PATH}groups/{groupId}/externalmembers")
    suspend fun updateGroupExternalMembers(
        @Path("groupId") groupId: Long,
        @Body request: UpdateGroupExternalMembersRequestDTO,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    /** API Call to Expenses**/
    // Get expenses by group id.
    @GET("${BASE_PATH}expenses/{groupId}")
    suspend fun getExpenses(
        @Path("groupId") groupId: Long,
        @Header("Authorization") token: String
    ): Response<List<GetExpenseResponseDTO>>

    // Get all user expenses for a specific group
    @GET("${BASE_PATH}expenses/{groupId}/user-expenses")
    suspend fun getUserExpensesByGroupId(
        @Path("groupId") groupId: Long,
        @Header("Authorization") token: String
    ): Response<List<UserExpenseDTO>>

    // Get total debt of a user across all groups
    @GET("${BASE_PATH}expenses/user/{userId}/total-debt")
    suspend fun getTotalUserDebt(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<TotalDebtResponseDTO>

    // Get total amount spent by a user across all groups
    @GET("${BASE_PATH}expenses/user/{userId}/total-spent")
    suspend fun getTotalUserSpent(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<TotalSpentResponseDTO>

    /** API Calls to Payment Confirmations **/
    // Get userExpenseIds for current user in a specific group
    @GET("${BASE_PATH}payment-confirmations/groups/{groupId}/user-expenses")
    suspend fun getUserExpenseIds(
        @Path("groupId") groupId: Long, @Header("Authorization") token: String
    ): Response<List<PaymentResponseDTO>>

    // Get all unconfirmed payment confirmations in a group
    @GET("${BASE_PATH}payment-confirmations/groups/{groupId}/unconfirmed")
    suspend fun getUnconfirmedPaymentConfirmations(
        @Path("groupId") groupId: Long, @Header("Authorization") token: String
    ): Response<List<ListUnconfirmedPaymentConfirmationResponseDTO>>

    // Request a payment confirmation (from user side)
    @POST("${BASE_PATH}payment-confirmations/request-payment")
    suspend fun requestPaymentConfirmation(
        @Body request: ConfirmPaymentRequestDTO, @Header("Authorization") token: String
    ): Response<PaymentResponseDTO>

    // Confirm payment (group creator confirms)
    @POST("${BASE_PATH}payment-confirmations/confirm")
    suspend fun confirmPayment(
        @Body request: ConfirmPaymentRequestDTO, @Header("Authorization") token: String
    ): Response<PaymentResponseDTO>

    // Mark a payment confirmation as confirmed (used by backend)
    @PATCH("${BASE_PATH}payment-confirmations/confirm/{confirmationId}")
    suspend fun markPaymentAsConfirmed(
        @Path("confirmationId") confirmationId: Long, @Header("Authorization") token: String
    ): Response<PaymentResponseDTO>

    // Delete payment confirmation
    @DELETE("${BASE_PATH}payment-confirmations/{paymentConfirmationId}")
    suspend fun deletePaymentConfirmation(
        @Path("paymentConfirmationId") paymentConfirmationId: Long,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    /** API Calls for notifications **/
    // Get all notifications for the authenticated user
    @GET("${BASE_PATH}notifications")
    suspend fun getAllNotifications(
        @Header("Authorization") token: String
    ): Response<NotificationListResponseDTO>

    // Get only unread notifications for the authenticated user
    @GET("${BASE_PATH}notifications/unread")
    suspend fun getUnreadNotifications(
        @Header("Authorization") token: String
    ): Response<NotificationListResponseDTO>

    // Mark a single notification as read
    @PATCH("${BASE_PATH}notifications/{notificationId}/read")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: Long,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Mark all notifications as read
    @PATCH("${BASE_PATH}notifications/read-all")
    suspend fun markAllNotificationsAsRead(
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

    // Delete a notification
    @DELETE("${BASE_PATH}notifications/{notificationId}")
    suspend fun deleteNotification(
        @Path("notificationId") notificationId: Long,
        @Header("Authorization") token: String
    ): Response<MessageResponseDTO>

}
