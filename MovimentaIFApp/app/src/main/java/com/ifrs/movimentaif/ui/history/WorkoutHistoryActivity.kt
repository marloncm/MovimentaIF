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
                // Primeiro tenta buscar WorkoutHistory
                val workoutHistoryResponse = RetrofitInstance.api.getWorkoutHistoryByUserId(currentUser.uid)
                
                if (workoutHistoryResponse.isSuccessful && workoutHistoryResponse.body() != null) {
                    // Se tem WorkoutHistory, processa ele
                    processWorkoutHistory(workoutHistoryResponse.body()!!, currentUser.uid)
                } else if (workoutHistoryResponse.code() == 404) {
                    // Se não encontrar (404), usa fallback com ficha atual + completions
                    Toast.makeText(this@WorkoutHistoryActivity, 
                        "Carregando histórico da ficha atual...", 
                        Toast.LENGTH_SHORT).show()
                    loadHistoryFromCurrentChart(currentUser.uid)
                } else {
                    // Outro erro
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@WorkoutHistoryActivity, 
                        "Erro ao buscar histórico: ${workoutHistoryResponse.code()}", 
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Log.e("WorkoutHistory", "Erro ao buscar histórico", e)
                Toast.makeText(this@WorkoutHistoryActivity, 
                    "Erro: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun processWorkoutHistory(
        workoutHistory: com.ifrs.movimentaif.model.WorkoutHistory,
        userId: String
    ) {
        val historyItems = mutableListOf<WorkoutHistoryItem>()
        
        // Itera sobre cada ficha arquivada
        for (chart in workoutHistory.workoutCharts) {
            // Processa cada dia da semana na ficha
            val days = mapOf(
                "monday" to chart.mondayWorkouts,
                "tuesday" to chart.tuesdayWorkouts,
                "wednesday" to chart.wednesdayWorkouts,
                "thursday" to chart.thursdayWorkouts,
                "friday" to chart.fridayWorkouts
            )
            
            for ((dayOfWeek, workoutIds) in days) {
                if (workoutIds.isEmpty()) continue
                
                val exercises = mutableListOf<ExerciseDetail>()
                
                for (workoutId in workoutIds) {
                    try {
                        val userWorkoutResponse = RetrofitInstance.api.getUserWorkoutById(workoutId)
                        if (!userWorkoutResponse.isSuccessful) continue
                        
                        val userWorkout = userWorkoutResponse.body() ?: continue
                        
                        val workoutResponse = RetrofitInstance.api.getWorkoutById(userWorkout.workoutId)
                        if (!workoutResponse.isSuccessful) continue
                        
                        val workout = workoutResponse.body() ?: continue
                        
                        exercises.add(
                            ExerciseDetail(
                                name = workout.workoutName,
                                series = userWorkout.series,
                                repetitions = userWorkout.repetitions,
                                weight = userWorkout.weight
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("WorkoutHistory", "Erro ao buscar exercício", e)
                        continue
                    }
                }
                
                if (exercises.isNotEmpty()) {
                    historyItems.add(
                        WorkoutHistoryItem(
                            date = chart.chartId, // Usando chartId como identificador
                            dayOfWeek = translateDayOfWeek(dayOfWeek),
                            exercises = exercises
                        )
                    )
                }
            }
        }
        
        progressBar.visibility = View.GONE
        
        if (historyItems.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter.submitList(historyItems)
        }
        
        // Buscar estatísticas
        loadStatistics(userId)
    }

    private suspend fun loadHistoryFromCurrentChart(userId: String) {
        // Buscar a ficha atual do usuário
        val chartResponse = RetrofitInstance.api.getWorkoutChartByUserId(userId)
        
        // Buscar completions para saber os dias realizados
        val historyResponse = RetrofitInstance.api.getWorkoutCompletionsByUserId(userId)

                progressBar.visibility = View.GONE

                if (chartResponse.isSuccessful && historyResponse.isSuccessful) {
                    val chart = chartResponse.body()
                    val completions = historyResponse.body() ?: emptyList()
                    
                    if (completions.isEmpty() || chart == null) {
                        tvEmptyState.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        tvEmptyState.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        
                        // Processar completions com exercícios da ficha
                        val historyItems = processCompletionsWithChart(completions, chart)
                        adapter.submitList(historyItems)
                    }
                    
                    // Atualizar estatísticas
                    loadStatistics(userId)
                } else {
                    if (!chartResponse.isSuccessful) {
                        Log.e("WorkoutHistory", "Erro ao buscar ficha: ${chartResponse.code()}")
                    }
                    if (!historyResponse.isSuccessful) {
                        Log.e("WorkoutHistory", "Erro ao buscar completions: ${historyResponse.code()}")
                    }
                    
                    // Se não tem ficha mas tem completions, mostrar apenas as datas
                    if (historyResponse.isSuccessful) {
                        val completions = historyResponse.body() ?: emptyList()
                        if (completions.isNotEmpty()) {
                            val simpleItems = completions.groupBy { 
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.completedDate)
                            }.map { (date, items) ->
                                com.ifrs.movimentaif.ui.history.WorkoutHistoryItem(
                                    date = date,
                                    dayOfWeek = translateDayOfWeek(items.first().dayOfWeek),
                                    exercises = emptyList()
                                )
                            }
                            adapter.submitList(simpleItems)
                            recyclerView.visibility = View.VISIBLE
                        } else {
                            tvEmptyState.visibility = View.VISIBLE
                        }
                    } else {
                        tvEmptyState.visibility = View.VISIBLE
                    }
                }
    }

    private suspend fun loadStatistics(userId: String) {
        try {
            val totalWorkoutsResponse = RetrofitInstance.api.getTotalWorkoutsCompleted(userId)
            val activeDaysResponse = RetrofitInstance.api.getActiveDaysCount(userId)
            
            if (totalWorkoutsResponse.isSuccessful) {
                val total = totalWorkoutsResponse.body() ?: 0
                tvTotalWorkouts.text = "$total treinos concluídos"
            }
            
            if (activeDaysResponse.isSuccessful) {
                val activeDays = activeDaysResponse.body() ?: 0
                tvActiveDays.text = "$activeDays dias ativos"
            }
        } catch (e: Exception) {
            Log.e("WorkoutHistory", "Erro ao buscar estatísticas", e)
        }
    }

    private suspend fun processCompletionsWithChart(
        completions: List<DailyWorkoutCompletion>,
        chart: com.ifrs.movimentaif.model.WorkoutChart
    ): List<com.ifrs.movimentaif.ui.history.WorkoutHistoryItem> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val grouped = completions
            .sortedByDescending { it.completedDate }
            .groupBy { 
                Pair(dateFormat.format(it.completedDate), it.dayOfWeek)
            }
        
        return grouped.map { (dateDay, items) ->
            val exercises = mutableListOf<ExerciseDetail>()
            val dayOfWeek = dateDay.second
            
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
                    Log.e("WorkoutHistory", "Erro ao buscar exercício", e)
                }
            }
            
            com.ifrs.movimentaif.ui.history.WorkoutHistoryItem(
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
