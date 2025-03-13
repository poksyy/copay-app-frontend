import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.copay.app.repository.UserRepository
import com.copay.app.viewmodel.AuthViewModel

/**
 * Factory class for creating an instance of AuthViewModel with required dependencies.
 * Ensures that UserRepository is properly injected into AuthViewModel.
 */

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel is type AuthViewModel.
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            // Create and return an instance of AuthViewModel with UserRepository injected.
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
