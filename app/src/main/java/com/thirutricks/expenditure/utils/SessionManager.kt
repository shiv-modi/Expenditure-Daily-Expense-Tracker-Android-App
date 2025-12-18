package com.thirutricks.expenditure.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject

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
     */
    fun isTokenValid(): Boolean {
        val token = getToken() ?: return false
        
        // If token exists, check expiry
        return !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        try {
            val parts = token.split(".")
            if (parts.size != 3) {
                return false // Invalid format, treat as not expired or invalid? If invalid format, let's say it IS expired/invalid.
            }
            // However, the prompt says "if jwt token is expired". If invalid, we probably can't use it.
            // Let's decode payload
            val payload = parts[1]
            // Url safe decoding
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes)
            val jsonObject = JSONObject(decodedString)
            
            if (jsonObject.has("exp")) {
                val exp = jsonObject.getLong("exp")
                val currentTime = System.currentTimeMillis() / 1000
                return currentTime > exp
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // If we can't parse it, assume it's valid (lifetime) as per user request "if not then use it for lifetime"
            // Or if it fails to parse, maybe it's not a JWT? 
            // The user said: "yes jwt will have expiry, if not then use it for lifetime"
            // So if parsing fails or no exp, return false (not expired).
            return false
        }
        return false // Default to not expired
    }
}
