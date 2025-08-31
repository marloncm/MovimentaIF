package com.ifrs.movimentaif.movimentaifapi.service;


import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentReference;
import com.ifrs.movimentaif.movimentaifapi.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final Firestore firestore;

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    public void saveUser(User user) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(user.getUid());
        docRef.set(user).get();
    }

    public User getUserById(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(uid);
        return docRef.get().get().toObject(User.class);
    }
}
