package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.ifrs.movimentaif.movimentaifapi.model.UserWorkout;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserWorkoutService {
    private final Firestore firestore;

    public UserWorkoutService(Firestore firestore) {
        this.firestore = firestore;
    }

    public String saveUserWorkout(UserWorkout userWorkout) {
       try{
           if (userWorkout.getUserWorkoutId() == null) {
               userWorkout.setUserWorkoutId(java.util.UUID.randomUUID().toString());
           }
           String userWorkoutId = userWorkout.getUserWorkoutId();

           DocumentReference docRef = firestore.collection("userWorkouts").document(userWorkoutId);
           docRef.set(userWorkout).get();
           return userWorkoutId;
       }catch (ExecutionException e){
           System.err.println("Error saving user workout: " + e.getMessage());
           return  null;
       }catch (InterruptedException e){
           System.err.println("Error saving user workout: " + e.getMessage());
           Thread.currentThread().interrupt();
           return  null;
       }
    }

    public UserWorkout getUserWorkoutById(String uid) throws ExecutionException, InterruptedException{
        try {
            DocumentReference docRef = firestore.collection("userWorkouts").document(uid);
            return docRef.get().get().toObject(UserWorkout.class);
        }catch (NullPointerException e){
            return null;
        }
    }


    public List<UserWorkout> getUserWorkoutsByUserId(String userId) {
        try {
            var query = firestore.collection("userWorkouts").whereEqualTo("userId", userId);
            var querySnapshot = query.get().get();
            return querySnapshot.toObjects(UserWorkout.class);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error fetching user workouts: " + e.getMessage());
            return null;
        }
    }
}
