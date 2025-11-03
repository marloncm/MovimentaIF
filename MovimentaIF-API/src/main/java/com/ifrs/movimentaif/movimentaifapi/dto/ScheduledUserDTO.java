package com.ifrs.movimentaif.movimentaifapi.dto;

import java.util.List;

public class ScheduledUserDTO {
    private String userId;
    private String userName;
    private List<String> workoutIds; // Lista dos IDs de treino do dia
    private String workoutChartId;

    public ScheduledUserDTO() {}

    public ScheduledUserDTO(String userId, String userName, List<String> workoutIds, String workoutChartId) {
        this.userId = userId;
        this.userName = userName;
        this.workoutIds = workoutIds;
        this.workoutChartId = workoutChartId;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public List<String> getWorkoutIds() { return workoutIds; }
    public void setWorkoutIds(List<String> workoutIds) { this.workoutIds = workoutIds; }
    public String getWorkoutChartId() { return workoutChartId; }
    public void setWorkoutChartId(String workoutChartId) { this.workoutChartId = workoutChartId; }
}
