package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.DailyWorkoutCompletion;
import com.ifrs.movimentaif.movimentaifapi.service.DailyWorkoutCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/workout-completions")
@CrossOrigin(origins = "*")
public class DailyWorkoutCompletionController {

    @Autowired
    private DailyWorkoutCompletionService completionService;

    // Criar uma nova conclusão de treino
    @PostMapping
    public ResponseEntity<DailyWorkoutCompletion> createCompletion(@RequestBody DailyWorkoutCompletion completion) throws ExecutionException, InterruptedException {
        DailyWorkoutCompletion savedCompletion = completionService.saveCompletion(completion);
        return new ResponseEntity<>(savedCompletion, HttpStatus.CREATED);
    }

    // Buscar todas as conclusões de um usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DailyWorkoutCompletion>> getCompletionsByUserId(@PathVariable String userId) throws ExecutionException, InterruptedException {
        List<DailyWorkoutCompletion> completions = completionService.getCompletionsByUserId(userId);
        return ResponseEntity.ok(completions);
    }

    // Buscar conclusões de um usuário para um dia específico
    @GetMapping("/user/{userId}/day/{dayOfWeek}")
    public ResponseEntity<List<DailyWorkoutCompletion>> getCompletionsByUserIdAndDay(
            @PathVariable String userId,
            @PathVariable String dayOfWeek) throws ExecutionException, InterruptedException {
        List<DailyWorkoutCompletion> completions = completionService.getCompletionsByUserIdAndDay(userId, dayOfWeek);
        return ResponseEntity.ok(completions);
    }

    // Verificar se foi completado hoje
    @GetMapping("/user/{userId}/day/{dayOfWeek}/today")
    public ResponseEntity<Boolean> isCompletedToday(
            @PathVariable String userId,
            @PathVariable String dayOfWeek) throws ExecutionException, InterruptedException {
        boolean isCompleted = completionService.isCompletedToday(userId, dayOfWeek);
        return ResponseEntity.ok(isCompleted);
    }

    // Buscar conclusão por ID
    @GetMapping("/{completionId}")
    public ResponseEntity<DailyWorkoutCompletion> getCompletionById(@PathVariable String completionId) throws ExecutionException, InterruptedException {
        DailyWorkoutCompletion completion = completionService.getCompletionById(completionId);
        if (completion != null) {
            return ResponseEntity.ok(completion);
        }
        return ResponseEntity.notFound().build();
    }

    // Deletar conclusão
    @DeleteMapping("/{completionId}")
    public ResponseEntity<Void> deleteCompletion(@PathVariable String completionId) throws ExecutionException, InterruptedException {
        completionService.deleteCompletion(completionId);
        return ResponseEntity.noContent().build();
    }

    // Obter total de treinos completados
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Integer> getTotalWorkoutsCompleted(@PathVariable String userId) throws ExecutionException, InterruptedException {
        int total = completionService.getTotalWorkoutsCompleted(userId);
        return ResponseEntity.ok(total);
    }

    // Obter dias ativos
    @GetMapping("/user/{userId}/active-days")
    public ResponseEntity<Integer> getActiveDaysCount(@PathVariable String userId) throws ExecutionException, InterruptedException {
        int activeDays = completionService.getActiveDaysCount(userId);
        return ResponseEntity.ok(activeDays);
    }
}
