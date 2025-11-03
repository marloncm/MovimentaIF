package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.dto.ScheduledUserDTO;
import com.ifrs.movimentaif.movimentaifapi.service.WorkoutChartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final WorkoutChartService chartService;

    // Injeção de dependência do novo serviço
    public AdminController(WorkoutChartService chartService) {
        this.chartService = chartService;
    }

    /**
     * Retorna a lista de usuários ativos com Workouts agendados para o dia da semana especificado.
     * @param dayOfWeek (Ex: MONDAY, TUESDAY)
     * @return Lista de ScheduledUserDTO
     */
    @GetMapping("/schedule/day/{dayOfWeek}")
    public List<ScheduledUserDTO> getScheduledUsersByDay(@PathVariable String dayOfWeek) throws ExecutionException, InterruptedException {
        return chartService.getScheduledUsersByDay(dayOfWeek);
    }
}
