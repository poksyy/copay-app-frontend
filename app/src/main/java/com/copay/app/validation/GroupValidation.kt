package com.copay.app.validation

object GroupValidation {

    fun validateGroupName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "Group name cannot be empty")
            name.length < 3 -> ValidationResult(false, "Group name must be at least 3 characters")
            name.length > 25 -> ValidationResult(false, "Group name cannot exceed 25 characters")
            else -> ValidationResult(true, null)
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
        return if (price.isBlank()) {
            ValidationResult(false, "Price cannot be empty")
        } else {
            try {
                val priceValue = price.toFloat()
                if (priceValue < 0) {
                    ValidationResult(false, "Price cannot be negative")
                } else {
                    ValidationResult(true, null)
                }
            } catch (e: NumberFormatException) {
                ValidationResult(false, "Price must be a valid number")
            }
        }
    }

    fun validateGroupDescription(description: String): ValidationResult {
        return if (description.length >= 150) {
            ValidationResult(false, "Description cannot exceed 150 characters")
        } else {
            ValidationResult(true, null)
        }
    }

    // Validation for the group members.
    fun validateMaxInvitedExternalMembers(members: List<String>): ValidationResult {
        return when {
            members.isEmpty() -> {
                ValidationResult(false, "Members list cannot be empty")
            }
            members.size > 15 -> {
                ValidationResult(false, "You cannot add more than 15 members")
            }
            members.any { it.isBlank() } -> {
                ValidationResult(false, "Member name cannot be empty")
            }
            else -> {
                ValidationResult(true, null)
            }
        }
    }

    fun validateMaxInvitedCopayMembers(phoneNumbers: List<String>): ValidationResult {
        return if (phoneNumbers.size > 15) {
            ValidationResult(false, "You cannot invite more than 15 people")
        } else {
            ValidationResult(true, null)
        }
    }
}
