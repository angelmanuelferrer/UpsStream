package es.us.dp1.l4_04_24_25.Upstream.statistics;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    @Test
    void testGetAllGames() {
        // Preparación de datos de prueba
        Statistics stats1 = new Statistics();
        stats1.setMatchesPlayed(10);
        stats1.setMatchesWon(5);
        stats1.setMatchesLost(5);
        stats1.setPoints(15);

        Statistics stats2 = new Statistics();
        stats2.setMatchesPlayed(20);
        stats2.setMatchesWon(10);
        stats2.setMatchesLost(10);
        stats2.setPoints(30);

        List<Statistics> mockStatistics = Arrays.asList(stats1, stats2);

        // Configuración del comportamiento del mock
        when(statisticsService.getStatistics()).thenReturn(mockStatistics);

        // Ejecución del método bajo prueba
        List<Statistics> result = statisticsController.getAllGames(null);

        // Verificaciones
        assertNotNull(result); // Verifica que la lista no sea nula
        assertEquals(2, result.size()); // Verifica el tamaño de la lista
        assertEquals(stats1, result.get(0)); // Verifica el primer elemento
        assertEquals(stats2, result.get(1)); // Verifica el segundo elemento

        // Verifica que el servicio fue llamado exactamente una vez
        verify(statisticsService, times(1)).getStatistics();
    }
}
