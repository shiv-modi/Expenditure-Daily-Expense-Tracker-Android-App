package com.thirutricks.expenditure

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thirutricks.expenditure.api.RetrofitClient
import com.thirutricks.expenditure.databinding.ActivityLoginBinding
import com.thirutricks.expenditure.model.LoginRequest
import com.thirutricks.expenditure.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: com.thirutricks.expenditure.utils.SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sessionManager = com.thirutricks.expenditure.utils.SessionManager(this)

        // Check if user is already logged in
        if (sessionManager.isTokenValid()) {
            val token = sessionManager.getToken()
            if (token != null) {
                RetrofitClient.setAuthToken(token)
                goToMainActivity()
                return
            }
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        val request = LoginRequest(email = email, password = password)
        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    if (loginResponse.accessToken != null) {
                        // Save token
                        sessionManager.saveToken(loginResponse.accessToken)
                        RetrofitClient.setAuthToken(loginResponse.accessToken)
                        goToMainActivity()
                    } else {
                        val message = loginResponse.message.ifEmpty { "Login failed" }
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "Invalid credentials"
                        500 -> "Server error. Please try again later"
                        else -> "Login failed: ${response.message()}"
                    }
                    Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                val errorMsg = when {
                    t is java.net.UnknownHostException -> "No internet connection"
                    t is java.net.SocketTimeoutException -> "Connection timeout"
                    else -> "Network error: ${t.message}"
                }
                Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
