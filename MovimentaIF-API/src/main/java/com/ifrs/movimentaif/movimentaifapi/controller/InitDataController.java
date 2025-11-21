package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.AcademyInfo;
import com.ifrs.movimentaif.movimentaifapi.model.ParQ;
import com.ifrs.movimentaif.movimentaifapi.model.Anamnese;
import com.ifrs.movimentaif.movimentaifapi.model.User;
import com.ifrs.movimentaif.movimentaifapi.service.AcademyInfoService;
import com.ifrs.movimentaif.movimentaifapi.service.ParQService;
import com.ifrs.movimentaif.movimentaifapi.service.AnamneseService;
import com.ifrs.movimentaif.movimentaifapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/init")
public class InitDataController {

    private final AcademyInfoService academyInfoService;
    private final ParQService parQService;
    private final AnamneseService anamneseService;
    private final UserService userService;

    public InitDataController(AcademyInfoService academyInfoService, ParQService parQService, 
                             AnamneseService anamneseService, UserService userService) {
        this.academyInfoService = academyInfoService;
        this.parQService = parQService;
        this.anamneseService = anamneseService;
        this.userService = userService;
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

    @PostMapping("/fix-user-forms")
    public Map<String, Object> fixUserForms() throws ExecutionException, InterruptedException {
        Map<String, Object> result = new HashMap<>();
        int parqFixed = 0;
        int anamneseFixed = 0;
        int errors = 0;

        try {
            // Buscar todos os PAR-Q
            List<ParQ> allParQ = parQService.getAllParQ();
            
            // Para cada PAR-Q, atualizar o usuário
            for (ParQ parq : allParQ) {
                try {
                    if (parq.getUserId() != null && parq.getParqId() != null) {
                        User user = userService.getUserById(parq.getUserId());
                        if (user != null) {
                            user.setParqId(parq.getParqId());
                            userService.updateUser(parq.getUserId(), user);
                            parqFixed++;
                        }
                    }
                } catch (Exception e) {
                    errors++;
                }
            }

            // Buscar todas as Anamneses
            List<Anamnese> allAnamnese = anamneseService.getAllAnamnese();
            
            // Para cada Anamnese, atualizar o usuário
            for (Anamnese anamnese : allAnamnese) {
                try {
                    if (anamnese.getUserId() != null && anamnese.getAnamneseId() != null) {
                        User user = userService.getUserById(anamnese.getUserId());
                        if (user != null) {
                            user.setAnamneseId(anamnese.getAnamneseId());
                            userService.updateUser(anamnese.getUserId(), user);
                            anamneseFixed++;
                        }
                    }
                } catch (Exception e) {
                    errors++;
                }
            }

            result.put("success", true);
            result.put("parqFixed", parqFixed);
            result.put("anamneseFixed", anamneseFixed);
            result.put("errors", errors);
            result.put("message", "Correção concluída: " + parqFixed + " PAR-Q e " + anamneseFixed + " Anamneses vinculados aos usuários");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erro: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/migrate-form-keys")
    public Map<String, Object> migrateFormKeys() throws ExecutionException, InterruptedException {
        Map<String, Object> result = new HashMap<>();
        int parqMigrated = 0;
        int anamneseMigrated = 0;
        int errors = 0;

        try {
            // Migrar PAR-Q: pergunta1-7 -> q1-7
            List<ParQ> allParQ = parQService.getAllParQ();
            
            for (ParQ parq : allParQ) {
                try {
                    Map<String, Boolean> oldRespostas = parq.getRespostas();
                    Map<String, Boolean> newRespostas = new HashMap<>();
                    
                    // Verificar se já está no formato novo
                    if (oldRespostas != null && oldRespostas.containsKey("pergunta1")) {
                        // Migrar de pergunta1-7 para q1-7
                        for (int i = 1; i <= 7; i++) {
                            Boolean value = oldRespostas.get("pergunta" + i);
                            if (value != null) {
                                newRespostas.put("q" + i, value);
                            }
                        }
                        
                        parq.setRespostas(newRespostas);
                        parQService.updateParQ(parq.getParqId(), parq);
                        parqMigrated++;
                    }
                } catch (Exception e) {
                    errors++;
                }
            }

            // Migrar Anamnese: pergunta1-7 -> q1-10
            List<Anamnese> allAnamnese = anamneseService.getAllAnamnese();
            
            for (Anamnese anamnese : allAnamnese) {
                try {
                    Map<String, Boolean> oldRespostas = anamnese.getRespostas();
                    Map<String, Boolean> newRespostas = new HashMap<>();
                    
                    // Verificar se já está no formato novo
                    if (oldRespostas != null && oldRespostas.containsKey("pergunta1")) {
                        // Migrar de pergunta1-7 para q1-10 (assumir false para q8-10 se não existirem)
                        for (int i = 1; i <= 7; i++) {
                            Boolean value = oldRespostas.get("pergunta" + i);
                            if (value != null) {
                                newRespostas.put("q" + i, value);
                            }
                        }
                        
                        // Adicionar q8-10 com valor false se não existirem
                        for (int i = 8; i <= 10; i++) {
                            newRespostas.put("q" + i, false);
                        }
                        
                        anamnese.setRespostas(newRespostas);
                        anamneseService.updateAnamnese(anamnese.getAnamneseId(), anamnese);
                        anamneseMigrated++;
                    }
                } catch (Exception e) {
                    errors++;
                }
            }

            result.put("success", true);
            result.put("parqMigrated", parqMigrated);
            result.put("anamneseMigrated", anamneseMigrated);
            result.put("errors", errors);
            result.put("message", "Migração concluída: " + parqMigrated + " PAR-Q e " + anamneseMigrated + " Anamneses migrados para o novo formato de chaves");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erro: " + e.getMessage());
            result.put("errorDetails", e.toString());
        }

        return result;
    }
}
