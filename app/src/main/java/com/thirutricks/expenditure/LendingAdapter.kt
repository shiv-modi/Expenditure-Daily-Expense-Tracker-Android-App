package com.thirutricks.expenditure

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thirutricks.expenditure.model.Lending

class LendingAdapter(private var lendings: List<Lending>) :
    RecyclerView.Adapter<LendingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lending, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lending = lendings[position]

        holder.tvName.text = lending.name
        holder.tvAmount.text = String.format("₹%.2f", lending.amount)
        holder.tvDescription.text = lending.description
        holder.tvDate.text = lending.dateOfLending
        holder.tvStatus.text = lending.displayStatus

        if (lending.status.equals("received", ignoreCase = true)) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Green
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")) // Orange
        }
    }

    override fun getItemCount() = lendings.size

    fun updateData(newLendings: List<Lending>) {
        lendings = newLendings
        notifyDataSetChanged()
    }
}
