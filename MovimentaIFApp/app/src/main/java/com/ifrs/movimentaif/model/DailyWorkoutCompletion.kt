package com.ifrs.movimentaif.model

import java.util.Date

data class DailyWorkoutCompletion(
    var completionId: String = "",
    var userId: String = "",
    var dayOfWeek: String = "", // "monday", "tuesday", "wednesday", "thursday", "friday"
    var completedDate: Date = Date(),
    var workoutChartId: String = ""
)
