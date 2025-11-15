package com.ifrs.movimentaif.ui.workoutlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifrs.movimentaif.api.RetrofitInstance
import com.ifrs.movimentaif.databinding.FragmentWorkoutListBinding
import com.ifrs.movimentaif.model.Workout
import kotlinx.coroutines.launch

class WorkoutListFragment : Fragment() {

    private var _binding: FragmentWorkoutListBinding? = null
    private val binding get() = _binding!!
    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadWorkouts()
    }

    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter()
        binding.recyclerViewWorkouts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = workoutAdapter
        }
    }

    private fun loadWorkouts() {
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyStateLayout.visibility = View.GONE
        binding.recyclerViewWorkouts.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getAllWorkouts()
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val workouts = response.body() ?: emptyList()
                    if (workouts.isEmpty()) {
                        showEmptyState()
                    } else {
                        showWorkouts(workouts)
                    }
                } else {
                    showError("Erro ao carregar exercícios: ${response.code()}")
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                showError("Erro de conexão: ${e.message}")
            }
        }
    }

    private fun showWorkouts(workouts: List<Workout>) {
        binding.recyclerViewWorkouts.visibility = View.VISIBLE
        binding.emptyStateLayout.visibility = View.GONE
        workoutAdapter.submitList(workouts)
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.recyclerViewWorkouts.visibility = View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        showEmptyState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
