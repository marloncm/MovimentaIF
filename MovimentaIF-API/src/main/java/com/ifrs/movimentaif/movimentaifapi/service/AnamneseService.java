package com.ifrs.movimentaif.movimentaifapi.service;

import com.ifrs.movimentaif.movimentaifapi.model.Anamnese;
import com.ifrs.movimentaif.movimentaifapi.repository.AnamneseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class AnamneseService {
    private final AnamneseRepository anamneseRepository;

    public AnamneseService(AnamneseRepository anamneseRepository) {
        this.anamneseRepository = anamneseRepository;
    }

    public Anamnese saveAnamnese(Anamnese anamnese) throws ExecutionException, InterruptedException {
        if (anamnese.getAnamneseId() == null || anamnese.getAnamneseId().isEmpty()) {
            anamnese.setAnamneseId(UUID.randomUUID().toString());
        }
        anamneseRepository.saveAnamnese(anamnese);
        return anamnese;
    }

    public Anamnese getAnamneseById(String anamneseId) throws ExecutionException, InterruptedException {
        return anamneseRepository.getAnamneseById(anamneseId);
    }

    public Anamnese getAnamneseByUserId(String userId) throws ExecutionException, InterruptedException {
        return anamneseRepository.getAnamneseByUserId(userId);
    }

    public Anamnese updateAnamnese(String anamneseId, Anamnese anamnese) throws ExecutionException, InterruptedException {
        anamnese.setAnamneseId(anamneseId);
        anamneseRepository.updateAnamnese(anamnese);
        return anamnese;
    }

    public String deleteAnamnese(String anamneseId) throws ExecutionException, InterruptedException {
        return anamneseRepository.deleteAnamnese(anamneseId);
    }

    public List<Anamnese> getAllAnamnese() throws ExecutionException, InterruptedException {
        return anamneseRepository.getAllAnamnese();
    }
}
