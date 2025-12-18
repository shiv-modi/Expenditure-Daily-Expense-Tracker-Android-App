package com.thirutricks.expenditure.utils

/**
 * ValidationUtils provides common validation methods for user inputs.
 */
object ValidationUtils {
    
    /**
     * Validates and parses a numeric amount string.
     * @param amountStr The string to validate and parse
     * @param fieldName The name of the field for error messages (e.g., "cost", "amount")
     * @return ValidationResult containing either the parsed value or an error message
     */
    fun validateAmount(amountStr: String, fieldName: String = "amount"): ValidationResult {
        if (amountStr.isEmpty()) {
            return ValidationResult.Error("Please enter $fieldName")
        }
        
        val amount = try {
            amountStr.toDouble()
        } catch (e: NumberFormatException) {
            return ValidationResult.Error("Please enter a valid $fieldName")
        }
        
        if (amount <= 0) {
            return ValidationResult.Error("${fieldName.capitalize()} must be greater than 0")
        }
        
        return ValidationResult.Success(amount)
    }
    
    /**
     * Result of a validation operation.
     */
    sealed class ValidationResult {
        data class Success(val value: Double) : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }
    
    /**
     * Capitalizes the first character of the string.
     */
    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
