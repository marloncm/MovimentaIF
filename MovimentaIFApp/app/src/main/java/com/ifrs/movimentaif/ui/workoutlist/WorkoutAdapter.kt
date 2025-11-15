package com.ifrs.movimentaif.ui.workoutlist

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ifrs.movimentaif.databinding.ItemWorkoutBinding
import com.ifrs.movimentaif.model.Workout

class WorkoutAdapter : ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WorkoutViewHolder(private val binding: ItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workout: Workout) {
            binding.textWorkoutName.text = workout.workoutName
            binding.textWorkoutDescription.text = workout.workoutDescription
            
            if (workout.workoutVideoLink.isNotEmpty()) {
                binding.btnWatchVideo.visibility = android.view.View.VISIBLE
                binding.btnWatchVideo.setOnClickListener {
                    com.ifrs.movimentaif.utils.SoundManager.playClickSound()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(workout.workoutVideoLink))
                    binding.root.context.startActivity(intent)
                }
            } else {
                binding.btnWatchVideo.visibility = android.view.View.GONE
            }
        }
    }

    private class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.workoutId == newItem.workoutId
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }
    }
}
