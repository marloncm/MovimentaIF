package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.Anamnese;
import com.ifrs.movimentaif.movimentaifapi.service.AnamneseService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/anamnese")
public class AnamneseController {
    private final AnamneseService anamneseService;

    public AnamneseController(AnamneseService anamneseService) {
        this.anamneseService = anamneseService;
    }

    @PostMapping
    public Anamnese createAnamnese(@RequestBody Anamnese anamnese) throws ExecutionException, InterruptedException {
        return anamneseService.saveAnamnese(anamnese);
    }

    @GetMapping("/{anamneseId}")
    public Anamnese getAnamneseById(@PathVariable String anamneseId) throws ExecutionException, InterruptedException {
        return anamneseService.getAnamneseById(anamneseId);
    }

    @GetMapping("/user/{userId}")
    public Anamnese getAnamneseByUserId(@PathVariable String userId) throws ExecutionException, InterruptedException {
        return anamneseService.getAnamneseByUserId(userId);
    }

    @PutMapping("/{anamneseId}")
    public Anamnese updateAnamnese(@PathVariable String anamneseId, @RequestBody Anamnese anamnese) throws ExecutionException, InterruptedException {
        return anamneseService.updateAnamnese(anamneseId, anamnese);
    }

    @DeleteMapping("/{anamneseId}")
    public String deleteAnamnese(@PathVariable String anamneseId) throws ExecutionException, InterruptedException {
        return anamneseService.deleteAnamnese(anamneseId);
    }
}
