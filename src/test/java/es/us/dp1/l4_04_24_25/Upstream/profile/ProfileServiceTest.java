package es.us.dp1.l4_04_24_25.Upstream.profile;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.us.dp1.l4_04_24_25.Upstream.match.Match;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;





class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Profile profile = new Profile();
        when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.save(profile);

        assertEquals(profile, result);
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    void testFindStatisticsWithMatches() {
        String username = "testUser";
        
        Match match1 = new Match();
        match1.setStart(LocalDateTime.of(2023, 10, 1, 10, 0));
        match1.setFinish(LocalDateTime.of(2023, 10, 1, 10, 30));

        Match match2 = new Match();
        match2.setStart(LocalDateTime.of(2023, 10, 2, 11, 0));
        match2.setFinish(LocalDateTime.of(2023, 10, 2, 11, 45));

        List<Match> matches = Arrays.asList(match1, match2);
        when(profileRepository.partidasJugadas(username)).thenReturn(matches);

        TimeStatistics statistics = profileService.findStatistics(username);

        assertEquals(37.5*60, statistics.getAvg());
        assertEquals(75*60, statistics.getSum());
        verify(profileRepository, times(1)).partidasJugadas(username);
    }

    @Test
    void testFindStatisticsWithNoMatches() {
        String username = "testUser";
        when(profileRepository.partidasJugadas(username)).thenReturn(Collections.emptyList());

        TimeStatistics statistics = profileService.findStatistics(username);

        assertEquals(0.0, statistics.getAvg());
        assertEquals(0, statistics.getSum());
        verify(profileRepository, times(1)).partidasJugadas(username);
    }
}