package com.ifrs.movimentaif.movimentaifapi.model;

import java.util.Date;
import java.util.UUID;

public class WorkoutCompletion {
    private String completionId;
    private String userId;
    private Date completedDate;
    private String dayOfWeek; // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
    private int totalExercises;
    private String notes;

    public WorkoutCompletion() {
        this.completionId = UUID.randomUUID().toString();
        this.completedDate = new Date();
    }

    public WorkoutCompletion(String userId, String dayOfWeek, int totalExercises) {
        this.completionId = UUID.randomUUID().toString();
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.totalExercises = totalExercises;
        this.completedDate = new Date();
    }

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

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getTotalExercises() {
        return totalExercises;
    }

    public void setTotalExercises(int totalExercises) {
        this.totalExercises = totalExercises;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
