package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.WorkoutHistory;
import com.ifrs.movimentaif.movimentaifapi.service.WorkoutHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/workout-history")
public class WorkoutHistoryController {

    private final WorkoutHistoryService workoutHistoryService;

    public WorkoutHistoryController(WorkoutHistoryService workoutHistoryService) {
        this.workoutHistoryService = workoutHistoryService;
    }

    @PostMapping
    public ResponseEntity<WorkoutHistory> createWorkoutHistory(@RequestBody WorkoutHistory workoutHistory) throws ExecutionException, InterruptedException {
        WorkoutHistory savedHistory = workoutHistoryService.saveWorkoutHistory(workoutHistory);
        return new ResponseEntity<>(savedHistory, HttpStatus.CREATED);
    }

    @PutMapping("/{historyId}")
    public ResponseEntity<WorkoutHistory> updateWorkoutHistory(@PathVariable String historyId, @RequestBody WorkoutHistory workoutHistory) throws ExecutionException, InterruptedException {
        // Chamamos o novo método de atualização no serviço
        WorkoutHistory updatedHistory = workoutHistoryService.updateWorkoutHistory(historyId, workoutHistory);
        return ResponseEntity.ok(updatedHistory);
    }

    @GetMapping
    public ResponseEntity<WorkoutHistory> getWorkoutHistoryById(@RequestParam String id) throws ExecutionException, InterruptedException {
        WorkoutHistory history = workoutHistoryService.getWorkoutHistoryById(id);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WorkoutHistory> getWorkoutHistoryByUserId(@PathVariable String userId) throws ExecutionException, InterruptedException {
        WorkoutHistory history = workoutHistoryService.getWorkoutHistoryByUserId(userId);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
