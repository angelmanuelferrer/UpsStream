package es.us.dp1.l4_04_24_25.Upstream.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchService;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonRepository;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonState;
import es.us.dp1.l4_04_24_25.Upstream.statistics.StatisticsService;
import es.us.dp1.l4_04_24_25.Upstream.tile.Tile;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileType;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private SalmonRepository salmonRepository;

    @Mock
    private MatchService matchService;

    @Mock
    private TileService tileService;

    @Mock
    private StatisticsService statisticsService;

    @Mock
    private UserService userService;
    
    @InjectMocks
    private BoardService boardService;

    private Board board;
    private Match match;
    private Player player;
    private Salmon salmon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        board = new Board();
        board.setId(1);
        board.setTileCount(9);

        match = new Match();
        match.setId(1);
        match.setPlayers(new ArrayList<>());
        board.setMatch(match);

        player = new Player();
        player.setColour(Colour.RED);
        player.setPoints(10);
        match.getPlayers().add(player);

        salmon = new Salmon();
        salmon.setPlayer(player);
        salmon.setX(1);
        salmon.setY(2);
        salmon.setPair(true);
    }

    @Test
    void getAllBoards() {
        List<Board> boards = new ArrayList<>();
        boards.add(board);

        when(boardRepository.findAll()).thenReturn(boards);

        List<Board> result = boardService.getAllBoards();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(boardRepository, times(1)).findAll();
    }

    @Test
    void getBoardById_WhenExists() {
        when(boardRepository.findById(1)).thenReturn(Optional.of(board));

        Optional<Board> result = boardService.getBoardById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(boardRepository, times(1)).findById(1);
    }

    @Test
    void getBoardById_WhenNotExists() {
        when(boardRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Board> result = boardService.getBoardById(1);

        assertFalse(result.isPresent());
        verify(boardRepository, times(1)).findById(1);
    }

    @Test
    void saveBoard() {
        when(boardRepository.save(board)).thenReturn(board);

        Board result = boardService.save(board);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    void deleteBoard() {
        doNothing().when(boardRepository).deleteById(1);

        boardService.delete(1);

        verify(boardRepository, times(1)).deleteById(1);
    }

    @Test
    void updateTurnoTile_ShouldUpdateTurnoTile() {
        board.setTurnoTile(Colour.RED);
        when(boardRepository.save(board)).thenReturn(board);

        BoardTurnos result = boardService.updateTurnoTile(board);

        assertNotNull(result);
        assertEquals(Colour.GREEN, result.getTurnoTile());
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    void getBoardByMatchId_ShouldReturnBoard_WhenExists() {
        when(boardRepository.findBoardByMatchId(1)).thenReturn(Optional.of(board));

        Optional<Board> result = boardService.getBoardByMatchId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(boardRepository, times(1)).findBoardByMatchId(1);
    }

    @Test
    void getBoardByMatchId_ShouldReturnEmpty_WhenNotExists() {
        when(boardRepository.findBoardByMatchId(1)).thenReturn(Optional.empty());

        Optional<Board> result = boardService.getBoardByMatchId(1);

        assertFalse(result.isPresent());
        verify(boardRepository, times(1)).findBoardByMatchId(1);
    }

    @Test
    void initializeSalmones_ShouldReturnExistingSalmons_WhenSalmonsExist() {
        List<Salmon> existingSalmons = new ArrayList<>();
        existingSalmons.add(salmon);

        when(boardRepository.findSalmonsByBoardId(1)).thenReturn(existingSalmons);

        List<Salmon> result = boardService.initializeSalmones(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(boardRepository, times(1)).findSalmonsByBoardId(1);
    }

    @Test
    void initializeSalmones_ShouldThrowException_WhenBoardNotFound() {
        when(boardRepository.findSalmonsByBoardId(1)).thenReturn(new ArrayList<>());
        when(boardRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> boardService.initializeSalmones(1));

        assertEquals("Tablero no encontrado", exception.getMessage());
        verify(boardRepository, times(1)).findById(1);
    }

    @Test
    void initializeSalmones_ShouldThrowException_WhenMatchNotAssociated() {
        when(boardRepository.findSalmonsByBoardId(1)).thenReturn(new ArrayList<>());
        when(boardRepository.findById(1)).thenReturn(Optional.of(board));

        board.setMatch(null);

        Exception exception = assertThrows(IllegalStateException.class, () -> boardService.initializeSalmones(1));

        assertEquals("El tablero no tiene una partida asociada.", exception.getMessage());
        verify(boardRepository, times(1)).findById(1);
    }

    @Test
    void initializeSalmones_ShouldThrowException_WhenNoPlayersInMatch() {
        when(boardRepository.findSalmonsByBoardId(1)).thenReturn(new ArrayList<>());
        when(boardRepository.findById(1)).thenReturn(Optional.of(board));
        when(matchService.findPlayersByMatchId(1)).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(IllegalStateException.class, () -> boardService.initializeSalmones(1));

        assertEquals("No hay jugadores en la partida.", exception.getMessage());
        verify(boardRepository, times(1)).findById(1);
        verify(matchService, times(1)).findPlayersByMatchId(1);
    }

    @Test
    void initializeSalmones_ShouldCreateAndReturnSalmons_WhenNoExistingSalmons() {
        when(boardRepository.findSalmonsByBoardId(1)).thenReturn(new ArrayList<>());
        when(boardRepository.findById(1)).thenReturn(Optional.of(board));
        when(matchService.findPlayersByMatchId(1)).thenReturn(List.of(player));
        when(salmonRepository.save(any(Salmon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Salmon> result = boardService.initializeSalmones(1);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(boardRepository, times(1)).findSalmonsByBoardId(1);
        verify(boardRepository, times(1)).findById(1);
        verify(matchService, times(1)).findPlayersByMatchId(1);
        verify(salmonRepository, times(result.size())).save(any(Salmon.class));
    }

    @Test
    void createSalmon_ShouldCreateSalmonsForAllPlayers() {
        List<Player> players = List.of(player);
        when(salmonRepository.save(any(Salmon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Salmon> result = boardService.createSalmon(board, players);

        assertNotNull(result);
        assertEquals(4, result.size());
        verify(salmonRepository, times(4)).save(any(Salmon.class));
    }

    @Test
    void createSalmon_ShouldSetCorrectAttributesForSalmons() {
        List<Player> players = List.of(player);
        when(salmonRepository.save(any(Salmon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Salmon> result = boardService.createSalmon(board, players);

        for (Salmon salmon : result) {
            assertEquals(player, salmon.getPlayer());
            assertTrue(salmon.isPair());
            assertEquals(SalmonState.LIVE, salmon.getState());
            assertEquals(board, salmon.getBoard());
            assertEquals("/ficharoja.png", salmon.getImage());
        }
    }

    @Test
    void createSalmon_ShouldCreateSalmonsForMultiplePlayers() {
        Player player2 = new Player();
        player2.setColour(Colour.GREEN);
        List<Player> players = List.of(player, player2);
        when(salmonRepository.save(any(Salmon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Salmon> result = boardService.createSalmon(board, players);

        assertNotNull(result);
        assertEquals(8, result.size()); // 4 initial positions * 2 players
        verify(salmonRepository, times(8)).save(any(Salmon.class));
    }

    @Test
    void createSalmon_ShouldSetCorrectImagesBasedOnPlayerColour() {
        Player player2 = new Player();
        player2.setColour(Colour.GREEN);
        List<Player> players = List.of(player, player2);
        when(salmonRepository.save(any(Salmon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Salmon> result = boardService.createSalmon(board, players);

        for (Salmon salmon : result) {
            if (salmon.getPlayer().getColour() == Colour.RED) {
                assertEquals("/ficharoja.png", salmon.getImage());
            } else if (salmon.getPlayer().getColour() == Colour.GREEN) {
                assertEquals("/fichaverde.png", salmon.getImage());
            } else if (salmon.getPlayer().getColour() == Colour.PURPLE) {
                assertEquals("/fichamorada.png", salmon.getImage());
            } else if (salmon.getPlayer().getColour() == Colour.YELLOW) {
                assertEquals("/fichaamarilla.png", salmon.getImage());
            } else {
                assertEquals("/fichaorange.png", salmon.getImage());
            }
        }
    }

    @Test
    void heronRule_ShouldInvokeHeronRule1_WhenSalmonFound() {
        board.setTurno(Colour.RED);

        Tile tile = new Tile();
        tile.setX(1);
        tile.setY(2);
        tile.setBoard(board);
        tile.setType(TileType.HERON);

        player.setMovement(5);
        salmon.setPlayer(player);
        salmon.setX(1);
        salmon.setY(2);
        List<Salmon> salmons = List.of(salmon);

        when(salmonRepository.findSalmonByBoard(board)).thenReturn(salmons);
        when(tileService.getTileByCoordenadas(1, 2, board.getId())).thenReturn(tile);

        boardService.heronRule(board);

        verify(salmonRepository, times(1)).findSalmonByBoard(board);
        verify(tileService, times(1)).getTileByCoordenadas(1, 2, board.getId());
    }

    @Test
    void heronRule_ShouldNotInvokeHeronRule1_WhenTileNotHeron() {
        board.setTurno(Colour.RED);
        player.setMovement(5);
        salmon.setPlayer(player);
        salmon.setX(1);
        salmon.setY(2);

        Tile tile = new Tile();
        tile.setX(1);
        tile.setY(2);
        tile.setBoard(board);
        tile.setType(TileType.WATER);

        List<Salmon> salmons = List.of(salmon);

        when(salmonRepository.findSalmonByBoard(board)).thenReturn(salmons);
        when(tileService.getTileByCoordenadas(1, 2, board.getId())).thenReturn(tile);

        boardService.heronRule(board);

        verify(salmonRepository, times(1)).findSalmonByBoard(board);
        verify(tileService, times(1)).getTileByCoordenadas(1, 2, board.getId());
    }

    @Test
    void heronRule1_ShouldSetPairToFalseAndUpdateImage_WhenSalmonIsPair() {
        salmon.setPair(true);
        salmon.setPlayer(player);
        player.setColour(Colour.RED);

        boardService.heronRule1(salmon);

        assertFalse(salmon.isPair());
        assertEquals("/ficharoja_unico.png", salmon.getImage());
        verify(salmonRepository, times(1)).save(salmon);
    }


    @Test
    void heronRule1_ShouldIncreaseTileCapacityAndDeleteSalmon_WhenSalmonIsNotPair() {
        salmon.setPair(false);
        salmon.setBoard(board);
        salmon.setPlayer(player);
        
        Tile tile = new Tile();
        tile.setX(1);
        tile.setY(2);
        tile.setBoard(board);
        tile.setCapacity(1);

        when(tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId())).thenReturn(tile);

        boardService.heronRule1(salmon);

        assertEquals(2, tile.getCapacity());
        verify(tileService, times(1)).save(tile);
        verify(salmonRepository, times(1)).delete(salmon);
    }

    @Test
    void getFriendsBoards() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        
        List<Board> mockBoards = new ArrayList<>();
        Board board1 = new Board();
        board1.setId(101);
        

        Board board2 = new Board();
        board2.setId(102);
        

        mockBoards.add(board1);
        mockBoards.add(board2);

        // Mocking dependencies
        Mockito.when(userService.findCurrentUser()).thenReturn(currentUser);
        Mockito.when(boardRepository.findBoardsByUserFriends(currentUser)).thenReturn(mockBoards);

        // Act
        List<Board> result = boardService.getFriendsBoards();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
      

        Mockito.verify(userService).findCurrentUser();
        Mockito.verify(boardRepository).findBoardsByUserFriends(currentUser);
    }

    @Test
    void validateTilePlacement_WhenBoardDoesNotExist() {
        // Arrange
        Integer boardId = 1;
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> boardService.validateTilePlacement(boardId, 3, 1)
        );

        Mockito.verify(boardRepository).findById(boardId);
    }

    @Test
    void validateTilePlacement_WhenXDoesNotMatchSetIndex() {
        // Arrange
        Integer boardId = 1;
        Board board = new Board();
        board.setId(boardId);
        board.setTileCount(6);

        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // Act & Assert
        IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> boardService.validateTilePlacement(boardId, 2, 1) // x != setIndex
        );

        Assertions.assertEquals("Quedan posiciones por rellenar con losetas", exception.getMessage());
        Mockito.verify(boardRepository).findById(boardId);
    }

    @Test
    void validateTilePlacement() {
        // Arrange
        Integer boardId = 1;
        Board board = new Board();
        board.setId(boardId);
        board.setTileCount(6);

        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> boardService.validateTilePlacement(boardId, 4, 1)); // x == setIndex
        Mockito.verify(boardRepository).findById(boardId);
    }
}