package com.ifrs.movimentaif.movimentaifapi.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.ifrs.movimentaif.movimentaifapi.model.ParQ;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ParQRepository {
    private static final String COLLECTION_NAME = "parq";

    public String saveParQ(ParQ parq) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<com.google.cloud.firestore.WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME)
                .document(parq.getParqId()).set(parq);
        return writeResult.get().getUpdateTime().toString();
    }

    public ParQ getParQById(String parqId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(parqId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return document.toObject(ParQ.class);
        }
        return null;
    }

    public ParQ getParQByUserId(String userId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId).get();
        QuerySnapshot querySnapshot = future.get();
        
        if (!querySnapshot.isEmpty()) {
            return querySnapshot.getDocuments().get(0).toObject(ParQ.class);
        }
        return null;
    }

    public String updateParQ(ParQ parq) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<com.google.cloud.firestore.WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME)
                .document(parq.getParqId()).set(parq);
        return writeResult.get().getUpdateTime().toString();
    }

    public String deleteParQ(String parqId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<com.google.cloud.firestore.WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME)
                .document(parqId).delete();
        return writeResult.get().getUpdateTime().toString();
    }

    public List<ParQ> getAllParQ() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.toObjects(ParQ.class);
    }
}
