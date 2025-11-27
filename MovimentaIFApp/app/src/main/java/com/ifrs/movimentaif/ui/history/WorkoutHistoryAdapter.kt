package com.ifrs.movimentaif.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ifrs.movimentaif.R

class WorkoutHistoryAdapter : ListAdapter<WorkoutHistoryItem, WorkoutHistoryAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDayOfWeek: TextView = view.findViewById(R.id.tvDayOfWeek)
        val exercisesContainer: LinearLayout = view.findViewById(R.id.exercisesContainer)
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
        
        // Limpar container antes de adicionar novos exercícios
        holder.exercisesContainer.removeAllViews()
        
        // Adicionar cada exercício
        item.exercises.forEach { exercise ->
            val exerciseView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_exercise_detail, holder.exercisesContainer, false)
            
            val tvExerciseName = exerciseView.findViewById<TextView>(R.id.tvExerciseName)
            val tvExerciseDetails = exerciseView.findViewById<TextView>(R.id.tvExerciseDetails)
            
            tvExerciseName.text = exercise.name
            tvExerciseDetails.text = "${exercise.series} séries × ${exercise.repetitions} reps"
            
            if (exercise.weight > 0) {
                tvExerciseDetails.text = "${tvExerciseDetails.text} - ${exercise.weight}kg"
            }
            
            holder.exercisesContainer.addView(exerciseView)
        }
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
