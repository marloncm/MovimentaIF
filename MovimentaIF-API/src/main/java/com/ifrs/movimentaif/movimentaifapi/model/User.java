package com.ifrs.movimentaif.movimentaifapi.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private String userId;
    private String userName;
    private Date age;
    private String phoneNumber;
    private String email;
    private String role; //USER, ADMIN
    private Date createdAt; // Alterado para java.util.Date
    private boolean isActive; //active or inactive
    private String affiliationType;
    private boolean interviewed;
    private boolean didFirstWorkout;
    private boolean scheduledFirstWorkout;
    private boolean isAppUser;
    private String firstWorkoutDate;
    private String interviewDate;
    private boolean signedTermOfCommitment;
    private String workoutChartId;
    private Boolean isAdmin;
    private String parqId;
    private String anamneseId;
    private String userObs;

    public User(){
        this.userId = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.isActive = false;
        this.interviewed = false;
        this.didFirstWorkout = false;
        this.scheduledFirstWorkout = false;
    }

    public User(String userId, String email){
        this.userId = userId;
        this.email = email;
        this.createdAt = new Date();

    }

    public User(String name, String email, Boolean isAdmin){
        this.userId = UUID.randomUUID().toString();
        this.userName = name;
        this.email = email;
        this.createdAt = new Date();
        this.isActive = false;
        this.interviewed = false;
        this.didFirstWorkout = false;
        this.scheduledFirstWorkout = false;
        this.isAdmin = isAdmin;
    }

    public User(String userId, String userName, String email, String role) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.createdAt = new Date();
    }

    public User(String userName, String email, String role) {
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.createdAt = new Date();
    }

    public User(String uid, String userName, String email, String role, Date createdAt){
        this.userId = uid;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getAge() {
        return age;
    }

    public void setAge(Date age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public String getAffiliationType() {
        return affiliationType;
    }

    public void setAffiliationType(String affiliationType) {
        this.affiliationType = affiliationType;
    }

    public boolean isInterviewed() {
        return interviewed;
    }

    public void setInterviewed(boolean interviewed) {
        this.interviewed = interviewed;
    }

    public boolean isDidFirstWorkout() {
        return didFirstWorkout;
    }

    public void setDidFirstWorkout(boolean didFirstWorkout) {
        this.didFirstWorkout = didFirstWorkout;
    }

    public boolean isScheduledFirstWorkout() {
        return scheduledFirstWorkout;
    }

    public void setScheduledFirstWorkout(boolean scheduledFirstWorkout) {
        this.scheduledFirstWorkout = scheduledFirstWorkout;
    }

    public boolean isAppUser() {
        return isAppUser;
    }

    public void setAppUser(boolean appUser) {
        isAppUser = appUser;
    }

    public String getFirstWorkoutDate() {
        return firstWorkoutDate;
    }

    public void setFirstWorkoutDate(String firstWorkoutDate) {
        this.firstWorkoutDate = firstWorkoutDate;
    }

    public boolean isSignedTermOfCommitment() {
        return signedTermOfCommitment;
    }

    public void setSignedTermOfCommitment(boolean signedTermOfCommitment) {
        this.signedTermOfCommitment = signedTermOfCommitment;
    }

    public String getWorkoutChartId() {
        return workoutChartId;
    }

    public void setWorkoutChartId(String workoutChartId) {
        this.workoutChartId = workoutChartId;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getParqId() {
        return parqId;
    }

    public void setParqId(String parqId) {
        this.parqId = parqId;
    }

    public String getAnamneseId() {
        return anamneseId;
    }

    public void setAnamneseId(String anamneseId) {
        this.anamneseId = anamneseId;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getUserObs() {
        return userObs;
    }

    public void setUserObs(String userObs) {
        this.userObs = userObs;
    }
}
