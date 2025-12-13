package com.thirutricks.expenditure

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirutricks.expenditure.api.RetrofitClient
import com.thirutricks.expenditure.databinding.FragmentDashboardBinding
import com.thirutricks.expenditure.model.DashboardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: DashboardCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = DashboardCategoryAdapter(emptyList())
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
            isNestedScrollingEnabled = false
        }

        fetchDashboardData()
    }

    override fun onResume() {
        super.onResume()
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        RetrofitClient.instance.getDashboard().enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val dashboardData = response.body()!!.data
                    if (_binding != null) {
                        updateUI(dashboardData)
                    }
                } else {
                    context?.let {
                        Toast.makeText(it, "Failed to load dashboard", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                context?.let {
                    Toast.makeText(it, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateUI(data: com.thirutricks.expenditure.model.DashboardData) {
        binding.tvWelcome.text = "Hello, ${data.user.name}"
        
        binding.tvBalance.text = "₹${data.balance}"
        binding.tvTotalIncome.text = "₹${data.totalIncome}"
        binding.tvTotalExpense.text = "₹${data.totalExpense}"
        
        binding.tvTodayExpense.text = "₹${data.todayExpense}"
        binding.tvYesterdayExpense.text = "₹${data.yesterdayExpense}"
        binding.tvMonthlyExpense.text = "₹${data.monthlyExpense}"

        categoryAdapter.updateData(data.categories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
