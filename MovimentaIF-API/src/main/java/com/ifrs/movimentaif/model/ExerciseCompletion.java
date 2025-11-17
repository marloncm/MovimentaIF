package com.ifrs.movimentaif.model;

import java.util.Date;
import java.util.UUID;

public class ExerciseCompletion {
    
    private String completionId;
    private String userId;
    private String userWorkoutId; // ID do UserWorkout (exercício específico)
    private String dayOfWeek; // monday, tuesday, etc.
    private Date completedDate;

    public ExerciseCompletion() {
        this.completionId = UUID.randomUUID().toString();
        this.completedDate = new Date();
    }

    public ExerciseCompletion(String userId, String userWorkoutId, String dayOfWeek) {
        this();
        this.userId = userId;
        this.userWorkoutId = userWorkoutId;
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and Setters
    public String getCompletionId() {
        return completionId;
    }

    public void setCompletionId(String completionId) {
        this.completionId = completionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserWorkoutId() {
        return userWorkoutId;
    }

    public void setUserWorkoutId(String userWorkoutId) {
        this.userWorkoutId = userWorkoutId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
