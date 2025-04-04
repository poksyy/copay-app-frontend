import android.util.Log
import com.google.gson.Gson
import com.copay.app.dto.response.LoginResponseDTO
import com.copay.app.dto.response.RegisterStepTwoResponseDTO
import com.copay.app.utils.state.AuthState
import com.copay.app.viewmodel.UserViewModel

class UserService(private val userViewModel: UserViewModel) {

    fun handleBackendResponse(response: AuthState.Success) {
        val gson = Gson()

        // Log para ver la respuesta completa que estamos recibiendo
        Log.d("UserService", "Backend Response: ${gson.toJson(response)}")

        // Aquí tomamos el objeto `user` directamente (que es de tipo Any?)
        val userJson = gson.toJson(response.user)

        // Log para ver el JSON generado a partir del objeto user
        Log.d("UserService", "User JSON: $userJson")

        // Intentamos convertir el objeto `user` al tipo adecuado
        when (val data = response.user) {
            is LoginResponseDTO -> {
                // Log para ver los valores antes de pasarlos al ViewModel
                Log.d("UserService", "LoginResponseDTO received: phoneNumber=${data.phoneNumber}, userId=${data.userId}, username=${data.username}, email=${data.email}")

                val phoneNumber = data.phoneNumber
                val userId = data.userId
                val username = data.username
                val email = data.email

                // Pasamos los datos al UserViewModel
                userViewModel.setUser(phoneNumber, userId, username, email)

                // Log para verificar si el ViewModel se actualizó correctamente
                Log.d("UserService", "User data passed to UserViewModel: phoneNumber=$phoneNumber, userId=$userId, username=$username, email=$email")
            }
            is RegisterStepTwoResponseDTO -> {
                // Log para ver los valores antes de pasarlos al ViewModel
                Log.d("UserService", "RegisterStepTwoResponseDTO received: phoneNumber=${data.phoneNumber}, userId=${data.userId}, username=${data.username}, email=${data.email}")

                val phoneNumber = data.phoneNumber
                val userId = data.userId
                val username = data.username
                val email = data.email

                // Pasamos los datos al UserViewModel
                userViewModel.setUser(phoneNumber, userId, username, email)

                // Log para verificar si el ViewModel se actualizó correctamente
                Log.d("UserService", "User data passed to UserViewModel: phoneNumber=$phoneNumber, userId=$userId, username=$username, email=$email")
            }
            else -> {
                // Si el tipo de respuesta no es uno de los que conocemos, logueamos el tipo
                Log.d("UserService", "Unexpected response type: $data")
            }
        }
    }
}
