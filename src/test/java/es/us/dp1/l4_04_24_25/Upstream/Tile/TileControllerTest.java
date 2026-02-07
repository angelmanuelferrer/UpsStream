package es.us.dp1.l4_04_24_25.Upstream.tile;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.containsString;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.tileStack.TileStack;
import es.us.dp1.l4_04_24_25.Upstream.tileStack.TileStackService;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

class TileControllerTest {

    @Mock
    private TileService tileService;

    @Mock
    private BoardService boardService;

    @Mock
    private TileStackService tileStackService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TileController tileController;

    @Mock
    private Tile tile;

    @Mock
    private Board board;

    @Mock
    private Player player;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tileController).build();
    }

    @Test
    void testGetAllTiles() throws Exception {
        // Simulamos una lista de losetas
        List<Tile> tiles = Arrays.asList(new Tile(), new Tile());
        when(tileService.getAllTile()).thenReturn(tiles);

        mockMvc.perform(get("/api/v1/tile"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));  // Aseguramos que se devuelven 2 losetas
    }

    @Test
    void testGetTileById_NotFound() throws Exception {
        Integer id = 1;
        when(tileService.getTileById(id)).thenReturn(null);

        mockMvc.perform(get("/api/v1/tile/{id}", id))
            .andExpect(status().isNotFound());  // Esperamos un error 404 si no se encuentra la loseta
    }

    @Test
    void testCreateTile_BoardNotFound() throws Exception {
        Integer boardId = 999;
        Tile tileRequest = new Tile();

        when(boardService.getBoardById(boardId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/tile/{id}", boardId)
                .contentType("application/json")
                .content("{}"))
            .andExpect(status().isNotFound());  // El tablero no se encuentra, esperamos un 404
    }

    @Test
    void testDeleteTile() throws Exception {
        Integer tileId = 1;

        doNothing().when(tileService).delete(tileId);

        mockMvc.perform(delete("/api/v1/tile/{id}", tileId))
            .andExpect(status().isNoContent());  // Comprobamos que el status es No Content (204)
    }

    @Test
    void testGetTilesByBoardId() throws Exception {
        Integer boardId = 1;
        List<Tile> tiles = Arrays.asList(new Tile(), new Tile());
        when(tileService.getTilesByBoardId(boardId)).thenReturn(tiles);

        mockMvc.perform(get("/api/v1/tile/board/{id}", boardId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));  // Comprobamos que se devuelven 2 losetas
    }

    @Test
    void testCreateTile_Success() throws Exception {
        Integer boardId = 1;
        Tile tileRequest = new Tile();
        tileRequest.setX(1);
        tileRequest.setY(1);
        tileRequest.setTileStack(new TileStack());
        Tile createdTile = new Tile();
        createdTile.setId(1);

        Board board = new Board();
        board.setId(boardId);
        board.setTileCount(12);
        board.setSalmonMoved(true);
        board.setTurno(Colour.RED);
        board.setTurnoTile(Colour.RED);
        board.setRoundPass(true);

        Match match = new Match();
        match.setNumberOfPlayers(4);
        Player player = new Player();
        player.setColour(Colour.RED);
        User user = new User();
        user.setId(1);
        player.setUser(user);
        match.setPlayers(Arrays.asList(player));
        board.setMatch(match);

        when(boardService.getBoardById(boardId)).thenReturn(Optional.of(board));
        doNothing().when(boardService).validateTilePlacement(boardId, tileRequest.getX(), tileRequest.getY());
        when(userService.findCurrentUser()).thenReturn(user);
        when(tileService.createTile(board, tileRequest)).thenReturn(createdTile);
        doNothing().when(tileStackService).actualizeTile(boardId);

        mockMvc.perform(post("/api/v1/tile/{id}", boardId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tileRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/v1/tile/1")));
    }


}