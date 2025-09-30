package com.ifrs.movimentaif.movimentaifapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkoutChart {

    private String chartId;
    private String userId;
    private List<String> mondayWorkouts;
    private List<String> tuesdayWorkouts;
    private List<String> wednesdayWorkouts;
    private List<String> thursdayWorkouts;
    private List<String> fridayWorkouts;

    public WorkoutChart(){
        this.chartId = UUID.randomUUID().toString();
        this.mondayWorkouts = new ArrayList<>();
        this.tuesdayWorkouts = new ArrayList<>();
        this.wednesdayWorkouts = new ArrayList<>();
        this.thursdayWorkouts = new ArrayList<>();
        this.fridayWorkouts = new ArrayList<>();
    }

    public WorkoutChart(String userId){
        this.chartId = UUID.randomUUID().toString();
        this.userId = userId;
        this.mondayWorkouts = new ArrayList<>();
        this.tuesdayWorkouts = new ArrayList<>();
        this.wednesdayWorkouts = new ArrayList<>();
        this.thursdayWorkouts = new ArrayList<>();
        this.fridayWorkouts = new ArrayList<>();
    }

    public WorkoutChart(String chartId, String userId, List<String> mondayWorkouts, List<String> tuesdayWorkouts, List<String> wednesdayWorkouts, List<String> thursdayWorkouts, List<String> fridayWorkouts) {
        this.chartId = chartId;
        this.userId = userId;
        this.mondayWorkouts = mondayWorkouts;
        this.tuesdayWorkouts = tuesdayWorkouts;
        this.wednesdayWorkouts = wednesdayWorkouts;
        this.thursdayWorkouts = thursdayWorkouts;
        this.fridayWorkouts = fridayWorkouts;
    }

    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;

    }

    public List<String> getMondayWorkouts() {
        return mondayWorkouts;
    }

    public void setMondayWorkouts(List<String> mondayWorkouts) {
        this.mondayWorkouts = mondayWorkouts;
    }

    public void addWorkoutId(String workoutId){
        this.mondayWorkouts.add(workoutId);
    }
}
