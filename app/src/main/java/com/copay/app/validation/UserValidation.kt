package com.copay.app.validation

import android.util.Patterns

object UserValidation {

    /** Validations for Login **/
    // Validate if the username is not empty.
    fun validateLoginPhoneNumber(username: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult(false, "Phone number cannot be empty")
            else -> ValidationResult(true)
        }
    }

    // Validate if the password is not empty.
    fun validateLoginPassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "Password cannot be empty")
            else -> ValidationResult(true)
        }
    }

    /** Validations for Register **/
    /** STEP ONE **/
    // Validate if the username is not empty and has a valid length.
    fun validateRegisterUsername(username: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult(false, "Username cannot be empty")
            username.length < 3 || username.length > 15 -> ValidationResult(false, "Username must be between 3 and 15 characters")
            else -> ValidationResult(true)
        }
    }

    // Validate if the email format is correct.
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(false, "Invalid email format")
            else -> ValidationResult(true)
        }
    }

    // Validate if the password meets security requirements.
    fun validateRegisterPassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "Password cannot be empty")
            password.length < 8 -> ValidationResult(false, "Password must be at least 8 characters long")
            !password.any { it.isUpperCase() } -> ValidationResult(false, "Password must contain at least one uppercase letter")
            else -> ValidationResult(true)
        }
    }

    // Validate if both passwords match (for registration).
    fun validatePasswordMatch(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult(false, "Passwords must match")
            password != confirmPassword -> ValidationResult(false, "Passwords must match")
            else -> ValidationResult(true)
        }
    }

    /** STEP TWO **/
    // Validate if the phone number is not empty and has a valid length.
    fun validateRegisterPhoneNumber(phoneNumber: String): ValidationResult {
        return when {
            phoneNumber.isBlank() -> ValidationResult(false, "PhoneNumber cannot be empty")
            else -> ValidationResult(true)
        }
    }
}

/** Wrapper class for validation results */
data class ValidationResult(val isValid: Boolean, val errorMessage: String? = null)
