package com.ifrs.movimentaif.ui.userworkouts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.ifrs.movimentaif.api.RetrofitInstance
import com.ifrs.movimentaif.databinding.FragmentUserWorkoutsBinding
import com.ifrs.movimentaif.model.DailyWorkoutCompletion
import com.ifrs.movimentaif.model.UserWorkout
import com.ifrs.movimentaif.model.Workout
import com.ifrs.movimentaif.model.WorkoutChart
import kotlinx.coroutines.launch

class UserWorkoutsFragment : Fragment() {

    private var _binding: FragmentUserWorkoutsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var workoutChart: WorkoutChart? = null
    private val workoutCache = mutableMapOf<String, Workout>()
    private val userWorkoutCache = mutableMapOf<String, UserWorkout>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserWorkoutsBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFinishButtons()
        loadWorkoutChart()
    }

    private fun setupFinishButtons() {
        binding.btnFinishMonday.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            finishWorkoutDay("monday", "Segunda-feira")
        }
        
        binding.btnFinishTuesday.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            finishWorkoutDay("tuesday", "Terça-feira")
        }
        
        binding.btnFinishWednesday.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            finishWorkoutDay("wednesday", "Quarta-feira")
        }
        
        binding.btnFinishThursday.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            finishWorkoutDay("thursday", "Quinta-feira")
        }
        
        binding.btnFinishFriday.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            finishWorkoutDay("friday", "Sexta-feira")
        }
    }

    private fun finishWorkoutDay(dayOfWeek: String, dayName: String) {
        val userId = auth.currentUser?.uid
        val chartId = workoutChart?.chartId
        
        if (userId == null || chartId == null) {
            Toast.makeText(context, "Erro: usuário ou ficha não encontrada", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val completion = DailyWorkoutCompletion(
                    userId = userId,
                    dayOfWeek = dayOfWeek,
                    workoutChartId = chartId
                )
                
                val response = RetrofitInstance.api.createWorkoutCompletion(completion)
                
                if (response.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Parabéns! Treino de $dayName finalizado! 💪",
                        Toast.LENGTH_LONG
                    ).show()
                    updateButtonState(dayOfWeek, true)
                } else {
                    Toast.makeText(
                        context,
                        "Erro ao finalizar treino",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Erro de conexão: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateButtonState(dayOfWeek: String, completed: Boolean) {
        val button = when(dayOfWeek) {
            "monday" -> binding.btnFinishMonday
            "tuesday" -> binding.btnFinishTuesday
            "wednesday" -> binding.btnFinishWednesday
            "thursday" -> binding.btnFinishThursday
            "friday" -> binding.btnFinishFriday
            else -> null
        }
        
        button?.let {
            if (completed) {
                it.text = "✓ Treino Finalizado Hoje"
                it.isEnabled = false
                it.alpha = 0.6f
            } else {
                it.text = "✓ Finalizar Treino do Dia"
                it.isEnabled = true
                it.alpha = 1.0f
            }
        }
    }

    private fun checkCompletionStatus() {
        val userId = auth.currentUser?.uid ?: return
        
        lifecycleScope.launch {
            try {
                val days = listOf("monday", "tuesday", "wednesday", "thursday", "friday")
                
                for (day in days) {
                    val response = RetrofitInstance.api.isCompletedToday(userId, day)
                    if (response.isSuccessful) {
                        val isCompleted = response.body() ?: false
                        updateButtonState(day, isCompleted)
                    }
                }
            } catch (e: Exception) {
                // Silenciosamente ignora erros ao verificar status
            }
        }
    }

    private fun loadWorkoutChart() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            showError("Usuário não autenticado")
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.scrollViewContent.visibility = View.GONE
        binding.emptyStateLayout.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getWorkoutChartByUserId(userId)
                
                if (response.isSuccessful) {
                    workoutChart = response.body()
                    if (workoutChart != null && hasWorkouts(workoutChart!!)) {
                        loadUserWorkouts(userId)
                    } else {
                        binding.progressBar.visibility = View.GONE
                        showEmptyState()
                    }
                } else if (response.code() == 404) {
                    binding.progressBar.visibility = View.GONE
                    showEmptyState()
                } else {
                    binding.progressBar.visibility = View.GONE
                    showError("Erro ao carregar ficha: ${response.code()}")
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                showError("Erro de conexão: ${e.message}")
            }
        }
    }

    private fun hasWorkouts(chart: WorkoutChart): Boolean {
        return chart.mondayWorkouts.isNotEmpty() ||
                chart.tuesdayWorkouts.isNotEmpty() ||
                chart.wednesdayWorkouts.isNotEmpty() ||
                chart.thursdayWorkouts.isNotEmpty() ||
                chart.fridayWorkouts.isNotEmpty()
    }

    private suspend fun loadUserWorkouts(userId: String) {
        try {
            val response = RetrofitInstance.api.getUserWorkoutsByUserId(userId)
            if (response.isSuccessful) {
                val userWorkouts = response.body() ?: emptyList()
                if (userWorkouts.isEmpty()) {
                    binding.progressBar.visibility = View.GONE
                    showEmptyState()
                    return
                }
                userWorkouts.forEach { userWorkout ->
                    userWorkoutCache[userWorkout.userWorkoutId] = userWorkout
                }
                loadWorkoutDetails()
            } else {
                binding.progressBar.visibility = View.GONE
                showError("Erro ao carregar detalhes dos treinos: ${response.code()}")
            }
        } catch (e: Exception) {
            binding.progressBar.visibility = View.GONE
            showError("Erro ao carregar UserWorkouts: ${e.message}")
            e.printStackTrace()
        }
    }

    private suspend fun loadWorkoutDetails() {
        val chart = workoutChart ?: return

        // Carregar todos os workouts únicos dos UserWorkouts
        val allWorkoutIds = userWorkoutCache.values.map { it.workoutId }.distinct()

        for (workoutId in allWorkoutIds) {
            try {
                val response = RetrofitInstance.api.getWorkoutById(workoutId)
                if (response.isSuccessful) {
                    response.body()?.let { workout ->
                        workoutCache[workoutId] = workout
                    }
                }
            } catch (e: Exception) {
                // Continuar mesmo se falhar
            }
        }

        binding.progressBar.visibility = View.GONE
        displayWorkoutChart()
    }

    private fun displayWorkoutChart() {
        val chart = workoutChart ?: return
        
        binding.scrollViewContent.visibility = View.VISIBLE
        binding.emptyStateLayout.visibility = View.GONE

        // Segunda-feira
        displayDayWorkouts(
            chart.mondayWorkouts,
            binding.mondayCard,
            binding.mondayWorkoutsContainer
        )

        // Terça-feira
        displayDayWorkouts(
            chart.tuesdayWorkouts,
            binding.tuesdayCard,
            binding.tuesdayWorkoutsContainer
        )

        // Quarta-feira
        displayDayWorkouts(
            chart.wednesdayWorkouts,
            binding.wednesdayCard,
            binding.wednesdayWorkoutsContainer
        )

        // Quinta-feira
        displayDayWorkouts(
            chart.thursdayWorkouts,
            binding.thursdayCard,
            binding.thursdayWorkoutsContainer
        )

        // Sexta-feira
        displayDayWorkouts(
            chart.fridayWorkouts,
            binding.fridayCard,
            binding.fridayWorkoutsContainer
        )
        
        // Verificar status de conclusão dos treinos
        checkCompletionStatus()
    }

    private fun displayDayWorkouts(
        userWorkoutIds: List<String>,
        cardView: View,
        container: android.widget.LinearLayout
    ) {
        if (userWorkoutIds.isEmpty()) {
            cardView.visibility = View.GONE
            return
        }

        cardView.visibility = View.VISIBLE
        container.removeAllViews()

        userWorkoutIds.forEach { userWorkoutId ->
            val userWorkout = userWorkoutCache[userWorkoutId]
            val workout = userWorkout?.let { workoutCache[it.workoutId] }

            if (userWorkout != null && workout != null) {
                val workoutView = createWorkoutItemView(userWorkout, workout)
                container.addView(workoutView)
            }
        }
    }

    private fun createWorkoutItemView(userWorkout: UserWorkout, workout: Workout): View {
        val itemView = LayoutInflater.from(requireContext()).inflate(
            com.ifrs.movimentaif.R.layout.item_user_workout,
            null,
            false
        )

        val workoutName = itemView.findViewById<android.widget.TextView>(com.ifrs.movimentaif.R.id.textWorkoutName)
        val workoutDetails = itemView.findViewById<android.widget.TextView>(com.ifrs.movimentaif.R.id.textWorkoutDetails)
        val btnVideo = itemView.findViewById<com.google.android.material.button.MaterialButton>(com.ifrs.movimentaif.R.id.btnWatchVideo)

        workoutName?.text = workout.workoutName ?: "Exercício"
        
        val detailsText = buildString {
            append("${userWorkout.series} séries")
            append(" • ${userWorkout.repetitions} repetições")
            if (userWorkout.weight > 0) {
                append(" • ${userWorkout.weight}kg")
            }
            if (userWorkout.obs.isNotEmpty()) {
                append("\n${userWorkout.obs}")
            }
        }
        workoutDetails?.text = detailsText

        if (!workout.workoutVideoLink.isNullOrEmpty()) {
            btnVideo?.visibility = View.VISIBLE
            btnVideo?.setOnClickListener {
                com.ifrs.movimentaif.utils.SoundManager.playClickSound()
                openVideo(workout.workoutVideoLink)
            }
            
            // Adicionar botão de compartilhar
            val btnShare = itemView.findViewById<com.google.android.material.button.MaterialButton>(com.ifrs.movimentaif.R.id.btnShareVideo)
            btnShare?.visibility = View.VISIBLE
            btnShare?.setOnClickListener {
                com.ifrs.movimentaif.utils.SoundManager.playClickSound()
                shareVideo(workout.workoutVideoLink, workout.workoutName ?: "Exercício")
            }
        } else {
            btnVideo?.visibility = View.GONE
            val btnShare = itemView.findViewById<com.google.android.material.button.MaterialButton>(com.ifrs.movimentaif.R.id.btnShareVideo)
            btnShare?.visibility = View.GONE
        }

        return itemView
    }

    private fun openVideo(videoUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao abrir vídeo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareVideo(videoUrl: String, workoutName: String) {
        try {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Confira este treino: $workoutName")
                putExtra(Intent.EXTRA_TEXT, "Olha que treino legal do MovimentaIF!\n\n$workoutName\n\n$videoUrl\n\n#MaisMovimento #IFRS")
            }
            startActivity(Intent.createChooser(shareIntent, "Compartilhar treino via"))
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao compartilhar vídeo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.scrollViewContent.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
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
