package es.us.dp1.l4_04_24_25.Upstream.board;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.us.dp1.l4_04_24_25.Upstream.chat.ChatService;
import es.us.dp1.l4_04_24_25.Upstream.chat.Message;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchService;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchStatus;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    @MockBean
    private BoardService boardService;

    @MockBean
    private MatchService matchService;

    @MockBean
    private TileService tileService;

    @MockBean
    private ChatService chatService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private BoardController boardController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    public void testGetAllBoards() throws Exception {
        List<Board> boards = Arrays.asList(new Board(), new Board());
        when(boardService.getAllBoards()).thenReturn(boards);

        mockMvc.perform(get("/api/v1/board"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(boards.size()));
    }

    @Test
    public void testGetBoardById() throws Exception {
        Board board = new Board();
        when(boardService.getBoardById(anyInt())).thenReturn(Optional.of(board));

        mockMvc.perform(get("/api/v1/board/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    public void testGetBoardByIdNotFound() throws Exception {
        when(boardService.getBoardById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/board/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateBoard() throws Exception {
        Match match = new Match();
        match.setPlayers(Arrays.asList(new Player(), new Player()));
        match.setStatus(MatchStatus.PLAYING);
        when(matchService.getMatchById(anyInt())).thenReturn(Optional.of(match));
        when(boardService.save(any(Board.class))).thenReturn(new Board());

        mockMvc.perform(post("/api/v1/board/1"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateBoard() throws Exception {
        Board board = new Board();
        when(boardService.getBoardById(anyInt())).thenReturn(Optional.of(board));
        when(boardService.save(any(Board.class))).thenReturn(board);

        mockMvc.perform(put("/api/v1/board/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBoard() throws Exception {
        Board board = new Board();
        when(boardService.getBoardById(anyInt())).thenReturn(Optional.of(board));

        mockMvc.perform(delete("/api/v1/board/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetBoardByMatchId() throws Exception {
        Board board = new Board();
        when(boardService.getBoardByMatchId(anyInt())).thenReturn(Optional.of(board));

        mockMvc.perform(get("/api/v1/board/match/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    public void testInitializeSalmones() throws Exception {
        when(boardService.initializeSalmones(anyInt())).thenReturn(Arrays.asList(new Salmon(), new Salmon()));

        mockMvc.perform(post("/api/v1/board/1/initializeSalmones"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTileCountByBoardId() throws Exception {
        Board board = new Board();
        board.setTileCount(10);
        when(boardService.getBoardById(anyInt())).thenReturn(Optional.of(board));

        mockMvc.perform(get("/api/v1/board/1/tileCount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10));
    }

    @Test
    public void testGetWinner() throws Exception {
        Board board = new Board();
        User user = new User();
        board.setWinner(user);
        when(boardService.getBoardById(anyInt())).thenReturn(Optional.of(board));

        mockMvc.perform(get("/api/v1/board/1/winner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void testSendMessageToChat() throws Exception {
        Board board = new Board();
        Player player = new Player();
        User user = new User();
        user.setId(1);
        player.setUser(user);
        board.setMatch(new Match());
        board.getMatch().setPlayers(Arrays.asList(player));
        when(boardService.getBoardById(anyInt())).thenReturn(Optional.of(board));
        when(userService.findCurrentUser()).thenReturn(user);
        when(chatService.saveMessage(any(Board.class), any(Player.class), any(String.class))).thenReturn(new Message());

        mockMvc.perform(post("/api/v1/board/1/sendChat")
                .param("message", "Hello"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetChatMessages() throws Exception {
        Board board = new Board();
        when(boardService.getBoardById(anyInt())).thenReturn(Optional.of(board));
        when(chatService.getMessagesByBoard(any(Board.class))).thenReturn(Arrays.asList(new Message(), new Message()));

        mockMvc.perform(get("/api/v1/board/1/chat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetFriendsBoards() throws Exception {
        when(boardService.getFriendsBoards()).thenReturn(Arrays.asList(new Board(), new Board()));

        mockMvc.perform(get("/api/v1/board/friendsBoard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}