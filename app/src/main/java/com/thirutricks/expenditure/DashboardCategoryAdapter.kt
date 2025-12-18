package com.thirutricks.expenditure

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thirutricks.expenditure.model.DashboardCategory

class DashboardCategoryAdapter(private var categories: List<DashboardCategory>) :
    RecyclerView.Adapter<DashboardCategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
        val tvCategoryAmount: TextView = view.findViewById(R.id.tvCategoryAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategoryName.text = category.category
        holder.tvCategoryAmount.text = "₹${category.totalExpense}"
    }

    override fun getItemCount() = categories.size
    
    fun updateData(newCategories: List<DashboardCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
