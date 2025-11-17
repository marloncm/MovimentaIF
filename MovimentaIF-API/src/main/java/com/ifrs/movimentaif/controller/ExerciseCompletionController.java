package com.ifrs.movimentaif.controller;

import com.ifrs.movimentaif.model.ExerciseCompletion;
import com.ifrs.movimentaif.service.ExerciseCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/exercise-completions")
public class ExerciseCompletionController {

    @Autowired
    private ExerciseCompletionService exerciseCompletionService;

    @PostMapping
    public ResponseEntity<ExerciseCompletion> createExerciseCompletion(@RequestBody ExerciseCompletion completion) {
        try {
            ExerciseCompletion savedCompletion = exerciseCompletionService.saveCompletion(completion);
            return ResponseEntity.ok(savedCompletion);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExerciseCompletion>> getExerciseCompletionsByUserId(@PathVariable String userId) {
        try {
            List<ExerciseCompletion> completions = exerciseCompletionService.getCompletionsByUserId(userId);
            return ResponseEntity.ok(completions);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/exercise/{userWorkoutId}/today")
    public ResponseEntity<Boolean> isExerciseCompletedToday(
            @PathVariable String userId,
            @PathVariable String userWorkoutId) {
        try {
            boolean isCompleted = exerciseCompletionService.isExerciseCompletedToday(userId, userWorkoutId);
            return ResponseEntity.ok(isCompleted);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/day/{dayOfWeek}/today")
    public ResponseEntity<Integer> getCompletedExercisesCountToday(
            @PathVariable String userId,
            @PathVariable String dayOfWeek) {
        try {
            int count = exerciseCompletionService.getCompletedExercisesCountToday(userId, dayOfWeek);
            return ResponseEntity.ok(count);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Integer> getTotalExercisesCompleted(@PathVariable String userId) {
        try {
            int total = exerciseCompletionService.getTotalExercisesCompleted(userId);
            return ResponseEntity.ok(total);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
