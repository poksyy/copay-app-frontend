package com.copay.app.validation

object GroupValidation {

    fun validateGroupName(name: String): ValidationResult {
        return if (name.isBlank()) {
            ValidationResult(false, "Group name cannot be empty")
        } else if (name.length > 25) {
            ValidationResult(false, "Group name cannot exceed 25 characters")
        } else {
            ValidationResult(true, null)
        }
    }

    fun validateCurrency(currency: String): ValidationResult {
        return if (currency.isBlank()) {
            ValidationResult(false, "Currency cannot be empty")
        } else {
            ValidationResult(true, null)
        }
    }

    fun validateEstimatedPrice(price: String): ValidationResult {
        val priceValue = price.toIntOrNull()
        return if (price.isBlank()) {
            ValidationResult(false, "Price cannot be empty")
        } else if (priceValue == null || priceValue < 0 || priceValue > 10000000) {
            ValidationResult(false, "Price must be a valid number between 0 and 10,000,000")
        } else {
            ValidationResult(true, null)
        }
    }

    fun validateGroupDescription(description: String): ValidationResult {
        return if (description.length > 50) {
            ValidationResult(false, "Description cannot exceed 50 characters")
        } else {
            ValidationResult(true, null)
        }
    }

    fun validateMaxPhoneNumbers(phoneNumbers: List<String>): ValidationResult {
        return if (phoneNumbers.size > 15) {
            ValidationResult(false, "You cannot invite more than 15 people")
        } else {
            ValidationResult(true, null)
        }
    }
}
