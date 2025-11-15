package com.ifrs.movimentaif.movimentaifapi.service;


import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentReference;
import com.ifrs.movimentaif.movimentaifapi.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final Firestore firestore;

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    public void saveUser(User user) throws ExecutionException, InterruptedException {
        try{
            DocumentReference docRef = firestore.collection("users").document(user.getUserId());
            docRef.set(user).get();
        }catch (ExecutionException e){
            System.err.println("Error saving user: " + e.getMessage());
        }catch (InterruptedException e){
            System.err.println("Error saving user: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public User getUserById(String uid) throws ExecutionException, InterruptedException {
        try {
            DocumentReference docRef = firestore.collection("users").document(uid);
            return docRef.get().get().toObject(User.class);
        }catch (NullPointerException e){
            return null;
        }
    }

    public List<User> getUsersByRole(String role){
        try{
            return firestore.collection("users").whereEqualTo("role", role).get().get().toObjects(User.class);
        }catch (ExecutionException e){
            System.err.println("Error getting users by role " + role + ": " + e.getMessage());
            return null;
        }catch (InterruptedException e){
            System.err.println("Error getting users by role " + role + ": " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public List<User> getAppUsers(){
        try{
            return firestore.collection("users").whereEqualTo("appUser", true).get().get().toObjects(User.class);
        }catch (ExecutionException e){
            System.err.println("Error getting app users: " + e.getMessage());
            return null;
        }catch (InterruptedException e){
            System.err.println("Error getting app users: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public List<User> getUsersByStatus(String status){
        try{
            return firestore.collection("users").whereEqualTo("status", status).get().get().toObjects(User.class);
        }catch (ExecutionException e){
            System.err.println("Error getting users by status " + status + ": " + e.getMessage());
            return null;
        }catch (InterruptedException e){
            System.err.println("Error getting users by status " + status + ": " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public User updateUser(String uid, User user) throws ExecutionException, InterruptedException {
        User existingUser = getUserById(uid);
        if (existingUser != null) {
            existingUser.setUserName(user.getUserName() != null && !user.getUserName().isEmpty() ? user.getUserName() : existingUser.getUserName());
            existingUser.setAge(user.getAge() != null ? user.getAge() : existingUser.getAge());
            existingUser.setPhoneNumber(user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty() ? user.getPhoneNumber() : existingUser.getPhoneNumber());
            existingUser.setEmail(user.getEmail() != null && !user.getEmail().isEmpty() ? user.getEmail() : existingUser.getEmail());
            existingUser.setRole(user.getRole() != null && !user.getRole().isEmpty() ? user.getRole() : existingUser.getRole());
            existingUser.setAffiliationType(user.getAffiliationType() != null && !user.getAffiliationType().isEmpty() ? user.getAffiliationType() : existingUser.getAffiliationType());
            existingUser.setActive(user.isActive() != existingUser.isActive() ? user.isActive() : existingUser.isActive());
            existingUser.setSignedTermOfCommitment(user.isSignedTermOfCommitment() != existingUser.isSignedTermOfCommitment() ? user.isSignedTermOfCommitment() : existingUser.isSignedTermOfCommitment());
            existingUser.setInterviewed(user.isInterviewed() != existingUser.isInterviewed() ? user.isInterviewed() : existingUser.isInterviewed());
            existingUser.setDidFirstWorkout(user.isDidFirstWorkout() != existingUser.isDidFirstWorkout() ? user.isDidFirstWorkout() : existingUser.isDidFirstWorkout());


            saveUser(existingUser);
            return existingUser;
        } else {
            saveUser(user);
            return user;
        }
    }
}
