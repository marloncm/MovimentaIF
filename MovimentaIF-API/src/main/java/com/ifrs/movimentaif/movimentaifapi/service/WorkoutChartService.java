package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.ifrs.movimentaif.movimentaifapi.dto.ScheduledUserDTO;
import com.ifrs.movimentaif.movimentaifapi.model.User;
import com.ifrs.movimentaif.movimentaifapi.model.WorkoutChart;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class WorkoutChartService {

    private final Firestore firestore;
    private final UserService userService;
    private static final String COLLECTION_NAME = "workoutCharts";

    public WorkoutChartService(Firestore firestore, UserService userService) {
        this.firestore = firestore;
        this.userService = userService;
    }


    /**
     * Salva ou atualiza uma ficha de treino (UPSERT).
     */
    public WorkoutChart saveOrUpdateChart(WorkoutChart chart) throws ExecutionException, InterruptedException {
        // Se for uma nova ficha (POST), gera o ID.
        if (chart.getChartId() == null || chart.getChartId().isEmpty()){
            chart.setChartId(UUID.randomUUID().toString());
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(chart.getChartId());
        ApiFuture<WriteResult> future = docRef.set(chart);
        future.get(); // Aguarda a conclusão da operação

        return chart;
    }

    /**
     * Busca uma ficha de treino pelo ID único da ficha.
     */
    public WorkoutChart getChartById(String chartId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(chartId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.toObject(WorkoutChart.class);
        } else {
            return null;
        }
    }

    /**
     * Busca a ficha de treino pelo ID do usuário (aluno). Usado para a página web.
     */
    public WorkoutChart getChartByUserId(String userId) throws ExecutionException, InterruptedException {
        // Para uma coleção pequena, buscamos todas as fichas e filtramos em memória.
        List<WorkoutChart> charts = firestore.collection(COLLECTION_NAME).get().get().toObjects(WorkoutChart.class);

        for (WorkoutChart chart : charts) {
            if (chart.getUserId().equals(userId)) {
                return chart;
            }
        }
        return null;
    }


    // --- Lógica de Escala (Agenda) ---

    private List<String> getWorkoutsForDay(WorkoutChart chart, String dayOfWeek) {
        return switch (dayOfWeek.toUpperCase()) {
            case "MONDAY" -> chart.getMondayWorkouts();
            case "TUESDAY" -> chart.getTuesdayWorkouts();
            case "WEDNESDAY" -> chart.getWednesdayWorkouts();
            case "THURSDAY" -> chart.getThursdayWorkouts();
            case "FRIDAY" -> chart.getFridayWorkouts();
            default -> List.of();
        };
    }

    public List<ScheduledUserDTO> getScheduledUsersByDay(String dayOfWeek) throws ExecutionException, InterruptedException {
        List<WorkoutChart> charts = firestore.collection(COLLECTION_NAME).get().get().toObjects(WorkoutChart.class);
        List<ScheduledUserDTO> scheduledUsers = new ArrayList<>();

        for (WorkoutChart chart : charts) {
            List<String> workoutIds = getWorkoutsForDay(chart, dayOfWeek);

            if (!workoutIds.isEmpty()) {
                // Busca os detalhes do usuário pelo ID do WorkoutChart
                User user = userService.getUserById(chart.getUserId());

                // Filtra: só adiciona se o usuário existir, for do app e estiver ativo
                if (user != null && user.isAppUser() && user.isActive()) {
                    scheduledUsers.add(new ScheduledUserDTO(
                            user.getUserId(),
                            user.getUserName(),
                            workoutIds, // Lista completa de IDs para o frontend mapear
                            chart.getChartId()
                    ));
                }
            }
        }
        return scheduledUsers;
    }

    public void deleteChart(String chartId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(chartId).delete().get();
    }

}
