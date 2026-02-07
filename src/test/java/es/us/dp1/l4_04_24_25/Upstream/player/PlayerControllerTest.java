package es.us.dp1.l4_04_24_25.Upstream.player;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    private MockMvc mockMvc;

    private Player player;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
        player = new Player();
        player.setId(1);
        player.setMovement(3);
    }

    @Test
    public void testGetAllPlayers() throws Exception {
        List<Player> players = Arrays.asList(player, new Player());
        when(playerService.getAllPlayers()).thenReturn(players);

        mockMvc.perform(get("/api/v1/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetPlayerById() throws Exception {
        when(playerService.getPlayerById(player.getId())).thenReturn(player);

        mockMvc.perform(get("/api/v1/players/{id}", player.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(player.getId()));
    }

    @Test
    public void testGetPlayerById_NotFound() throws Exception {
        when(playerService.getPlayerById(5)).thenThrow(new ResourceNotFoundException("player", "id", 5));

        mockMvc.perform(get("/api/v1/players/{id}", 5))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetMovementByPlayerId() throws Exception {
        when(playerService.getMovementByPlayerId(player.getId())).thenReturn(player.getMovement());

        mockMvc.perform(get("/api/v1/players/{id}/movement", player.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(player.getMovement()));
    }

    @Test
    public void testGetMovementByPlayerId_NotFound() throws Exception {
        when(playerService.getMovementByPlayerId(5)).thenThrow(new ResourceNotFoundException("player", "id", 5));

        mockMvc.perform(get("/api/v1/players/{id}/movement", 5))
                .andExpect(status().isNotFound());
    }
}