package com.ifrs.movimentaif.movimentaifapi.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.ifrs.movimentaif.movimentaifapi.model.Anamnese;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class AnamneseRepository {
    private static final String COLLECTION_NAME = "anamnese";

    public String saveAnamnese(Anamnese anamnese) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<com.google.cloud.firestore.WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME)
                .document(anamnese.getAnamneseId()).set(anamnese);
        return writeResult.get().getUpdateTime().toString();
    }

    public Anamnese getAnamneseById(String anamneseId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(anamneseId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return document.toObject(Anamnese.class);
        }
        return null;
    }

    public Anamnese getAnamneseByUserId(String userId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId).get();
        QuerySnapshot querySnapshot = future.get();
        
        if (!querySnapshot.isEmpty()) {
            return querySnapshot.getDocuments().get(0).toObject(Anamnese.class);
        }
        return null;
    }

    public String updateAnamnese(Anamnese anamnese) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<com.google.cloud.firestore.WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME)
                .document(anamnese.getAnamneseId()).set(anamnese);
        return writeResult.get().getUpdateTime().toString();
    }

    public String deleteAnamnese(String anamneseId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<com.google.cloud.firestore.WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME)
                .document(anamneseId).delete();
        return writeResult.get().getUpdateTime().toString();
    }
}
