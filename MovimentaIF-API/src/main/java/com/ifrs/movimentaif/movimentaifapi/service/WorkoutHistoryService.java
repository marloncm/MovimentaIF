package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.ifrs.movimentaif.movimentaifapi.model.WorkoutHistory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class WorkoutHistoryService {
    private final Firestore firestore;

    public WorkoutHistoryService(Firestore firestore) {
        this.firestore = firestore;
    }

    public WorkoutHistory saveWorkoutHistory(WorkoutHistory workoutHistory) {
        try{
            if (workoutHistory.getHistoryId() == null) {
                workoutHistory.setHistoryId(UUID.randomUUID().toString());
            }
            DocumentReference docRef = firestore.collection("workoutHistory").document(workoutHistory.getHistoryId());
            docRef.set(workoutHistory).get();
            return workoutHistory;
        }catch (ExecutionException e){
            System.err.println("Error saving workout history: " + e.getMessage());
            return null;
        }catch (InterruptedException e){
            System.err.println("Error saving workout history: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public WorkoutHistory getWorkoutHistoryById(String workoutHistoryId) {
        try {
            DocumentReference docRef = firestore.collection("workoutHistory").document(workoutHistoryId);
            return docRef.get().get().toObject(WorkoutHistory.class);
        }catch (NullPointerException e){
            return null;
        }catch (ExecutionException e){
            System.err.println("Error getting workout history by ID: " + e.getMessage());
            return null;
        }catch (InterruptedException e){
            System.err.println("Error getting workout history by ID: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }

    }

    public WorkoutHistory getWorkoutHistoryByUserId(String userId) {
        try {
            return firestore.collection("workoutHistory")
                    .whereEqualTo("userId", userId)
                    .get()
                    .get()
                    .toObjects(WorkoutHistory.class)
                    .stream()
                    .findFirst()
                    .orElse(null);
        } catch (ExecutionException e) {
            System.err.println("Error getting workout history by user ID: " + e.getMessage());
            return null;
        } catch (InterruptedException e) {
            System.err.println("Error getting workout history by user ID: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public WorkoutHistory updateWorkoutHistory(WorkoutHistory workoutHistory) {
        try{
            DocumentReference docRef = firestore.collection("workoutHistory").document(workoutHistory.getHistoryId());
            if(docRef.get().get().exists()){
                docRef.set(workoutHistory).get();
                return workoutHistory;
            }else{
                System.err.println("Workout history with ID " + workoutHistory.getHistoryId() + " does not exist.");
                return null;
            }
        }catch (ExecutionException e){
            System.err.println("Error updating workout history: " + e.getMessage());
            return null;
        }catch (InterruptedException e){
            System.err.println("Error updating workout history: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }


}
