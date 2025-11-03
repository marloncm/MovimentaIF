package com.ifrs.movimentaif.movimentaifapi.model;

import java.util.UUID;

public class WorkoutHistory {
    private String historyId;
    private String userId;
    private String[] workoutChartIds;

    public WorkoutHistory(){}
    public WorkoutHistory(String userId, String[] workoutChartIds) {
        this.historyId = UUID.randomUUID().toString();
        this.userId = userId;
        this.workoutChartIds = workoutChartIds;
    }
    public WorkoutHistory(int historyId, String userId, String[] workoutChartIds) {
        this.historyId = String.valueOf(historyId);
        this.userId = userId;
        this.workoutChartIds = workoutChartIds;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getWorkoutChartIds() {
        return workoutChartIds;
    }

    public void setWorkoutChartIds(String[] workoutChartIds) {
        this.workoutChartIds = workoutChartIds;
    }
}
