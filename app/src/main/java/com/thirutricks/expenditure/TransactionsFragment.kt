package com.thirutricks.expenditure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirutricks.expenditure.api.RetrofitClient
import com.thirutricks.expenditure.databinding.FragmentTransactionsBinding
import com.thirutricks.expenditure.model.TransactionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadTransactions()
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter(emptyList())
        binding.rvTransactions.layoutManager = LinearLayoutManager(context)
        binding.rvTransactions.adapter = adapter
    }

    private fun loadTransactions() {
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.getTransactions(1, 100, "all").enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(
                call: Call<TransactionResponse>,
                response: Response<TransactionResponse>
            ) {
                if (_binding != null) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val transactionResponse = response.body()!!
                        if (transactionResponse.status == "success") {
                            adapter.updateData(transactionResponse.data.transactions)
                        } else {
                            context?.let { Toast.makeText(it, "Failed to load transactions", Toast.LENGTH_SHORT).show() }
                        }
                    } else {
                        context?.let { Toast.makeText(it, "Error: ${response.code()}", Toast.LENGTH_SHORT).show() }
                    }
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                if (_binding != null) {
                    binding.progressBar.visibility = View.GONE
                    context?.let { Toast.makeText(it, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show() }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
