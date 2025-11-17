package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.ifrs.movimentaif.movimentaifapi.model.AcademyInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class AcademyInfoService {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "academyInfo";
    private static final String DOCUMENT_ID = "academy-info-singleton";

    public AcademyInfoService(Firestore firestore) {
        this.firestore = firestore;
    }

    public AcademyInfo getAcademyInfo() throws ExecutionException, InterruptedException {
        try {
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(DOCUMENT_ID);
            AcademyInfo academyInfo = docRef.get().get().toObject(AcademyInfo.class);
            return academyInfo;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public AcademyInfo saveAcademyInfo(AcademyInfo academyInfo) throws ExecutionException, InterruptedException {
        try {
            academyInfo.setAcademyId(DOCUMENT_ID);
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(DOCUMENT_ID);
            docRef.set(academyInfo).get();
            return academyInfo;
        } catch (ExecutionException e) {
            System.err.println("Error saving academy info: " + e.getMessage());
            throw e;
        } catch (InterruptedException e) {
            System.err.println("Error saving academy info: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    public AcademyInfo updateAcademyInfo(AcademyInfo academyInfo) throws ExecutionException, InterruptedException {
        AcademyInfo existing = getAcademyInfo();
        if (existing != null) {
            existing.setStartDate(academyInfo.getStartDate() != null ? academyInfo.getStartDate() : existing.getStartDate());
            existing.setEndDate(academyInfo.getEndDate() != null ? academyInfo.getEndDate() : existing.getEndDate());
            existing.setOpenHour(academyInfo.getOpenHour() != null && !academyInfo.getOpenHour().isEmpty() ? academyInfo.getOpenHour() : existing.getOpenHour());
            existing.setCloseHour(academyInfo.getCloseHour() != null && !academyInfo.getCloseHour().isEmpty() ? academyInfo.getCloseHour() : existing.getCloseHour());
            existing.setAdditionalInfo(academyInfo.getAdditionalInfo() != null && !academyInfo.getAdditionalInfo().isEmpty() ? academyInfo.getAdditionalInfo() : existing.getAdditionalInfo());
            
            return saveAcademyInfo(existing);
        } else {
            return saveAcademyInfo(academyInfo);
        }
    }

    public void deleteAcademyInfo() throws ExecutionException, InterruptedException {
        try {
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(DOCUMENT_ID);
            docRef.delete().get();
        } catch (ExecutionException e) {
            System.err.println("Error deleting academy info: " + e.getMessage());
            throw e;
        } catch (InterruptedException e) {
            System.err.println("Error deleting academy info: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw e;
        }
    }
}
