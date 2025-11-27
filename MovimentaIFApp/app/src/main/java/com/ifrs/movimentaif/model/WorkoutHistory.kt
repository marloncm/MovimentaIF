package com.ifrs.movimentaif.model

data class WorkoutHistory(
    val historyId: String = "",
    val userId: String = "",
    val workoutCharts: List<WorkoutChart> = emptyList()
)
