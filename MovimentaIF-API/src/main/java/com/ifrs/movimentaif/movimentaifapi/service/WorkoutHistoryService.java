package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.ifrs.movimentaif.movimentaifapi.model.WorkoutHistory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class WorkoutHistoryService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "workoutHistory";

    public WorkoutHistoryService(Firestore firestore) {
        this.firestore = firestore;
    }

    public WorkoutHistory saveWorkoutHistory(WorkoutHistory workoutHistory) {
        try{
            if (workoutHistory.getHistoryId() == null || workoutHistory.getHistoryId().isEmpty()) {
                workoutHistory.setHistoryId(UUID.randomUUID().toString());
            }
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(workoutHistory.getHistoryId());
            ApiFuture<WriteResult> future = docRef.set(workoutHistory);
            future.get();
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

    public WorkoutHistory getWorkoutHistoryById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.toObject(WorkoutHistory.class);
        } else {
            return null;
        }
    }

    public WorkoutHistory getWorkoutHistoryByUserId(String userId) throws ExecutionException, InterruptedException {
        // Usando query WhereEqualTo para ser mais eficiente
        List<WorkoutHistory> histories = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .limit(1) // Assumimos que cada usuário tem apenas um registro de histórico
                .get()
                .get()
                .toObjects(WorkoutHistory.class);

        return histories.isEmpty() ? null : histories.get(0);
    }

    public WorkoutHistory updateWorkoutHistory(String historyId, WorkoutHistory history) throws ExecutionException, InterruptedException {
        history.setHistoryId(historyId); // Garante que o ID do path seja usado
        return saveWorkoutHistory(history);
    }


}
