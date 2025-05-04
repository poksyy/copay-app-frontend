package com.copay.app.factory

import com.copay.app.config.RetrofitInstance
import com.copay.app.repository.GroupRepository
import com.copay.app.repository.ProfileRepository
import com.copay.app.repository.UserRepository
import com.copay.app.service.AuthService
import com.copay.app.service.ExpenseService
import com.copay.app.service.GroupService
import com.copay.app.service.ProfileService
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

    /*    ========================================================================
          ============================= SERVICES ===============================
          ========================================================================

          These are the services responsible for the application's core functionalities
          like authentication and group management. They are provided as singletons
          to ensure there is only one instance of each service in the entire app.

          ========================================================================
          ========================================================================
          ========================================================================
    */

    // Provides the AuthService dependency. A single instance will be used throughout the app.
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        // Instantiates and returns AuthService using RetrofitInstance.
        return AuthService(RetrofitInstance.api)
    }

    // Provides the GroupService dependency. A single instance will be used throughout the app.
    @Provides
    @Singleton
    fun provideGroupService(): GroupService {
        // Instantiates and returns GroupService using RetrofitInstance.
        return GroupService(RetrofitInstance.api)
    }
    
    // Provides the ProfileService dependency. A single instance will be used throughout the app.
    @Provides
    @Singleton
    fun provideProfileService(): ProfileService {
        // Instantiates and returns ProfileService using RetrofitInstance.
        return ProfileService(RetrofitInstance.api)
    }

    @Provides
    @Singleton
    fun provideExpenseService(): ExpenseService {
        // Instantiates and returns ExpenseService using RetrofitInstance.
        return ExpenseService(RetrofitInstance.api)
    }
    
    /*    ========================================================================
          =========================== REPOSITORIES =============================
          ========================================================================

          These repositories manage the interaction with the data layer, such as
          user information and groups. Each repository is provided as a singleton
          and is injected with the necessary service dependencies.

          ========================================================================
          ========================================================================
          ========================================================================
    */

    // Provides the UserRepository dependency. A single instance will be used throughout the app.
    @Provides
    @Singleton
    fun provideUserRepository(authService: AuthService): UserRepository {
        // Instantiates and returns UserRepository with injected AuthService.
        return UserRepository(authService)
    }

    // Provides the GroupRepository dependency. A single instance will be used throughout the app.
    @Provides
    @Singleton
    fun provideGroupRepository(groupService: GroupService): GroupRepository {
        // Instantiates and returns UserRepository with injected AuthService.
        return GroupRepository(groupService)
    }

    // Provides the ProfileRepository dependency. A single instance will be used throughout the app.
    @Provides
    @Singleton
    fun provideProfileRepository(profileService: ProfileService): ProfileRepository {
        // Instantiates and returns ProfileRepository with injected ProfileService.
        return ProfileRepository(profileService)
    }
}
