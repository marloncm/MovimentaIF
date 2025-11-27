package com.ifrs.movimentaif.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ifrs.movimentaif.R

class WorkoutHistoryAdapter : ListAdapter<WorkoutHistoryItem, WorkoutHistoryAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDayOfWeek: TextView = view.findViewById(R.id.tvDayOfWeek)
        val tvCount: TextView = view.findViewById(R.id.tvCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvDate.text = item.date
        holder.tvDayOfWeek.text = item.dayOfWeek
        holder.tvCount.text = "${item.count} treino${if (item.count > 1) "s" else ""} completo${if (item.count > 1) "s" else ""}"
    }

    class DiffCallback : DiffUtil.ItemCallback<WorkoutHistoryItem>() {
        override fun areItemsTheSame(oldItem: WorkoutHistoryItem, newItem: WorkoutHistoryItem): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: WorkoutHistoryItem, newItem: WorkoutHistoryItem): Boolean {
            return oldItem == newItem
        }
    }
}
