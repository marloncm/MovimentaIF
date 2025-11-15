package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.ifrs.movimentaif.movimentaifapi.model.DailyWorkoutCompletion;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class DailyWorkoutCompletionService {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "dailyWorkoutCompletions";

    public DailyWorkoutCompletionService(Firestore firestore) {
        this.firestore = firestore;
    }

    // Criar ou atualizar uma conclusão de treino diário
    public DailyWorkoutCompletion saveCompletion(DailyWorkoutCompletion completion) throws ExecutionException, InterruptedException {
        try {
            if (completion.getCompletionId() == null || completion.getCompletionId().isEmpty()) {
                completion.setCompletionId(UUID.randomUUID().toString());
            }
            
            if (completion.getCompletedDate() == null) {
                completion.setCompletedDate(new Date());
            }

            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(completion.getCompletionId());
            ApiFuture<WriteResult> future = docRef.set(completion);
            future.get();
            return completion;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar conclusão do treino: " + e.getMessage());
        }
    }

    // Buscar todas as conclusões de um usuário
    public List<DailyWorkoutCompletion> getCompletionsByUserId(String userId) throws ExecutionException, InterruptedException {
        List<DailyWorkoutCompletion> completions = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .get()
                .toObjects(DailyWorkoutCompletion.class);
        
        return completions != null ? completions : new ArrayList<>();
    }

    // Buscar conclusões de um usuário para um dia específico
    public List<DailyWorkoutCompletion> getCompletionsByUserIdAndDay(String userId, String dayOfWeek) throws ExecutionException, InterruptedException {
        List<DailyWorkoutCompletion> completions = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("dayOfWeek", dayOfWeek)
                .get()
                .get()
                .toObjects(DailyWorkoutCompletion.class);
        
        return completions != null ? completions : new ArrayList<>();
    }

    // Verificar se um usuário completou um treino em um dia específico hoje
    public boolean isCompletedToday(String userId, String dayOfWeek) throws ExecutionException, InterruptedException {
        List<DailyWorkoutCompletion> completions = getCompletionsByUserIdAndDay(userId, dayOfWeek);
        
        if (completions.isEmpty()) {
            return false;
        }

        // Verificar se alguma conclusão foi feita hoje
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());

        for (DailyWorkoutCompletion completion : completions) {
            if (completion.getCompletedDate() != null) {
                String completionDate = dateFormat.format(completion.getCompletedDate());
                if (today.equals(completionDate)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Buscar conclusão por ID
    public DailyWorkoutCompletion getCompletionById(String completionId) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME)
                .document(completionId)
                .get()
                .get();

        if (document.exists()) {
            return document.toObject(DailyWorkoutCompletion.class);
        }
        return null;
    }

    // Deletar conclusão
    public void deleteCompletion(String completionId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(completionId).delete().get();
    }

    // Contar treinos realizados pelo usuário
    public int getTotalWorkoutsCompleted(String userId) throws ExecutionException, InterruptedException {
        List<DailyWorkoutCompletion> completions = getCompletionsByUserId(userId);
        return completions.size();
    }

    // Contar dias ativos (dias únicos em que o usuário treinou)
    public int getActiveDaysCount(String userId) throws ExecutionException, InterruptedException {
        List<DailyWorkoutCompletion> completions = getCompletionsByUserId(userId);
        
        if (completions.isEmpty()) {
            return 0;
        }

        // Usar Set para contar dias únicos
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Set<String> uniqueDays = completions.stream()
                .filter(c -> c.getCompletedDate() != null)
                .map(c -> dateFormat.format(c.getCompletedDate()))
                .collect(Collectors.toSet());

        return uniqueDays.size();
    }
}
