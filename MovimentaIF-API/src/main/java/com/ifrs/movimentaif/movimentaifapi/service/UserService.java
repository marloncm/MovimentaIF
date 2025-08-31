package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.ifrs.movimentaif.movimentaifapi.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
public class UserService {
Logger logger = Logger.getLogger(UserService.class.getName());

    public void saveUser(User user) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("users")
                .document(user.getUid())
                .set(user);
    }

    public User getUser(String uid) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            return db.collection("users")
                    .document(uid)
                    .get()
                    .get()
                    .toObject(User.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Reinterrompe a thread
            logger.severe("Thread interrompida ao buscar usuário: " + e.getMessage());
            return null;
        } catch (ExecutionException e) {
            logger.severe("Erro ao buscar usuário: " + e.getMessage());
            return null;
        }
    }

}
