package com.thirutricks.expenditure

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.thirutricks.expenditure.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set initial fragment
        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }

        // Setup Bottom Navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    loadFragment(DashboardFragment())
                    true
                }
                R.id.nav_transactions -> {
                    loadFragment(TransactionsFragment())
                    true
                }
                R.id.nav_lending -> {
                    loadFragment(LendingFragment())
                    true
                }
                else -> false
            }
        }

        binding.fabAdd.setOnClickListener {
            showAddOptions()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun showAddOptions() {
        val options = arrayOf("Add Expense", "Add Income")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> startActivity(Intent(this, AddExpenseActivity::class.java))
                1 -> startActivity(Intent(this, AddIncomeActivity::class.java))
            }
        }
        builder.show()
    }
    
    override fun onResume() {
        super.onResume()
    }
}
