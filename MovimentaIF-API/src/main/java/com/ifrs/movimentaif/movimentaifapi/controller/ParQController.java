package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.ParQ;
import com.ifrs.movimentaif.movimentaifapi.service.ParQService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/parq")
public class ParQController {
    private final ParQService parQService;

    public ParQController(ParQService parQService) {
        this.parQService = parQService;
    }

    @PostMapping
    public ParQ createParQ(@RequestBody ParQ parQ) throws ExecutionException, InterruptedException {
        return parQService.saveParQ(parQ);
    }

    @GetMapping("/{parqId}")
    public ParQ getParQById(@PathVariable String parqId) throws ExecutionException, InterruptedException {
        return parQService.getParQById(parqId);
    }

    @GetMapping("/user/{userId}")
    public ParQ getParQByUserId(@PathVariable String userId) throws ExecutionException, InterruptedException {
        return parQService.getParQByUserId(userId);
    }

    @PutMapping("/{parqId}")
    public ParQ updateParQ(@PathVariable String parqId, @RequestBody ParQ parQ) throws ExecutionException, InterruptedException {
        return parQService.updateParQ(parqId, parQ);
    }

    @DeleteMapping("/{parqId}")
    public String deleteParQ(@PathVariable String parqId) throws ExecutionException, InterruptedException {
        return parQService.deleteParQ(parqId);
    }
}
