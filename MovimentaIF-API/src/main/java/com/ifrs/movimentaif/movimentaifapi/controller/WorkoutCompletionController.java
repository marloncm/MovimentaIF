package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.WorkoutCompletion;
import com.ifrs.movimentaif.movimentaifapi.service.WorkoutCompletionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workout-completions")
public class WorkoutCompletionController {

    private final WorkoutCompletionService workoutCompletionService;

    public WorkoutCompletionController(WorkoutCompletionService workoutCompletionService) {
        this.workoutCompletionService = workoutCompletionService;
    }

    @PostMapping
    public ResponseEntity<WorkoutCompletion> createCompletion(@RequestBody WorkoutCompletion completion) {
        try {
            WorkoutCompletion created = workoutCompletionService.createCompletion(completion);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutCompletion>> getCompletionsByUserId(@PathVariable String userId) {
        try {
            List<WorkoutCompletion> completions = workoutCompletionService.getCompletionsByUserId(userId);
            return ResponseEntity.ok(completions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable String userId) {
        try {
            Map<String, Object> stats = workoutCompletionService.getUserStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{completionId}")
    public ResponseEntity<Void> deleteCompletion(@PathVariable String completionId) {
        try {
            workoutCompletionService.deleteCompletion(completionId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
