package es.us.dp1.l4_04_24_25.Upstream.statistic;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.BadRequestException;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementRestController achievementController;

    @Test
    public void testFindAll() {
        List<Achievement> achievements = List.of(new Achievement(), new Achievement());
        when(achievementService.getAchievements()).thenReturn(achievements);

        ResponseEntity<List<Achievement>> response = achievementController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(achievements, response.getBody());
    }

    @Test
    public void testFindAchievement() {
        Achievement achievement = new Achievement();
        when(achievementService.getById(1)).thenReturn(achievement);

        ResponseEntity<Achievement> response = achievementController.findAchievement(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(achievement, response.getBody());
    }

    @Test
    public void testFindAchievementNotFound() {
        when(achievementService.getById(1)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            achievementController.findAchievement(1);
        });
    }

    @Test
    public void testCreateAchievement() {
        Achievement newAchievement = new Achievement();
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        when(achievementService.saveAchievement(newAchievement)).thenReturn(newAchievement);

        ResponseEntity<Achievement> response = achievementController.createAchievement(newAchievement, br);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newAchievement, response.getBody());
    }

    @Test
    public void testCreateAchievementBadRequest() {
        Achievement newAchievement = new Achievement();
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            achievementController.createAchievement(newAchievement, br);
        });
    }

    @Test
    public void testModifyAchievement() {
        Achievement existingAchievement = new Achievement();
        existingAchievement.setId(1);
        Achievement newAchievement = new Achievement();
        newAchievement.setId(1);
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        when(achievementService.getById(1)).thenReturn(existingAchievement);

        ResponseEntity<Void> response = achievementController.modifyAchievement(newAchievement, br, 1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(achievementService).saveAchievement(existingAchievement);
    }

    @Test
    public void testDeleteAchievement() {
        Achievement achievement = new Achievement();
        when(achievementService.getById(1)).thenReturn(achievement);

        ResponseEntity<Void> response = achievementController.deleteAchievement(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(achievementService).deleteAchievementById(1);
    }
}
