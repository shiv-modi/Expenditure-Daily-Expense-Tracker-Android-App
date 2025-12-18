package com.thirutricks.expenditure.utils

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * NetworkErrorHandler provides utility methods for handling network errors
 * and HTTP status codes with user-friendly messages.
 */
object NetworkErrorHandler {
    
    /**
     * Maps HTTP status codes to user-friendly error messages.
     * @param statusCode The HTTP status code
     * @return User-friendly error message
     */
    fun getHttpErrorMessage(statusCode: Int): String {
        return when (statusCode) {
            400 -> "Invalid request"
            401 -> "Invalid credentials"
            403 -> "Access forbidden"
            404 -> "Service not found"
            408 -> "Request timeout"
            500 -> "Server error. Please try again later"
            502 -> "Bad gateway. Please try again later"
            503 -> "Service unavailable. Please try again later"
            504 -> "Gateway timeout. Please try again later"
            else -> "Request failed. Please try again"
        }
    }
    
    /**
     * Maps common network exceptions to user-friendly error messages.
     * @param throwable The exception that occurred
     * @return User-friendly error message
     */
    fun getNetworkErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> "No internet connection"
            is SocketTimeoutException -> "Connection timeout"
            is ConnectException -> "Could not connect to server"
            is SSLException -> "Secure connection failed"
            else -> "Network error. Please check your connection"
        }
    }
}
