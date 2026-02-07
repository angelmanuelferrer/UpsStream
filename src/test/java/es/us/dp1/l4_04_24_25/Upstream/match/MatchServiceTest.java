package es.us.dp1.l4_04_24_25.Upstream.match;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardRepository;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.player.PlayerRepository;
import es.us.dp1.l4_04_24_25.Upstream.user.User;

class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private MatchService matchService;

    private Match match;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        match = new Match();
        match.setPlayers(new ArrayList<>());
    }

    @Test
    void testGetAllMatches() {
        Match match1 = new Match();
        Match match2 = new Match();
        when(matchRepository.findAll()).thenReturn(Arrays.asList(match1, match2));

        List<Match> matches = matchService.getAllMatches();

        assertEquals(2, matches.size());
        verify(matchRepository, times(1)).findAll();
    }

    @Test
    void testGetMatchById() {
        Match match = new Match();
        when(matchRepository.findById(1)).thenReturn(Optional.of(match));

        Optional<Match> foundMatch = matchService.getMatchById(1);

        assertTrue(foundMatch.isPresent());
        assertEquals(match, foundMatch.get());
        verify(matchRepository, times(1)).findById(1);
    }

    @Test
    void testGetMatchByName() {
        Match match = new Match();
        when(matchRepository.findByName("Test Match")).thenReturn(match);

        Match foundMatch = matchService.getMatchByName("Test Match");

        assertEquals(match, foundMatch);
        verify(matchRepository, times(1)).findByName("Test Match");
    }

    @Test
    void testSaveMatch() {
        Match match = new Match();
        when(matchRepository.save(match)).thenReturn(match);

        Match savedMatch = matchService.save(match);

        assertEquals(match, savedMatch);
        verify(matchRepository, times(1)).save(match);
    }

    @Test
    void testDeleteMatch() {
        doNothing().when(matchRepository).deleteById(1);

        matchService.delete(1);

        verify(matchRepository, times(1)).deleteById(1);
    }

    @Test
    void testAddPlayer() {
        // Arrange
        Player player = new Player();

        // Act
        match.addPlayer(player);

        // Assert
        assertNotNull(match.getPlayers());
        assertEquals(1, match.getPlayers().size());
        assertTrue(match.getPlayers().contains(player));
    }

    @Test
    void testAddPlayerToMatch_ThrowsExceptionWhenMatchFull() {
        Match match = new Match();
        match.setNumberOfPlayers(1);
        match.setPlayers(Arrays.asList(new Player()));
        when(matchRepository.findById(1)).thenReturn(Optional.of(match));

        User user = new User();
        assertThrows(IllegalArgumentException.class, () -> matchService.addPlayerToMatch(1, user, 0));
    }

    @Test
    void testFindPlayersByMatchId() {
        Match match = new Match();
        Player player1 = new Player();
        Player player2 = new Player();
        match.setPlayers(Arrays.asList(player1, player2));
        when(matchRepository.findById(1)).thenReturn(Optional.of(match));

        List<Player> players = matchService.findPlayersByMatchId(1);

        assertEquals(2, players.size());
        verify(matchRepository, times(1)).findById(1);
    }

    @Test
    void testGetMatchStatus() {
        Board board = new Board();
        when(boardRepository.findBoardByMatchId(1)).thenReturn(Optional.of(board));

        Boolean status = matchService.getMatchStatus(1);

        assertTrue(status);
        verify(boardRepository, times(1)).findBoardByMatchId(1);
    }

    @Test
    void testGetMatchStatus_NoBoard() {
        when(boardRepository.findBoardByMatchId(1)).thenReturn(Optional.empty());

        Boolean status = matchService.getMatchStatus(1);

        assertFalse(status);
        verify(boardRepository, times(1)).findBoardByMatchId(1);
    }
}