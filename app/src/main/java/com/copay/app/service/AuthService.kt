import android.util.Log
import com.copay.app.config.ApiService
import com.copay.app.dto.request.UserLoginRequestDTO
import com.copay.app.dto.request.UserRegisterStepOneDTO
import com.copay.app.dto.request.UserRegisterStepTwoDTO
import com.copay.app.dto.response.LoginResponseDTO
import com.copay.app.dto.response.RegisterStepOneResponseDTO
import com.copay.app.dto.response.RegisterStepTwoResponseDTO
import retrofit2.Response
import retrofit2.http.Header

class AuthService(private val api: ApiService) {

    suspend fun registerStepOne(request: UserRegisterStepOneDTO): Response<RegisterStepOneResponseDTO> {
        return api.registerStepOne(request)
    }

    suspend fun registerStepTwo(request: UserRegisterStepTwoDTO, token: String): Response<RegisterStepTwoResponseDTO> {
        Log.d("DataStoreManager","SENDING TOKEN TO BACKEND $token")
        return api.registerStepTwo(request, token)
    }

    suspend fun login(request: UserLoginRequestDTO): Response<LoginResponseDTO> {
        return api.loginUser(request)
    }
}
