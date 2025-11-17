package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.AcademyInfo;
import com.ifrs.movimentaif.movimentaifapi.service.AcademyInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/academy-info")
public class AcademyInfoController {
    
    private final AcademyInfoService academyInfoService;

    public AcademyInfoController(AcademyInfoService academyInfoService) {
        this.academyInfoService = academyInfoService;
    }

    @GetMapping
    public AcademyInfo getAcademyInfo() throws ExecutionException, InterruptedException {
        return academyInfoService.getAcademyInfo();
    }

    @PostMapping
    public AcademyInfo createAcademyInfo(@RequestBody AcademyInfo academyInfo) throws ExecutionException, InterruptedException {
        return academyInfoService.saveAcademyInfo(academyInfo);
    }

    @PutMapping
    public AcademyInfo updateAcademyInfo(@RequestBody AcademyInfo academyInfo) throws ExecutionException, InterruptedException {
        return academyInfoService.updateAcademyInfo(academyInfo);
    }

    @DeleteMapping
    public void deleteAcademyInfo() throws ExecutionException, InterruptedException {
        academyInfoService.deleteAcademyInfo();
    }
}
