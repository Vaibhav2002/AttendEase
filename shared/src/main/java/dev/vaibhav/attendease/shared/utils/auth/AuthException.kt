package dev.vaibhav.attendease.shared.utils.auth

open class AuthException(override val message: String) : Exception(message)

class AuthCancellationException : AuthException("User cancelled Google Sign in")
class InvalidDomainException : AuthException("Invalid domain")