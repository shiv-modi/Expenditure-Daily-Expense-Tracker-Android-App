package com.thirutricks.expenditure

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirutricks.expenditure.api.RetrofitClient
import com.thirutricks.expenditure.databinding.ActivityTransactionsBinding
import com.thirutricks.expenditure.model.TransactionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionsBinding
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadTransactions()
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(emptyList())
        binding.rvTransactions.layoutManager = LinearLayoutManager(this)
        binding.rvTransactions.adapter = adapter
    }

    private fun loadTransactions() {
        binding.progressBar.visibility = View.VISIBLE
        val service = RetrofitClient.instance

        // Using default page=1, limit=100, type="all" for now
        service.getTransactions(1, 100, "all").enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(
                call: Call<TransactionResponse>,
                response: Response<TransactionResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val transactionResponse = response.body()!!
                    if (transactionResponse.status == "success") {
                        adapter.updateData(transactionResponse.data.transactions)
                    } else {
                        Toast.makeText(this@TransactionsActivity, "Failed to load transactions", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@TransactionsActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@TransactionsActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
