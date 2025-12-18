package com.thirutricks.expenditure.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject

/**
 * SessionManager handles user authentication token storage and validation.
 * Uses SharedPreferences to persist JWT tokens and validates token expiry.
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREF_NAME = "pith_expense_session"
        const val KEY_ACCESS_TOKEN = "access_token"
    }

    /**
     * Saves the auth token.
     */
    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(KEY_ACCESS_TOKEN, token)
        editor.apply()
    }

    /**
     * Retrieves the auth token.
     */
    fun getToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    /**
     * Clears the session.
     */
    fun clearToken() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * Checks if a token exists and is not expired.
     * @return true if token is valid and not expired, false otherwise
     */
    fun isTokenValid(): Boolean {
        val token = getToken() ?: return false
        
        // If token exists, check expiry
        return !isTokenExpired(token)
    }

    /**
     * Validates JWT token expiry by decoding the payload and checking the 'exp' claim.
     * @param token The JWT token to validate
     * @return true if token is expired or invalid, false if valid
     */
    private fun isTokenExpired(token: String): Boolean {
        try {
            val parts = token.split(".")
            if (parts.size != 3) {
                // Invalid JWT format, treat as expired
                return true
            }
            
            val payload = parts[1]
            // URL safe decoding for JWT tokens
            // Try URL_SAFE first (with padding), then NO_PADDING if that fails
            val decodedBytes = try {
                Base64.decode(payload, Base64.URL_SAFE)
            } catch (e: IllegalArgumentException) {
                // Try without padding for JWT tokens that don't include padding
                Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING)
            }
            val decodedString = String(decodedBytes)
            val jsonObject = JSONObject(decodedString)
            
            if (jsonObject.has("exp")) {
                val exp = jsonObject.getLong("exp")
                val currentTime = System.currentTimeMillis() / 1000
                return currentTime > exp
            }
            
            // If no expiry field, treat token as valid for lifetime
            return false
        } catch (e: Exception) {
            // Log error but don't expose sensitive information
            android.util.Log.e("SessionManager", "Error parsing token", e)
            // If parsing fails, assume token is expired/invalid for security
            return true
        }
    }
}
