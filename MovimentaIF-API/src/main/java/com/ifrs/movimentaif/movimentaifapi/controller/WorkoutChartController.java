package com.ifrs.movimentaif.movimentaifapi.controller;


import com.ifrs.movimentaif.movimentaifapi.dto.ScheduledUserDTO;
import com.ifrs.movimentaif.movimentaifapi.model.WorkoutChart;
import com.ifrs.movimentaif.movimentaifapi.service.WorkoutChartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/charts")
public class WorkoutChartController {

    private final WorkoutChartService chartService;

    public WorkoutChartController(WorkoutChartService chartService) {
        this.chartService = chartService;
    }

    // --- ROTA P/ LISTA DE ESCALA DA AGENDA (Usada pelo AdminController) ---

    @GetMapping("/day/{dayOfWeek}")
    public List<ScheduledUserDTO> getScheduledUsersByDay(@PathVariable String dayOfWeek) throws ExecutionException, InterruptedException {
        return chartService.getScheduledUsersByDay(dayOfWeek);
    }

    // --- ROTAS DE GERENCIAMENTO (Usadas pela user-workout-chart.html) ---

    /**
     * Busca a ficha de treino pelo ID do usuário. Rota ideal para o frontend.
     * @param userId O ID do usuário (aluno).
     * @return 200 OK com o Chart ou 404 NOT_FOUND.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<WorkoutChart> getChartByUserId(@PathVariable String userId) throws ExecutionException, InterruptedException {
        WorkoutChart chart = chartService.getChartByUserId(userId);
        if (chart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(chart, HttpStatus.OK);
    }

    /**
     * Busca a ficha de treino pelo ID do CHART (Usada para rotas internas/depuração).
     */
    @GetMapping("/{chartId}")
    public ResponseEntity<WorkoutChart> getChartById(@PathVariable String chartId) throws ExecutionException, InterruptedException {
        WorkoutChart chart = chartService.getChartById(chartId);
        if (chart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(chart, HttpStatus.OK);
    }

    /**
     * Cria uma nova ficha de treino (POST).
     * @param chart O objeto WorkoutChart com userId e listas de treinos.
     * @return 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<WorkoutChart> createChart(@RequestBody WorkoutChart chart) throws ExecutionException, InterruptedException {
        // O serviço irá gerar o chartId e salvar.
        WorkoutChart newChart = chartService.saveOrUpdateChart(chart);
        return new ResponseEntity<>(newChart, HttpStatus.CREATED);
    }

    /**
     * Atualiza uma ficha de treino existente (PUT).
     * @param chartId O ID do chart a ser atualizado.
     * @param chart O objeto WorkoutChart com os novos dados.
     * @return 200 OK.
     */
    @PutMapping("/{chartId}")
    public ResponseEntity<WorkoutChart> updateChart(@PathVariable String chartId, @RequestBody WorkoutChart chart) throws ExecutionException, InterruptedException {
        // Garante que o ID da URL seja usado para salvar
        chart.setChartId(chartId);
        WorkoutChart updatedChart = chartService.saveOrUpdateChart(chart);
        return new ResponseEntity<>(updatedChart, HttpStatus.OK);
    }
}
