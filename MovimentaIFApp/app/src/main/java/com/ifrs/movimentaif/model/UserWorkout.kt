package com.ifrs.movimentaif.model

data class UserWorkout(
    val userWorkoutId: String = "",
    val userId: String = "",
    val workoutId: String = "",
    val repetitions: Int = 0,
    val weight: Int = 0,
    val series: Int = 0,
    val obs: String = ""
)
