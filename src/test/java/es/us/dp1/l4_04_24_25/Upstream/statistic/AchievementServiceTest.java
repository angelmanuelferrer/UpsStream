package es.us.dp1.l4_04_24_25.Upstream.statistic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.us.dp1.l4_04_24_25.Upstream.statistics.Statistics;
import es.us.dp1.l4_04_24_25.Upstream.statistics.StatisticsRepository;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserService userService;

    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
    private AchievementService achievementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAchievements() {
        // Arrange
        Achievement achievement1 = new Achievement();
        achievement1.setName("Achievement 1");
        Achievement achievement2 = new Achievement();
        achievement2.setName("Achievement 2");

        when(achievementRepository.findAll()).thenReturn(List.of(achievement1, achievement2));

        // Act
        List<Achievement> result = achievementService.getAchievements();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Achievement 1", result.get(0).getName());
        assertEquals("Achievement 2", result.get(1).getName());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        // Arrange
        Achievement achievement = new Achievement();
        achievement.setName("Achievement Found");

        when(achievementRepository.findById(1)).thenReturn(Optional.of(achievement));

        // Act
        Achievement result = achievementService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Achievement Found", result.getName());
        verify(achievementRepository, times(1)).findById(1);
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        when(achievementRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Achievement result = achievementService.getById(1);

        // Assert
        assertNull(result);
        verify(achievementRepository, times(1)).findById(1);
    }

    @Test
    void testSaveAchievement() {
        // Arrange
        Achievement achievement = new Achievement();
        achievement.setName("New Achievement");

        when(achievementRepository.save(achievement)).thenReturn(achievement);

        // Act
        Achievement result = achievementService.saveAchievement(achievement);

        // Assert
        assertNotNull(result);
        assertEquals("New Achievement", result.getName());
        verify(achievementRepository, times(1)).save(achievement);
    }

    @Test
    void testDeleteAchievementById() {
        // Arrange
        int id = 1;

        // Act
        achievementService.deleteAchievementById(id);

        // Assert
        verify(achievementRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAchievementByName_Found() {
        // Arrange
        Achievement achievement = new Achievement();
        achievement.setName("Achievement Name");

        when(achievementRepository.findByName("Achievement Name")).thenReturn(achievement);

        // Act
        Achievement result = achievementService.getAchievementByName("Achievement Name");

        // Assert
        assertNotNull(result);
        assertEquals("Achievement Name", result.getName());
        verify(achievementRepository, times(1)).findByName("Achievement Name");
    }

    @Test
    void testGetAchievementByName_NotFound() {
        // Arrange
        when(achievementRepository.findByName("Unknown Name")).thenReturn(null);

        // Act
        Achievement result = achievementService.getAchievementByName("Unknown Name");

        // Assert
        assertNull(result);
        verify(achievementRepository, times(1)).findByName("Unknown Name");
    }

    @Test
    public void testAchievementStatusGamesPlayed() {
        Achievement achievement = mock(Achievement.class);
        Statistics statistics = mock(Statistics.class);

        when(achievement.getName()).thenReturn("Test Achievement");
        when(achievement.getMetric()).thenReturn(Metric.GAMES_PLAYED);
        when(achievement.getId()).thenReturn(1);
        when(achievement.getActualDescription()).thenReturn("Test Description");
        when(achievement.getBadgeImage()).thenReturn("badge.png");
        when(achievement.getThreshold()).thenReturn(10);
        when(statistics.getMatchesPlayed()).thenReturn(5);

        AchievementStatus achievementStatus = new AchievementStatus(achievement, statistics);

        assertEquals("Test Achievement", achievementStatus.getName());
        assertEquals(Metric.GAMES_PLAYED, achievementStatus.getMetric());
        assertEquals(1, achievementStatus.getId());
        assertEquals("Test Description", achievementStatus.getDescription());
        assertEquals("badge.png", achievementStatus.getBadgeImage());
        assertEquals(10, achievementStatus.getThreshold());
        assertEquals(5, achievementStatus.getActualValue());
    }

    @Test
    public void testAchievementStatusVictories() {
        Achievement achievement = mock(Achievement.class);
        Statistics statistics = mock(Statistics.class);

        when(achievement.getName()).thenReturn("Test Achievement");
        when(achievement.getMetric()).thenReturn(Metric.VICTORIES);
        when(achievement.getId()).thenReturn(2);
        when(achievement.getActualDescription()).thenReturn("Test Description");
        when(achievement.getBadgeImage()).thenReturn("badge.png");
        when(achievement.getThreshold()).thenReturn(20);
        when(statistics.getMatchesWon()).thenReturn(15);

        AchievementStatus achievementStatus = new AchievementStatus(achievement, statistics);

        assertEquals("Test Achievement", achievementStatus.getName());
        assertEquals(Metric.VICTORIES, achievementStatus.getMetric());
        assertEquals(2, achievementStatus.getId());
        assertEquals("Test Description", achievementStatus.getDescription());
        assertEquals("badge.png", achievementStatus.getBadgeImage());
        assertEquals(20, achievementStatus.getThreshold());
        assertEquals(15, achievementStatus.getActualValue());
    }
    @Test
    void testGetActualDescription() {
        // Configuración inicial
        Achievement instance = new Achievement();
        
        // Establecer el valor de description y threshold
        instance.setDescription("The value must not exceed <THRESHOLD>.");
        instance.setThreshold(100);

        // Resultado esperado
        String expected = "The value must not exceed 100.";

        // Llamar a la función y verificar el resultado
        String actual = instance.getActualDescription();
        assertEquals(expected, actual, "The description was not formatted correctly.");
    }
    @Test
    void testGetActualDescriptionWithoutThresholdPlaceholder() {
        // Crear instancia de Achievement
        Achievement achievement = new Achievement();

        // Configurar propiedades de la instancia
        achievement.setDescription("This is a static description.");
        achievement.setThreshold(50);

        // Resultado esperado
        String expected = "This is a static description.";

        // Llamar al método y verificar el resultado
        String actual = achievement.getActualDescription();
        assertEquals(expected, actual, "The description should remain unchanged.");
    }

    @Test
    void testAchivementsProgress_WithAchievements() {
        // Arrange
        Achievement achievement1 = new Achievement();
        achievement1.setName("Achievement 1");
        achievement1.setDescription("");
        achievement1.setMetric(Metric.GAMES_PLAYED);
        Achievement achievement2 = new Achievement();
        achievement2.setName("Achievement 2");
        achievement2.setDescription("");
        achievement2.setMetric(Metric.VICTORIES);

        User currentUser = new User();
        Statistics statistics = new Statistics();
        statistics.setUser(currentUser);

        when(achievementRepository.findAll()).thenReturn(List.of(achievement1, achievement2));
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(statisticsRepository.findByUser(currentUser)).thenReturn(statistics);

        // Act
        List<AchievementStatus> result = achievementService.achivementsProgress();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(achievementRepository, times(1)).findAll();
        verify(userService, times(2)).findCurrentUser();
    }

    @Test
    void testAchivementsProgress_NoAchievements() {
        // Arrange
        User currentUser = new User();
        Statistics statistics = new Statistics();
        statistics.setUser(currentUser);

        when(achievementRepository.findAll()).thenReturn(List.of());
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(statisticsRepository.findByUser(currentUser)).thenReturn(statistics);

        // Act
        List<AchievementStatus> result = achievementService.achivementsProgress();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(achievementRepository, times(1)).findAll();
        verify(userService, times(2)).findCurrentUser();
    }

    @Test
    void testAchivementsProgress_NoStatistics() {
        // Arrange
        Achievement achievement1 = new Achievement();
        achievement1.setName("Achievement 1");
        achievement1.setDescription("");
        achievement1.setMetric(Metric.GAMES_PLAYED);
        Achievement achievement2 = new Achievement();
        achievement2.setName("Achievement 2");
        achievement2.setDescription("");
        achievement2.setMetric(Metric.VICTORIES);

        User currentUser = new User();

        when(achievementRepository.findAll()).thenReturn(List.of(achievement1, achievement2));
        when(userService.findCurrentUser()).thenReturn(currentUser);
        when(statisticsRepository.findByUser(currentUser)).thenReturn(null);

        // Act
        List<AchievementStatus> result = achievementService.achivementsProgress();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(achievementRepository, times(1)).findAll();
        verify(userService, times(1)).findCurrentUser();
        verify(statisticsRepository, times(1)).findByUser(currentUser);
    }

}