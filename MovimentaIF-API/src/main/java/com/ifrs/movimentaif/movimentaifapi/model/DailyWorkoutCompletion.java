package com.ifrs.movimentaif.movimentaifapi.model;

import java.util.Date;
import java.util.UUID;

public class DailyWorkoutCompletion {
    private String completionId;
    private String userId;
    private String dayOfWeek; // "monday", "tuesday", "wednesday", "thursday", "friday"
    private Date completedDate;
    private String workoutChartId; // Referência para qual ficha de treino foi completada

    public DailyWorkoutCompletion() {
        this.completionId = UUID.randomUUID().toString();
        this.completedDate = new Date();
    }

    public DailyWorkoutCompletion(String userId, String dayOfWeek, String workoutChartId) {
        this.completionId = UUID.randomUUID().toString();
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.workoutChartId = workoutChartId;
        this.completedDate = new Date();
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

    public String getWorkoutChartId() {
        return workoutChartId;
    }

    public void setWorkoutChartId(String workoutChartId) {
        this.workoutChartId = workoutChartId;
    }
}
