package com.copay.app.validation

import android.util.Patterns

object UserValidation {

    /** GENERAL VALIDATIONS **/
    // Validate if field is not empty.
    fun validateNotEmpty(value: String, fieldName: String): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult(false, "$fieldName cannot be empty")
        } else {
            ValidationResult(true)
        }
    }

    // Validate if the username is not empty and has a valid length.
    fun validateUsername(username: String): ValidationResult {
        val emptyCheck = validateNotEmpty(username, "Username")
        if (!emptyCheck.isValid) return emptyCheck

        return when {
            username.length < 3 || username.length > 15 -> ValidationResult(
                false, "Username must be between 3 and 15 characters"
            )

            else -> ValidationResult(true)
        }
    }

    // Validate if the phone number is not empty, has a valid length and correct format.
    fun validatePhoneNumber(phoneNumber: String, dialCode: String): ValidationResult {
        val emptyCheck = validateNotEmpty(phoneNumber, "Phone number")
        if (!emptyCheck.isValid) return emptyCheck

        val onlyDigits = phoneNumber.all { it.isDigit() }
        if (!onlyDigits) return ValidationResult(false, "Phone number must contain only digits")

        // Rules by country (examples)
        return when (dialCode) {
            "+34" -> { // Spain
                if (phoneNumber.length != 9) ValidationResult(false, "Phone number must be 9 digits")
                else ValidationResult(true)
            }
            "+52" -> { // Mexico
                if (phoneNumber.length != 10) ValidationResult(false, "Phone number must be 10 digits")
                else ValidationResult(true)
            }
            "+1" -> { // USA/Canada
                if (phoneNumber.length != 10) ValidationResult(false, "Phone number must be 10 digits")
                else ValidationResult(true)
            }
            "+49" -> { // Germany
                if (phoneNumber.length != 11) ValidationResult(false, "Phone number must be 11 digits")
                else ValidationResult(true)
            }
            "+54" -> { // Argentina
                if (phoneNumber.length != 10) ValidationResult(false, "Phone number must be 10 digits")
                else ValidationResult(true)
            }
            "+33" -> { // France
                if (phoneNumber.length != 9) ValidationResult(false, "Phone number must be 9 digits")
                else ValidationResult(true)
            }
            "+39" -> { // Italy
                if (phoneNumber.length != 10) ValidationResult(false, "Phone number must be 10 digits")
                else ValidationResult(true)
            }
            "+55" -> { // Brazil
                if (phoneNumber.length != 11) ValidationResult(false, "Phone number must be 11 digits")
                else ValidationResult(true)
            }
            "+57" -> { // Colombia
                if (phoneNumber.length != 10) ValidationResult(false, "Phone number must be 10 digits")
                else ValidationResult(true)
            }
            "+56" -> { // Chile
                if (phoneNumber.length != 9) ValidationResult(false, "Phone number must be 9 digits")
                else ValidationResult(true)
            }
            "+51" -> { // Peru
                if (phoneNumber.length != 9) ValidationResult(false, "Phone number must be 9 digits")
                else ValidationResult(true)
            }
            "+91" -> { // India
                if (phoneNumber.length != 10) ValidationResult(false, "Phone number must be 10 digits")
                else ValidationResult(true)
            }
            "+86" -> { // China
                if (phoneNumber.length != 11) ValidationResult(false, "Phone number must be 11 digits")
                else ValidationResult(true)
            }
            "+81" -> { // Japan
                if (phoneNumber.length != 11) ValidationResult(false, "Phone number must be 11 digits")
                else ValidationResult(true)
            }
            "+61" -> { // Australia
                if (phoneNumber.length != 9) ValidationResult(false, "Phone number must be 9 digits")
                else ValidationResult(true)
            }
            "+7" -> { // Russia
                if (phoneNumber.length != 11) ValidationResult(false, "Phone number must be 11 digits")
                else ValidationResult(true)
            }
            else -> {
                if (phoneNumber.length < 6 || phoneNumber.length > 15)
                    ValidationResult(false, "Phone number must be between 6 and 15 digits")
                else ValidationResult(true)
            }
        }
    }

    // Validate if the email format is correct.
    fun validateEmail(email: String): ValidationResult {
        val emptyCheck = validateNotEmpty(email, "Email")
        if (!emptyCheck.isValid) return emptyCheck

        return when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(
                false, "Invalid email format"
            )

            else -> ValidationResult(true)
        }
    }

    /** SPECIFIC VALIDATIONS **/
    /** Register STEP ONE **/
    // Validate if the password meets security requirements.
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.length < 8 -> ValidationResult(
                false, "Password must be at least 8 characters long"
            )

            !password.any { it.isUpperCase() } -> ValidationResult(
                false, "Password must contain at least one uppercase letter"
            )

            !password.any { it.isDigit() } -> ValidationResult(
                false, "Password must contain at least one number"
            )

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
}

/** Wrapper class for validation results */
data class ValidationResult(val isValid: Boolean, val errorMessage: String? = null)