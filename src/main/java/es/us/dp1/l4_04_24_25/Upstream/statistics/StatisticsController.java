package es.us.dp1.l4_04_24_25.Upstream.statistics;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/statistics")
@Tag(name = "Statistics", description = "API for the management of Statistics")
public class StatisticsController {

    StatisticsService statisticsService;

    @Autowired
    public StatisticsController (StatisticsService ss){
        this.statisticsService = ss;
    }

    @GetMapping
    @Operation(summary = "Obtiene todas las estadísticas", description = "Recupera todas las estadísticas.")
    public List<Statistics> getAllGames(@ParameterObject() @RequestParam(value="name",required = false) String name){
        return statisticsService.getStatistics();
    }
}
