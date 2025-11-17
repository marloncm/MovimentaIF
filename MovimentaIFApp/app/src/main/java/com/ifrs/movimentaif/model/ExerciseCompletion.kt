package com.ifrs.movimentaif.model

import java.util.Date

data class ExerciseCompletion(
    var completionId: String = "",
    var userId: String = "",
    var userWorkoutId: String = "", // ID do UserWorkout (exercício específico)
    var dayOfWeek: String = "", // monday, tuesday, etc.
    var completedDate: Date = Date()
)
