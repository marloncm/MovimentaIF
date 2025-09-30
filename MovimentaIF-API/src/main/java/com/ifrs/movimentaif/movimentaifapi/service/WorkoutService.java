package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.ifrs.movimentaif.movimentaifapi.model.Workout;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class WorkoutService {
    private final Firestore db = FirestoreClient.getFirestore();
    private static final String COLLECTION_NAME = "workouts";


    public Workout saveWorkout(Workout workout) throws ExecutionException, InterruptedException {
        if (workout.getWorkoutId() == null || workout.getWorkoutId().isEmpty()){
            workout.setWorkoutId(UUID.randomUUID().toString());
        }
        ApiFuture<WriteResult> future = db.collection(COLLECTION_NAME).document(workout.getWorkoutId()).set(workout);
        return workout;
    }

    public Workout getWorkoutById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.toObject(Workout.class);
        } else {
            return null;
        }
    }

    // READ ALL
    public List<Workout> getAllWorkouts() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Workout> workouts = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            workouts.add(doc.toObject(Workout.class));
        }
        return workouts;
    }

    // UPDATE
    public Workout updateWorkout(String id, Workout workout) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        // get workout from db to check if exists
        Workout existingWorkout = getWorkoutById(id);
        existingWorkout.setWorkoutName(workout.getWorkoutName());
        existingWorkout.setWorkoutDescription(workout.getWorkoutDescription());
        existingWorkout.setWorkoutVideoLink(workout.getWorkoutVideoLink());
        ApiFuture<WriteResult> future = db.collection(COLLECTION_NAME).document(id).set(existingWorkout);
        return existingWorkout;
    }

    // DELETE
    public WriteResult deleteWorkout(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_NAME).document(id).delete();
        return writeResult.get();
    }
}
