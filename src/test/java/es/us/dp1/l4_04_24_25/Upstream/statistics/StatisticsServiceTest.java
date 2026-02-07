package es.us.dp1.l4_04_24_25.Upstream.statistics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

class StatisticsServiceTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStatistics() {
        // Arrange
        List<Statistics> statisticsList = List.of(new Statistics(), new Statistics());
        when(statisticsRepository.findAll()).thenReturn(statisticsList);

        // Act
        List<Statistics> result = statisticsService.getStatistics();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(statisticsRepository, times(1)).findAll();
    }

    @Test
    void testGetStatisticsByName_NotFound() {
        // Arrange
        when(statisticsRepository.findByName("testName")).thenReturn(null);

        // Act
        Statistics result = statisticsService.getStatisticsByName("testName");

        // Assert
        assertNull(result);
        verify(statisticsRepository, times(1)).findByName("testName");
    }

    @Test
    void testUpdateStatistics_NewStatistics() {
        // Arrange
        Board board = new Board();
        Match match = new Match();

        User winner = new User();
        winner.setUsername("winner");
        User loser = new User();
        loser.setUsername("loser");

        Player player1 = new Player();
        player1.setUser(winner);
        player1.setPoints(10);

        Player player2 = new Player();
        player2.setUser(loser);
        player2.setPoints(5);

        match.setPlayers(List.of(player1, player2));
        board.setMatch(match);
        board.setWinner(winner);

        when(statisticsRepository.findByUser(winner)).thenReturn(null);
        when(statisticsRepository.findByUser(loser)).thenReturn(null);

        // Act
        statisticsService.updateStatistics(board);

        // Assert
        // Verify new statistics created for winner
        verify(statisticsRepository, times(1)).findByUser(winner);
        verify(statisticsRepository, times(1)).save(argThat(statistics -> 
            statistics.getUser().equals(winner) &&
            statistics.getMatchesPlayed() == 1 &&
            statistics.getMatchesWon() == 1 &&
            statistics.getMatchesLost() == 0 &&
            statistics.getPoints() == 10
        ));

        // Verify new statistics created for loser
        verify(statisticsRepository, times(1)).findByUser(loser);
        verify(statisticsRepository, times(1)).save(argThat(statistics -> 
            statistics.getUser().equals(loser) &&
            statistics.getMatchesPlayed() == 1 &&
            statistics.getMatchesWon() == 0 &&
            statistics.getMatchesLost() == 1 &&
            statistics.getPoints() == 5
        ));
    }

    @Test
    void testUpdateStatistics_ExistingStatistics() {
        // Arrange
        Board board = new Board();
        Match match = new Match();

        User winner = new User();
        winner.setUsername("winner");
        User loser = new User();
        loser.setUsername("loser");

        Player player1 = new Player();
        player1.setUser(winner);
        player1.setPoints(15);

        Player player2 = new Player();
        player2.setUser(loser);
        player2.setPoints(5);

        match.setPlayers(List.of(player1, player2));
        board.setMatch(match);
        board.setWinner(winner);

        Statistics winnerStats = new Statistics();
        winnerStats.setUser(winner);
        winnerStats.setMatchesPlayed(5);
        winnerStats.setMatchesWon(3);
        winnerStats.setMatchesLost(2);
        winnerStats.setPoints(50);

        Statistics loserStats = new Statistics();
        loserStats.setUser(loser);
        loserStats.setMatchesPlayed(5);
        loserStats.setMatchesWon(2);
        loserStats.setMatchesLost(3);
        loserStats.setPoints(30);

        when(statisticsRepository.findByUser(winner)).thenReturn(winnerStats);
        when(statisticsRepository.findByUser(loser)).thenReturn(loserStats);

        // Act
        statisticsService.updateStatistics(board);

        // Assert
        // Verify winner statistics updated
        verify(statisticsRepository, times(1)).save(argThat(statistics -> 
            statistics.getUser().equals(winner) &&
            statistics.getMatchesPlayed() == 6 &&
            statistics.getMatchesWon() == 4 &&
            statistics.getMatchesLost() == 2 &&
            statistics.getPoints() == 65
        ));

        // Verify loser statistics updated
        verify(statisticsRepository, times(1)).save(argThat(statistics -> 
            statistics.getUser().equals(loser) &&
            statistics.getMatchesPlayed() == 6 &&
            statistics.getMatchesWon() == 2 &&
            statistics.getMatchesLost() == 4 &&
            statistics.getPoints() == 35
        ));
    }
}
