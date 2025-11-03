package com.ifrs.movimentaif.movimentaifapi.model;

public class UserWorkout {
    private String userWorkoutId;
    private String userId;
    private String workoutId;

    private int repetitions;
    private int weight;
    private int series;
    private String obs;

    public UserWorkout(){}
    public UserWorkout(String userId, String workoutId, int repetitions, int wieght, int series, String obs) {
        this.userWorkoutId = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.workoutId = workoutId;
        this.repetitions = repetitions;
        this.series = series;
        this.weight = wieght;
        this.obs = obs;
    }

    public String getUserWorkoutId() {
        return userWorkoutId;
    }

    public void setUserWorkoutId(String userWorkoutId) {
        this.userWorkoutId = userWorkoutId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getWieght() {
        return weight;
    }

    public void setWieght(int wieght) {
        this.weight = wieght;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
