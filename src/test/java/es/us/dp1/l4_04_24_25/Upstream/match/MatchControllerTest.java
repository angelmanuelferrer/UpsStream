package es.us.dp1.l4_04_24_25.Upstream.match;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.chat.ChatService;
import es.us.dp1.l4_04_24_25.Upstream.chat.Message;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.player.PlayerService;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonService;
import es.us.dp1.l4_04_24_25.Upstream.tile.Tile;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.tileStack.TileStack;
import es.us.dp1.l4_04_24_25.Upstream.tileStack.TileStackService;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.friends.FriendService;
import es.us.dp1.l4_04_24_25.Upstream.friends.MatchInvitation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class MatchControllerTest {

    @Mock
    private MatchService matchService;

    @Mock
    private UserService userService;

    @Mock
    private BoardService boardService;

    @Mock
    private PlayerService playerService;

    @Mock
    private TileService tileService;

    @Mock
    private TileStackService tileStackService;

    @Mock
    private SalmonService salmonService;

    @Mock
    private ChatService chatService;

    @Mock
    private FriendService friendService;

    @InjectMocks
    private MatchController matchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMatches() {
        // Arrange
        Match match1 = new Match();
        Match match2 = new Match();
        when(matchService.getAllMatches()).thenReturn(Arrays.asList(match1, match2));

        // Act
        List<Match> result = matchController.getAllMatches(null, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(matchService, times(1)).getAllMatches();
    }

    @Test
    void testGetMatchById_Success() {
        // Arrange
        Match match = new Match();
        when(matchService.getMatchById(1)).thenReturn(Optional.of(match));

        // Act
        Match result = matchController.getMatchById(1);

        // Assert
        assertNotNull(result);
        verify(matchService, times(1)).getMatchById(1);
    }

    @Test
    void testGetMatchById_NotFound() {
        // Arrange
        when(matchService.getMatchById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> matchController.getMatchById(1));
        verify(matchService, times(1)).getMatchById(1);
    }

    @Test
    void testUpdateMatch() {
        // Arrange
        Match existingMatch = new Match();
        Match updatedMatch = new Match();
        when(matchService.getMatchById(1)).thenReturn(Optional.of(existingMatch));

        // Act
        ResponseEntity<Void> response = matchController.updateMatch(updatedMatch, 1);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(matchService, times(1)).save(existingMatch);
    }



    @Test
    void testGetPlayersByMatchId() {
        // Arrange
        List<Player> players = Arrays.asList(new Player(), new Player());
        when(matchService.findPlayersByMatchId(1)).thenReturn(players);

        // Act
        ResponseEntity<List<Player>> response = matchController.getPlayersByMatchId(1);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(matchService, times(1)).findPlayersByMatchId(1);
    }

    @Test
    void testGetMatchStatus() {
        // Arrange
        when(matchService.getMatchStatus(1)).thenReturn(true);

        // Act
        Boolean status = matchController.getMatchStatus(1);

        // Assert
        assertNotNull(status);
        assertTrue(status);
        verify(matchService, times(1)).getMatchStatus(1);
    }

    @Test
    void testUpdateMatchStartDate_Success() {
        // Arrange
        Integer matchId = 1;

        Match existingMatch = new Match();
        Match updatedMatch = new Match();
        updatedMatch.setStart(java.time.LocalDateTime.now());

        when(matchService.getMatchById(matchId)).thenReturn(Optional.of(existingMatch));
        when(matchService.save(existingMatch)).thenReturn(existingMatch);

        // Act
        ResponseEntity<Void> response = matchController.updateMatchStartDate(updatedMatch, matchId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(matchService, times(1)).getMatchById(matchId);
        verify(matchService, times(1)).save(existingMatch);
        assertEquals(updatedMatch.getStart(), existingMatch.getStart(), "La fecha de inicio debería haberse actualizado correctamente.");
    }

    @Test
    void testUpdateMatchStartDate_NotFound() {
        // Arrange
        Integer matchId = 1;
        Match updatedMatch = new Match();

        when(matchService.getMatchById(matchId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> matchController.updateMatchStartDate(updatedMatch, matchId));
        verify(matchService, times(1)).getMatchById(matchId);
        verify(matchService, never()).save(any(Match.class));
    }

    @Test
    void testDeleteMatch_Success() {
        // Arrange
        Integer matchId = 1;

        Match match = new Match();
        match.setId(matchId);

        Player player = new Player();
        player.setId(1);
        match.setPlayers(Arrays.asList(player));

        Board board = new Board();
        board.setId(1);

        Tile tile = new Tile();
        tile.setId(1);
        tile.setBoard(board);

        Salmon salmon = new Salmon();
        salmon.setId(1);
        salmon.setBoard(board);

        TileStack tileStack = new TileStack();
        tileStack.setId(1);
        tileStack.setBoard(board);

        Message message = new Message();
        message.setId(1);
        message.setBoard(board);

        MatchInvitation matchInvitation = new MatchInvitation();
        matchInvitation.setId(1);
        matchInvitation.setMatch(match);

        when(matchService.getMatchById(matchId)).thenReturn(Optional.of(match));
        when(boardService.getBoardByMatchId(matchId)).thenReturn(Optional.of(board));
        when(tileService.getAllTile()).thenReturn(Arrays.asList(tile));
        when(salmonService.getAllSalmons()).thenReturn(Arrays.asList(salmon));
        when(tileStackService.getAllTileStacks()).thenReturn(Arrays.asList(tileStack));
        when(chatService.getMessagesByBoard(board)).thenReturn(Arrays.asList(message));
        when(friendService.findMatchInvitations()).thenReturn(Arrays.asList(matchInvitation));

        // Act
        ResponseEntity<Void> response = matchController.deleteMatch(matchId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());

        // Verifications
        verify(tileService, times(1)).delete(tile.getId());
        verify(salmonService, times(1)).delete(salmon.getId());
        verify(tileStackService, times(1)).delete(tileStack.getId());

        // Capture and verify the ID passed to friendService.delete
        ArgumentCaptor<Integer> matchInvitationCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(friendService, times(1)).delete(matchInvitationCaptor.capture());
        assertEquals(matchInvitation.getId(), matchInvitationCaptor.getValue());

        verify(chatService, times(1)).delete(message.getId());
        verify(boardService, times(1)).delete(board.getId());
        verify(playerService, times(1)).delete(player.getId());
        verify(matchService, times(1)).delete(matchId);
    }

    @Test
    void testDeleteMatch_NotFound() {
        // Arrange
        Integer matchId = 1;
        when(matchService.getMatchById(matchId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> matchController.deleteMatch(matchId));
        verify(matchService, times(1)).getMatchById(matchId);
        verify(boardService, never()).getBoardByMatchId(anyInt());
        verify(tileService, never()).getAllTile();
        verify(salmonService, never()).getAllSalmons();
        verify(chatService, never()).getMessagesByBoard(any(Board.class));
        verify(friendService, never()).findMatchInvitations();
        verify(boardService, never()).delete(anyInt());
        verify(playerService, never()).delete(anyInt());
        verify(matchService, never()).delete(anyInt());
    }

}
