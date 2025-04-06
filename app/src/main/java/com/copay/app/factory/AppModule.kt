package com.copay.app.factory

import com.copay.app.config.RetrofitInstance
import com.copay.app.repository.UserRepository
import com.copay.app.service.AuthService
import com.copay.app.service.UserService
import com.copay.app.utils.state.UserSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
* AppModule is a Dagger module responsible for providing the application's dependencies.
* It contains @Provides methods that define how to instantiate classes such as Repositories, Services...
* These dependencies are injected throughout the app using Hilt.

* Note: For ViewModels use the @HiltViewModel annotation directly in the ViewModel class.
* There is no need to provide them in this module, as Hilt automatically handles their injection.
*
* Annotations:
* - @Module: Marks this class as a Dagger module where dependencies are provided.
* - @InstallIn(SingletonComponent::class): Specifies that the provided dependencies are available for the entire application lifecycle.
* - @Provides: Marks a method as a provider of a dependency.
* - @Singleton: Ensures only one instance of the provided dependency is used throughout the application.
*/

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides the AuthService dependency. A single instance will be used throughout the app.
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        // Instantiates and returns AuthService using RetrofitInstance.
        return AuthService(RetrofitInstance.api)
    }

    // Provides the UserRepository dependency. A single instance will be used throughout the app.
    @Provides
    @Singleton
    fun provideUserRepository(authService: AuthService): UserRepository {
        // Instantiates and returns UserRepository with injected AuthService.
        return UserRepository(authService)
    }

    // Singleton to create only 1 instance of UserService.
    @Singleton
    fun provideUserService(): UserService {
        // Instantiates and returns UserSession.
        return UserService()
    }

    // Singleton to create only 1 instance of UserSession.
    @Singleton
    fun provideUserSession(): UserSession {
        // Instantiates and returns UserSession.
        return UserSession()
    }
}
