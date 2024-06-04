package com.example.auth.domain

import com.example.auth.domain.UserDataValidator.Companion.MIN_PASSWORD_LENGTH


interface UserDataValidator {
    fun isValidEmail(email: String): Boolean
    fun validatePassword(password: String): PasswordValidationState

    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }
}
class UserDataValidatorImpl(
    private val patternValidator: PatternValidator
) : UserDataValidator {

    override fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    override fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasUpperCase = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasUpperCaseCharacter = hasUpperCase,
            hasLowerCaseCharacter = hasLowercase,
            hasNumber = hasDigit,
            hasMinLength = hasMinLength
        )
    }
}