package com.thirutricks.expenditure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirutricks.expenditure.api.RetrofitClient
import com.thirutricks.expenditure.databinding.FragmentLendingBinding
import com.thirutricks.expenditure.model.LendingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LendingFragment : Fragment() {

    private var _binding: FragmentLendingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LendingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadLendingList()
    }

    private fun setupRecyclerView() {
        adapter = LendingAdapter(emptyList())
        binding.rvLending.layoutManager = LinearLayoutManager(context)
        binding.rvLending.adapter = adapter
    }

    private fun loadLendingList() {
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.getLendingList(1, 100, "all").enqueue(object : Callback<LendingResponse> {
            override fun onResponse(
                call: Call<LendingResponse>,
                response: Response<LendingResponse>
            ) {
                if (_binding != null) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val lendingResponse = response.body()!!
                        if (lendingResponse.status == "success") {
                            adapter.updateData(lendingResponse.data.lending)
                        } else {
                            context?.let { Toast.makeText(it, "Failed to load lending list", Toast.LENGTH_SHORT).show() }
                        }
                    } else {
                        context?.let { Toast.makeText(it, "Error: ${response.code()}", Toast.LENGTH_SHORT).show() }
                    }
                }
            }

            override fun onFailure(call: Call<LendingResponse>, t: Throwable) {
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
