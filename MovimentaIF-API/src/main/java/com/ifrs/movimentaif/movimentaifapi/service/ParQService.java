package com.ifrs.movimentaif.movimentaifapi.service;

import com.ifrs.movimentaif.movimentaifapi.model.ParQ;
import com.ifrs.movimentaif.movimentaifapi.repository.ParQRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ParQService {
    private final ParQRepository parQRepository;

    public ParQService(ParQRepository parQRepository) {
        this.parQRepository = parQRepository;
    }

    public ParQ saveParQ(ParQ parQ) throws ExecutionException, InterruptedException {
        if (parQ.getParqId() == null || parQ.getParqId().isEmpty()) {
            parQ.setParqId(UUID.randomUUID().toString());
        }
        parQRepository.saveParQ(parQ);
        return parQ;
    }

    public ParQ getParQById(String parqId) throws ExecutionException, InterruptedException {
        return parQRepository.getParQById(parqId);
    }

    public ParQ getParQByUserId(String userId) throws ExecutionException, InterruptedException {
        return parQRepository.getParQByUserId(userId);
    }

    public ParQ updateParQ(String parqId, ParQ parQ) throws ExecutionException, InterruptedException {
        parQ.setParqId(parqId);
        parQRepository.updateParQ(parQ);
        return parQ;
    }

    public String deleteParQ(String parqId) throws ExecutionException, InterruptedException {
        return parQRepository.deleteParQ(parqId);
    }
}
