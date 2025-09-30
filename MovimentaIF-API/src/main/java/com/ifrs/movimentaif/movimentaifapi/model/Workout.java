package com.ifrs.movimentaif.movimentaifapi.model;


import java.util.Objects;
import java.util.UUID;

public class Workout {

    private String workoutId;
    private String workoutName;
    private String workoutDescription;
    private String workoutVideoLink;

    public Workout() {}

    public Workout(String workoutName, String workoutDescription, String workoutVideoLink){
        this.workoutId = UUID.randomUUID().toString();
        this.workoutName = workoutName;
        this.workoutDescription = workoutDescription;
        this.workoutVideoLink = workoutVideoLink;
    }

    public Workout(String workoutId, String workoutName, String workoutDescription, String workoutVideoLink) {
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.workoutDescription = workoutDescription;
        this.workoutVideoLink = workoutVideoLink;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getWorkoutDescription() {
        return workoutDescription;
    }

    public void setWorkoutDescription(String workoutDescription) {
        this.workoutDescription = workoutDescription;
    }

    public String getWorkoutVideoLink() {
        return workoutVideoLink;
    }

    public void setWorkoutVideoLink(String workoutVideoLink) {
        this.workoutVideoLink = workoutVideoLink;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Workout workout = (Workout) o;
        return Objects.equals(workoutId, workout.workoutId) && Objects.equals(workoutName, workout.workoutName) && Objects.equals(workoutDescription, workout.workoutDescription) && Objects.equals(workoutVideoLink, workout.workoutVideoLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workoutId, workoutName, workoutDescription, workoutVideoLink);
    }

    @Override
    public String toString() {
        return "{" +
                " workoutId='" + getWorkoutId() + "'" +
                ", workoutName='" + getWorkoutName() + "'" +
                ", workoutDescription='" + getWorkoutDescription() + "'" +
                ", workoutVideoLink='" + getWorkoutVideoLink() + "'" +
                "}";
    }
}
