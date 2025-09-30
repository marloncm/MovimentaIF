package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.Workout;
import com.ifrs.movimentaif.movimentaifapi.service.WorkoutService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }


    @PostMapping
    public void newWorkout(@RequestBody Workout workout) throws ExecutionException, InterruptedException {
        workoutService.saveWorkout(workout);
    }

    @GetMapping
    public List<Workout> getAllWorkouts() throws ExecutionException, InterruptedException {
        return workoutService.getAllWorkouts();
    }

    @GetMapping("/{id}")
    public Workout getWorkoutById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return workoutService.getWorkoutById(id);
    }

    @PutMapping("/{id}")
    public Workout updateWorkout(@PathVariable String id, @RequestBody Workout workout) throws ExecutionException, InterruptedException {
        return workoutService.updateWorkout(id, workout);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkout(@PathVariable String id) throws ExecutionException, InterruptedException {
        workoutService.deleteWorkout(id);
    }
}
