package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.AcademyInfo;
import com.ifrs.movimentaif.movimentaifapi.service.AcademyInfoService;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/init")
public class InitDataController {

    private final AcademyInfoService academyInfoService;

    public InitDataController(AcademyInfoService academyInfoService) {
        this.academyInfoService = academyInfoService;
    }

    @PostMapping("/academy-info")
    public AcademyInfo initializeAcademyInfo() throws Exception {
        // Verificar se já existe
        AcademyInfo existing = academyInfoService.getAcademyInfo();
        if (existing != null) {
            return existing;
        }

        // Criar dados iniciais
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        AcademyInfo academyInfo = new AcademyInfo();
        academyInfo.setStartDate(dateFormat.parse("01/08/2025"));
        academyInfo.setEndDate(dateFormat.parse("12/12/2025"));
        academyInfo.setOpenHour("09:00");
        academyInfo.setCloseHour("19:00");
        academyInfo.setAdditionalInfo("Fechado em feriados");

        return academyInfoService.saveAcademyInfo(academyInfo);
    }
}
