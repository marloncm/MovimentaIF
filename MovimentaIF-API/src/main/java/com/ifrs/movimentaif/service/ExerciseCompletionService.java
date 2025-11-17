package com.ifrs.movimentaif.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.ifrs.movimentaif.model.ExerciseCompletion;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class ExerciseCompletionService {

    private static final String COLLECTION_NAME = "exerciseCompletions";

    public ExerciseCompletion saveCompletion(ExerciseCompletion completion) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        
        if (completion.getCompletionId() == null || completion.getCompletionId().isEmpty()) {
            completion.setCompletionId(UUID.randomUUID().toString());
        }
        
        if (completion.getCompletedDate() == null) {
            completion.setCompletedDate(new Date());
        }
        
        ApiFuture<WriteResult> result = db.collection(COLLECTION_NAME)
                .document(completion.getCompletionId())
                .set(completion);
        
        result.get();
        return completion;
    }

    public List<ExerciseCompletion> getCompletionsByUserId(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get();
        
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<ExerciseCompletion> completions = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            completions.add(document.toObject(ExerciseCompletion.class));
        }
        
        return completions;
    }

    public boolean isExerciseCompletedToday(String userId, String userWorkoutId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        
        // Obter data de hoje (início e fim)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();
        
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfDay = cal.getTime();
        
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("userWorkoutId", userWorkoutId)
                .whereGreaterThanOrEqualTo("completedDate", startOfDay)
                .whereLessThanOrEqualTo("completedDate", endOfDay)
                .get();
        
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return !documents.isEmpty();
    }

    public int getCompletedExercisesCountToday(String userId, String dayOfWeek) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        
        // Obter data de hoje
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();
        
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfDay = cal.getTime();
        
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("dayOfWeek", dayOfWeek)
                .whereGreaterThanOrEqualTo("completedDate", startOfDay)
                .whereLessThanOrEqualTo("completedDate", endOfDay)
                .get();
        
        return future.get().getDocuments().size();
    }

    public int getTotalExercisesCompleted(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get();
        
        return future.get().getDocuments().size();
    }
}
