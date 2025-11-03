package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.WorkoutHistory;
import com.ifrs.movimentaif.movimentaifapi.service.WorkoutHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workout-history")
public class WorkoutHistoryController {

    private final WorkoutHistoryService workoutHistoryService;

    public WorkoutHistoryController(WorkoutHistoryService workoutHistoryService) {
        this.workoutHistoryService = workoutHistoryService;
    }

    @PostMapping
    public ResponseEntity<WorkoutHistory> createWorkoutHistory(WorkoutHistory workoutHistory) {
        WorkoutHistory savedHistory = workoutHistoryService.saveWorkoutHistory(workoutHistory);
        return ResponseEntity.ok(savedHistory);
    }

    @PutMapping
    public ResponseEntity<WorkoutHistory> updateWorkoutHistory(WorkoutHistory workoutHistory) {
        WorkoutHistory updatedHistory = workoutHistoryService.updateWorkoutHistory(workoutHistory);
        return ResponseEntity.ok(updatedHistory);
    }

    @GetMapping
    public ResponseEntity<WorkoutHistory> getWorkoutHistoryById(@RequestParam String id) {
        WorkoutHistory history = workoutHistoryService.getWorkoutHistoryById(id);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WorkoutHistory> getWorkoutHistoryByUserId(@PathVariable String userId) {
        WorkoutHistory history = workoutHistoryService.getWorkoutHistoryByUserId(userId);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
