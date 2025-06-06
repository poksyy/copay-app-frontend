package com.copay.app.factory

import com.copay.app.config.RetrofitInstance
import com.copay.app.repository.*
import com.copay.app.service.*
import com.copay.app.utils.session.UserSession
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

    @Provides
    @Singleton
    fun providePaymentConfirmationService(): PaymentConfirmationService {
        // Instantiates and returns PaymentConfirmationService using RetrofitInstance.
        return PaymentConfirmationService(RetrofitInstance.api)
    }

    @Provides
    @Singleton
    fun provideNotificationService(): NotificationService {
        // Instantiates and returns NotificationService using RetrofitInstance.
        return NotificationService(RetrofitInstance.api)
    }

    @Provides
    @Singleton
    fun providePhotoService(): PhotoService {
        // Instantiates and returns PhotoService using RetrofitInstance.
        return PhotoService(RetrofitInstance.api)
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
    fun provideProfileRepository(
        profileService: ProfileService, userSession: UserSession
    ): ProfileRepository {
        // Instantiates and returns ProfileRepository with injected ProfileService.
        return ProfileRepository(
            profileService, userSession
        )
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(expenseService: ExpenseService): ExpenseRepository {
        // Instantiates and returns ProfileRepository with injected ProfileService.
        return ExpenseRepository(expenseService)
    }

    @Provides
    @Singleton
    fun providePaymentConfirmationRepository(paymentConfirmationService: PaymentConfirmationService): PaymentConfirmationRepository {
        // Instantiates and returns PaymentConfirmationRepository with injected ProfileService.
        return PaymentConfirmationRepository(paymentConfirmationService)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationService: NotificationService): NotificationRepository {
        // Instantiates and returns NotificationRepository with injected NotificationService.
        return NotificationRepository(notificationService)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(photoService: PhotoService): PhotoRepository {
        // Instantiates and returns PhotoRepository with injected PhotoService.
        return PhotoRepository(photoService)
    }
}
