package com.ifrs.movimentaif.movimentaifapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkoutHistory {
    private String historyId;
    private String userId;
    private List<WorkoutChart> workoutCharts;

    public WorkoutHistory(){
        this.historyId = UUID.randomUUID().toString();
        this.workoutCharts = new ArrayList<>();
    }

    public WorkoutHistory(String userId) {
        this.historyId = UUID.randomUUID().toString();
        this.userId = userId;
        this.workoutCharts = new ArrayList<>();
    }

    public WorkoutHistory(String userId, List<WorkoutChart> workoutCharts) {
        this.historyId = UUID.randomUUID().toString();
        this.userId = userId;
        this.workoutCharts = workoutCharts;
    }
    public WorkoutHistory(int historyId, String userId, List<WorkoutChart> workoutCharts) {
        this.historyId = String.valueOf(historyId);
        this.userId = userId;
        this.workoutCharts = workoutCharts;
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

    public List<WorkoutChart> getWorkoutCharts() {
        return workoutCharts;
    }

    public void setWorkoutCharts(List<WorkoutChart> workoutCharts) {
        this.workoutCharts = workoutCharts;
    }

    public void addWorkoutChartId(WorkoutChart workoutChartId) {
        this.workoutCharts.add(workoutChartId);
    }
}
