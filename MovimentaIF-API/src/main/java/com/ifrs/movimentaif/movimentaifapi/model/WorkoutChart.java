package com.ifrs.movimentaif.movimentaifapi.model;

import java.util.ArrayList;
import java.util.Date;
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
    private Date startDate;
    private Date endDate;

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

    public WorkoutChart(String chartId, String userId, List<String> mondayWorkouts, List<String> tuesdayWorkouts, List<String> wednesdayWorkouts, List<String> thursdayWorkouts, List<String> fridayWorkouts, Date startDate, Date endDate) {
        this.chartId = chartId;
        this.userId = userId;
        this.mondayWorkouts = mondayWorkouts;
        this.tuesdayWorkouts = tuesdayWorkouts;
        this.wednesdayWorkouts = wednesdayWorkouts;
        this.thursdayWorkouts = thursdayWorkouts;
        this.fridayWorkouts = fridayWorkouts;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public List<String> getTuesdayWorkouts() {
        return tuesdayWorkouts;
    }

    public void setTuesdayWorkouts(List<String> tuesdayWorkouts) {
        this.tuesdayWorkouts = tuesdayWorkouts;
    }

    public List<String> getWednesdayWorkouts() {
        return wednesdayWorkouts;
    }

    public void setWednesdayWorkouts(List<String> wednesdayWorkouts) {
        this.wednesdayWorkouts = wednesdayWorkouts;
    }

    public List<String> getThursdayWorkouts() {
        return thursdayWorkouts;
    }

    public void setThursdayWorkouts(List<String> thursdayWorkouts) {
        this.thursdayWorkouts = thursdayWorkouts;
    }

    public List<String> getFridayWorkouts() {
        return fridayWorkouts;
    }

    public void setFridayWorkouts(List<String> fridayWorkouts) {
        this.fridayWorkouts = fridayWorkouts;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
