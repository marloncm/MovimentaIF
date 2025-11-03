package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.UserWorkout;
import com.ifrs.movimentaif.movimentaifapi.service.UserWorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user-workouts")
public class UserWorkoutController {

    private final UserWorkoutService userWorkoutService;

    public UserWorkoutController(UserWorkoutService userWorkoutService) {
        this.userWorkoutService = userWorkoutService;
    }

    @PostMapping
    public ResponseEntity<UserWorkout> createUserWorkout(@RequestBody UserWorkout userWorkout) {
        userWorkoutService.saveUserWorkout(userWorkout);
        return ResponseEntity.ok(userWorkout);
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<UserWorkout>> getUserWorkoutByUserId(@PathVariable String userId) {
//        List<UserWorkout> userWorkouts = userWorkoutService.getUserWorkoutsByUserId(userId);
//        if (userWorkouts != null) {
//            return ResponseEntity.ok(userWorkouts);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWorkout> getUserWorkoutById(@PathVariable String id) throws ExecutionException, InterruptedException {
        UserWorkout userWorkout = userWorkoutService.getUserWorkoutById(id);
        if (userWorkout != null) {
            return ResponseEntity.ok(userWorkout);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserWorkout>> getUserWorkoutsByUserId(@PathVariable String userId) {
        List<UserWorkout> userWorkouts = userWorkoutService.getUserWorkoutsByUserId(userId);
        if (userWorkouts != null) {
            return ResponseEntity.ok(userWorkouts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
