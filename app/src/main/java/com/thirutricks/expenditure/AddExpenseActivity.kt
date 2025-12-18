package com.thirutricks.expenditure

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thirutricks.expenditure.api.RetrofitClient
import com.thirutricks.expenditure.databinding.ActivityAddExpenseBinding
import com.thirutricks.expenditure.model.ApiResponse
import com.thirutricks.expenditure.model.Category
import com.thirutricks.expenditure.model.CategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private var categories: List<Category> = emptyList()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatePicker()
        fetchCategories()

        binding.btnSave.setOnClickListener {
            saveExpense()
        }
    }

    private fun setupDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()
        }

        binding.etDate.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        updateDateLabel() // Set current date initially
    }

    private fun updateDateLabel() {
        val myFormat = "yyyy-MM-dd" // API format likely requires this
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etDate.setText(sdf.format(calendar.time))
    }

    private fun fetchCategories() {
        RetrofitClient.instance.getCategories(null).enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val catResponse = response.body()!!
                    val allCategories = catResponse.categories ?: emptyList()

                    categories = allCategories.filter { it.mode.equals("Expense", ignoreCase = true) }
                    
                    if (categories.isEmpty() && allCategories.isNotEmpty()) {
                         categories = allCategories
                    }
                    
                    val adapter = ArrayAdapter(
                        this@AddExpenseActivity,
                        android.R.layout.simple_spinner_item,
                        categories.map { it.categoryName }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerCategory.adapter = adapter
                } else {
                    Toast.makeText(this@AddExpenseActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(this@AddExpenseActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveExpense() {
        val date = binding.etDate.text.toString()
        val costStr = binding.etCost.text.toString()
        val description = binding.etDescription.text.toString()
        
        if (costStr.isEmpty()) {
            Toast.makeText(this, "Please enter cost", Toast.LENGTH_SHORT).show()
            return
        }

        if (categories.isEmpty()) {
             Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show()
             return
        }

        val categoryIndex = binding.spinnerCategory.selectedItemPosition
        if (categoryIndex == -1) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedCategory = categories[categoryIndex]

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.setEnabled(false)

        RetrofitClient.instance.addExpense(
            date = date,
            categoryId = selectedCategory.categoryId,
            cost = costStr.toDouble(),
            description = description
        ).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                binding.progressBar.visibility = View.GONE
                binding.btnSave.setEnabled(true)
                
                if (response.isSuccessful) {
                    Toast.makeText(this@AddExpenseActivity, "Expense Added!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddExpenseActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.btnSave.setEnabled(true)
                Toast.makeText(this@AddExpenseActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
