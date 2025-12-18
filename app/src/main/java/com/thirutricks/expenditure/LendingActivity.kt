package com.thirutricks.expenditure

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirutricks.expenditure.api.RetrofitClient
import com.thirutricks.expenditure.databinding.ActivityLendingBinding
import com.thirutricks.expenditure.model.LendingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LendingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLendingBinding
    private lateinit var adapter: LendingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadLendingList()
    }

    private fun setupRecyclerView() {
        adapter = LendingAdapter(emptyList())
        binding.rvLending.layoutManager = LinearLayoutManager(this)
        binding.rvLending.adapter = adapter
    }

    private fun loadLendingList() {
        binding.progressBar.visibility = View.VISIBLE
        val service = RetrofitClient.instance

        // Using default page=1, limit=100, status="all"
        service.getLendingList(1, 100, "all").enqueue(object : Callback<LendingResponse> {
            override fun onResponse(
                call: Call<LendingResponse>,
                response: Response<LendingResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val lendingResponse = response.body()!!
                    if (lendingResponse.status == "success") {
                        adapter.updateData(lendingResponse.data.lending)
                    } else {
                        Toast.makeText(this@LendingActivity, "Failed to load lending list", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LendingActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LendingResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@LendingActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
