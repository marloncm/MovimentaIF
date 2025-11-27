package com.ifrs.movimentaif.ui.history

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.ifrs.movimentaif.R
import com.ifrs.movimentaif.api.RetrofitInstance
import com.ifrs.movimentaif.model.DailyWorkoutCompletion
import com.ifrs.movimentaif.model.ExerciseCompletion
import com.ifrs.movimentaif.model.UserWorkout
import com.ifrs.movimentaif.model.Workout
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutHistoryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView
    private lateinit var tvTotalWorkouts: TextView
    private lateinit var tvActiveDays: TextView
    private lateinit var adapter: WorkoutHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_workout_history)

            auth = FirebaseAuth.getInstance()

            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Histórico de Treinos"

            recyclerView = findViewById(R.id.recyclerViewHistory)
            progressBar = findViewById(R.id.progressBar)
            tvEmptyState = findViewById(R.id.tvEmptyState)
            tvTotalWorkouts = findViewById(R.id.tvTotalWorkouts)
            tvActiveDays = findViewById(R.id.tvActiveDays)

            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = WorkoutHistoryAdapter()
            recyclerView.adapter = adapter

            loadWorkoutHistory()
        } catch (e: Exception) {
            Log.e("WorkoutHistory", "Erro no onCreate", e)
            Toast.makeText(this, "Erro ao inicializar: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun loadWorkoutHistory() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        tvEmptyState.visibility = View.GONE

        lifecycleScope.launch {
            try {
                // Buscar histórico de treinos
                val historyResponse = RetrofitInstance.api.getWorkoutCompletionsByUserId(currentUser.uid)
                Log.d("WorkoutHistory", "History response: ${historyResponse.code()}")
                
                // Buscar estatísticas
                val totalWorkoutsResponse = RetrofitInstance.api.getTotalWorkoutsCompleted(currentUser.uid)
                val activeDaysResponse = RetrofitInstance.api.getActiveDaysCount(currentUser.uid)

                progressBar.visibility = View.GONE

                if (historyResponse.isSuccessful) {
                    val completions = historyResponse.body() ?: emptyList()
                    
                    Log.d("WorkoutHistory", "Total completions: ${completions.size}")
                    
                    if (completions.isEmpty()) {
                        tvEmptyState.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        tvEmptyState.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        
                        // Agrupar por data e buscar detalhes dos exercícios do dia
                        val groupedCompletions = groupCompletionsByDateWithExercises(currentUser.uid, completions)
                        adapter.submitList(groupedCompletions)
                    }
                    
                    // Atualizar estatísticas
                    if (totalWorkoutsResponse.isSuccessful) {
                        val total = totalWorkoutsResponse.body() ?: 0
                        tvTotalWorkouts.text = "$total treinos concluídos"
                    }
                    
                    if (activeDaysResponse.isSuccessful) {
                        val activeDays = activeDaysResponse.body() ?: 0
                        tvActiveDays.text = "$activeDays dias ativos"
                    }
                } else {
                    val errorMsg = "Erro ao buscar histórico: ${historyResponse.code()}"
                    Log.e("WorkoutHistory", errorMsg)
                    Toast.makeText(this@WorkoutHistoryActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Log.e("WorkoutHistory", "Erro ao buscar histórico", e)
                Toast.makeText(this@WorkoutHistoryActivity, 
                    "Erro: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun groupCompletionsByDateWithExercises(
        userId: String,
        completions: List<DailyWorkoutCompletion>
    ): List<WorkoutHistoryItem> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val grouped = completions
            .sortedByDescending { it.completedDate }
            .groupBy { 
                Pair(dateFormat.format(it.completedDate), it.dayOfWeek)
            }
        
        return grouped.map { (dateDay, items) ->
            val exercises = mutableListOf<ExerciseDetail>()
            
            // Para cada dia, buscar os exercícios completados naquele dia
            val dayOfWeek = dateDay.second
            try {
                // Buscar ficha de treino do usuário
                val chartResponse = RetrofitInstance.api.getWorkoutChartByUserId(userId)
                if (chartResponse.isSuccessful) {
                    val chart = chartResponse.body()
                    if (chart != null) {
                        // Pegar os IDs dos exercícios do dia
                        val workoutIds = when (dayOfWeek.lowercase()) {
                            "monday" -> chart.mondayWorkouts
                            "tuesday" -> chart.tuesdayWorkouts
                            "wednesday" -> chart.wednesdayWorkouts
                            "thursday" -> chart.thursdayWorkouts
                            "friday" -> chart.fridayWorkouts
                            else -> emptyList()
                        }
                        
                        // Buscar detalhes de cada exercício
                        for (userWorkoutId in workoutIds) {
                            try {
                                val userWorkoutResponse = RetrofitInstance.api.getUserWorkoutById(userWorkoutId)
                                if (userWorkoutResponse.isSuccessful) {
                                    val userWorkout = userWorkoutResponse.body()
                                    if (userWorkout != null) {
                                        val workoutResponse = RetrofitInstance.api.getWorkoutById(userWorkout.workoutId)
                                        if (workoutResponse.isSuccessful) {
                                            val workout = workoutResponse.body()
                                            if (workout != null) {
                                                exercises.add(
                                                    ExerciseDetail(
                                                        name = workout.workoutName,
                                                        series = userWorkout.series,
                                                        repetitions = userWorkout.repetitions,
                                                        weight = userWorkout.weight
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("WorkoutHistory", "Erro ao buscar exercício $userWorkoutId", e)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("WorkoutHistory", "Erro ao buscar ficha de treino", e)
            }
            
            WorkoutHistoryItem(
                date = dateDay.first,
                dayOfWeek = translateDayOfWeek(dayOfWeek),
                exercises = exercises
            )
        }
    }

    private fun translateDayOfWeek(day: String): String {
        return when (day.lowercase()) {
            "monday" -> "Segunda-feira"
            "tuesday" -> "Terça-feira"
            "wednesday" -> "Quarta-feira"
            "thursday" -> "Quinta-feira"
            "friday" -> "Sexta-feira"
            else -> day
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

data class WorkoutHistoryItem(
    val date: String,
    val dayOfWeek: String,
    val exercises: List<ExerciseDetail>
)

data class ExerciseDetail(
    val name: String,
    val series: Int,
    val repetitions: Int,
    val weight: Int
)
