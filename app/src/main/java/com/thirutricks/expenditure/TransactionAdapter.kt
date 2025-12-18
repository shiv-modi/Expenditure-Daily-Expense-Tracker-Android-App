package com.thirutricks.expenditure

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thirutricks.expenditure.model.Transaction
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvType: TextView = view.findViewById(R.id.tvType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.tvCategory.text = transaction.category
        holder.tvDescription.text = transaction.description
        holder.tvDate.text = transaction.date
        holder.tvAmount.text = String.format("₹%.2f", transaction.amount)
        holder.tvType.text = transaction.type

        if (transaction.type.equals("Income", ignoreCase = true)) {
            holder.tvAmount.setTextColor(Color.parseColor("#4CAF50")) // Green
            holder.tvType.setBackgroundColor(Color.parseColor("#E8F5E9"))
            holder.tvType.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            holder.tvAmount.setTextColor(Color.parseColor("#F44336")) // Red
            holder.tvType.setBackgroundColor(Color.parseColor("#FFEBEE"))
            holder.tvType.setTextColor(Color.parseColor("#C62828"))
        }
    }

    override fun getItemCount() = transactions.size

    fun updateData(newTransactions: List<Transaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
