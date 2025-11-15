package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.ifrs.movimentaif.movimentaifapi.model.WorkoutCompletion;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class WorkoutCompletionService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "workoutCompletions";

    public WorkoutCompletionService(Firestore firestore) {
        this.firestore = firestore;
    }

    public WorkoutCompletion createCompletion(WorkoutCompletion completion) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(completion.getCompletionId())
                .set(completion)
                .get();
        return completion;
    }

    public List<WorkoutCompletion> getCompletionsByUserId(String userId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .get()
                .getDocuments();

        return documents.stream()
                .map(doc -> doc.toObject(WorkoutCompletion.class))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getUserStats(String userId) throws ExecutionException, InterruptedException {
        List<WorkoutCompletion> completions = getCompletionsByUserId(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalWorkouts", completions.size());
        
        // Calcular dias únicos
        Set<String> uniqueDays = completions.stream()
                .map(c -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(c.getCompletedDate());
                    return cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.DAY_OF_YEAR);
                })
                .collect(Collectors.toSet());
        
        stats.put("activeDays", uniqueDays.size());
        
        // Último treino
        Optional<WorkoutCompletion> lastWorkout = completions.stream()
                .max(Comparator.comparing(WorkoutCompletion::getCompletedDate));
        
        stats.put("lastWorkoutDate", lastWorkout.map(WorkoutCompletion::getCompletedDate).orElse(null));
        
        return stats;
    }

    public void deleteCompletion(String completionId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(completionId)
                .delete()
                .get();
    }
}
