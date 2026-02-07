package es.us.dp1.l4_04_24_25.Upstream.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
        player.setId(1);
        player.setMovement(3);
    }

    @Test
    public void testGetAllPlayers() {
        List<Player> players = Arrays.asList(player, new Player());
        when(playerRepository.findAll()).thenReturn(players);

        List<Player> result = playerService.getAllPlayers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    public void testSavePlayer() {
        when(playerRepository.save(player)).thenReturn(player);

        Player result = playerService.save(player);

        assertNotNull(result);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    public void testGetPlayerById_Success() {
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerById(player.getId());

        assertNotNull(result);
        assertEquals(player.getId(), result.getId());
        verify(playerRepository, times(1)).findById(player.getId());
    }

    @Test
    public void testGetPlayerById_NotFound() {
        when(playerRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getPlayerById(5));
        verify(playerRepository, times(1)).findById(5);
    }

    @Test
    public void testDeletePlayer() {
        playerService.delete(player.getId());

        verify(playerRepository, times(1)).deleteById(player.getId());
    }

    @Test
    public void testGetMovementByPlayerId_Success() {
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

        int movement = playerService.getMovementByPlayerId(player.getId());

        assertEquals(3, movement);
        verify(playerRepository, times(1)).findById(player.getId());
    }

    @Test
    public void testGetMovementByPlayerId_NotFound() {
        when(playerRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getMovementByPlayerId(5));
        verify(playerRepository, times(1)).findById(5);
    }

    @Test
    public void testDeletePlayer_NotFound() {
        doThrow(new ResourceNotFoundException("player", "id", 5)).when(playerRepository).deleteById(5);

        assertThrows(ResourceNotFoundException.class, () -> playerService.delete(5));
        verify(playerRepository, times(1)).deleteById(5);
    }
}