package com.copay.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.repository.UserRepository
import com.copay.app.service.UserService
import com.copay.app.utils.DataStoreManager
import com.copay.app.utils.state.AuthState
import com.copay.app.utils.session.UserSession
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userSession: UserSession
) : ViewModel() {

    init {
        Log.d("AuthViewModel", "ViewModel created")
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    private val _signInResult = MutableLiveData<Result<String>>()

    private val _isLoginResult = MutableStateFlow<Boolean?>(null)
    val isLoginResult: StateFlow<Boolean?> = _isLoginResult

    fun login(context: Context, phoneNumber: String, password: String) {
        viewModelScope.launch {

            _authState.value = AuthState.Loading
            // Stores the response from backend.
            val backendResponse = userRepository.login(context, phoneNumber, password)
            // Updates the UI trough authState.
            _authState.value = backendResponse

            // Only execute if the response is success.
            if (backendResponse is AuthState.Success) {
                // Extracts the user details through the userService.
                val extractedUser = userService.extractUser(backendResponse)
                extractedUser?.let {
                    // Set the user information trough userSession.
                    userSession.setUser(
                        it.phonePrefix,
                        it.phoneNumber,
                        it.userId,
                        it.username,
                        it.email
                    )
                }
            }
        }
    }

    // Handles the first step of user registration.
    fun registerStepOne(
        context: Context,
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {

            _authState.value = AuthState.Loading
            // Updates the UI trough authState.
            _authState.value =
                userRepository.registerStepOne(context, username, email, password, confirmPassword)
        }
    }

    // Handles the second step of user registration (phone number verification).
    fun registerStepTwo(context: Context, phonePrefix: String, phoneNumber: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Stores the response from backend.
            val backendResponse = userRepository.registerStepTwo(context, phonePrefix, phoneNumber)
            // Updates the UI through authState.
            _authState.value = backendResponse

            if (backendResponse is AuthState.Success) {
                // Extracts the user details through the userService.
                val extractedUser = userService.extractUser(backendResponse)
                extractedUser?.let {
                    // Set the user information through userSession.
                    userSession.setUser(
                        it.phonePrefix,
                        it.phoneNumber,
                        it.userId,
                        it.username,
                        it.email
                    )
                }
            }
        }
    }

    // Login with google.
    private fun loginWithGoogle(context: Context, idToken: String) {
        Log.d("GoogleSignIn", "Starting loginWithGoogle with token: ${idToken.take(20)}...") // Evita imprimir el token completo
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val backendResponse = userRepository.loginWithGoogle(context, idToken)
            Log.d("GoogleSignIn", "Backend response: ${backendResponse::class.simpleName}")

            _authState.value = backendResponse

            if (backendResponse is AuthState.Success) {

                Log.d("GoogleSignIn", "Login success from backend.")
                val extractedUser = userService.extractUser(backendResponse)

                if (extractedUser != null) {

                    Log.d("GoogleSignIn", "Extracted user: ${extractedUser.email}, isLogin=${extractedUser.isLogin}")
                    userSession.setUser(extractedUser.phonePrefix, extractedUser.phoneNumber, extractedUser.userId, extractedUser.username, extractedUser.email)

                    // Converts the "true" or "false" from string to boolean.
                    val isLoginBoolean = extractedUser.isLogin?.toBooleanStrictOrNull()
                    _isLoginResult.value = isLoginBoolean

                } else {
                    Log.e("GoogleSignIn", "User extraction failed.")
                }
            } else {
                Log.e("GoogleSignIn", "Login failed from backend.")
            }
        }
    }


    // Logs out the user by clearing the stored authentication token.
    fun logout(context: Context) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            DataStoreManager.getToken(context).first()?.let { token ->
                userRepository.logout(context, token)
            }
            DataStoreManager.clearToken(context)
            _authState.value = AuthState.Success(null)
        }
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>, context: Context) {
        Log.d("GoogleSignIn", "Handling sign-in result...")
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("GoogleSignIn", "Account retrieved: email=${account.email}, id=${account.id}")

            val idToken = account.idToken
            if (idToken != null) {
                Log.d("GoogleSignIn", "Valid ID Token received. Sending to backend...")
                loginWithGoogle(context, idToken)
                _signInResult.value = Result.success(idToken)
            } else {
                Log.e("GoogleSignIn", "ID Token is null. Cannot proceed.")
                _signInResult.value = Result.failure(Exception("ID token is null"))
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "ApiException: ${e.statusCode} - ${e.message}", e)
            _signInResult.value = Result.failure(e)
        }
    }
}