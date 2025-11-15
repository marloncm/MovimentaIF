package com.ifrs.movimentaif.model

import java.util.Date

data class WorkoutChart(
    val chartId: String = "",
    val userId: String = "",
    val mondayWorkouts: List<String> = emptyList(),
    val tuesdayWorkouts: List<String> = emptyList(),
    val wednesdayWorkouts: List<String> = emptyList(),
    val thursdayWorkouts: List<String> = emptyList(),
    val fridayWorkouts: List<String> = emptyList(),
    val startDate: Date? = null,
    val endDate: Date? = null
)
