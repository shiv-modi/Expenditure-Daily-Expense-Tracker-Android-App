package com.thirutricks.expenditure.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitClient provides a singleton instance of the Retrofit API client.
 * Configured with authentication interceptor and logging for API requests.
 */
object RetrofitClient {

    private const val BASE_URL = "https://expenditure.iceiy.com/"

    private var authToken: String? = null

    /**
     * Sets the authentication token to be used in API requests.
     * @param token The JWT authentication token
     */
    fun setAuthToken(token: String) {
        authToken = token
    }

    /**
     * Interceptor that adds Authorization header to all requests if token is available.
     */
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
        
        authToken?.let {
            builder.header("Authorization", "Bearer $it")
        }
        
        val request = builder.build()
        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: ExpenseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ExpenseApiService::class.java)
    }
}
