package com.ifrs.movimentaif.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public class User {
    private String userId;
    private String userName;
    private Date age;
    private String phoneNumber;
    private String email;
    private String role; //USER, ADMIN
    private Date createdAt;
    
    @SerializedName("active")
    private boolean active;
    
    private String affiliationType; //STUDENT, PROFESSOR, STAFF TODO transformar em enum
    
    @SerializedName("interviewed")
    private boolean interviewed;
    
    @SerializedName("didFirstWorkout")
    private boolean didFirstWorkout;
    
    @SerializedName("scheduledFirstWorkout")
    private boolean scheduledFirstWorkout;
    
    @SerializedName("appUser")
    private boolean appUser;
    
    private Date firstWorkoutDate;
    
    private Date interviewDate;
    
    @SerializedName("signedTermOfCommitment")
    private boolean signedTermOfCommitment;
    
    private String workoutChartId;
    
    @SerializedName("admin")
    private Boolean admin;
    
    private String parqId;
    private String anamneseId;

    public User(){
        this.userId = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.active = false;
        this.interviewed = false;
        this.didFirstWorkout = false;
        this.scheduledFirstWorkout = false;
    }

    public User(String name, String email, Boolean admin){
        this.userId = UUID.randomUUID().toString();
        this.userName = name;
        this.email = email;
        this.createdAt = new Date();
        this.active = false;
        this.interviewed = false;
        this.didFirstWorkout = false;
        this.scheduledFirstWorkout = false;
        this.admin = admin;
    }

    public User(String userId, String email){
        this.userId = userId;
        this.email = email;
        this.createdAt = new Date();
        this.active = false;
        this.interviewed = false;
        this.didFirstWorkout = false;
        this.scheduledFirstWorkout = false;
        this.appUser = true;
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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        return appUser;
    }

    public void setAppUser(boolean appUser) {
        this.appUser = appUser;
    }

    public Date getFirstWorkoutDate() {
        return firstWorkoutDate;
    }

    public void setFirstWorkoutDate(Date firstWorkoutDate) {
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

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
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

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }
}
