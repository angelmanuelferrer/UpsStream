package es.us.dp1.l4_04_24_25.Upstream.tileStack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileType;

class TileStackControllerTest {

    @Mock
    private TileStackService tileStackService;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private TileStackController tileStackController;

    private TileStack tileStack;
    private Board board;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        board = new Board();
        board.setId(1);

        tileStack = new TileStack();
        tileStack.setId(1);
        tileStack.setBoard(board);
        tileStack.setImage("/test_image.png");
        tileStack.setType(TileType.WATER);
        tileStack.setUsed(false);
    }

    @Test
    void getAllTileStacks_ShouldReturnListOfTileStacks() {
        List<TileStack> tileStacks = new ArrayList<>();
        tileStacks.add(tileStack);

        when(tileStackService.getAllTileStacks()).thenReturn(tileStacks);

        List<TileStack> result = tileStackController.getAllTileStacks();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tileStackService, times(1)).getAllTileStacks();
    }

    @Test
    void getTileStackById_ShouldReturnTileStack_WhenExists() {
        when(tileStackService.getTileStackById(1)).thenReturn(Optional.of(tileStack));

        TileStack result = tileStackController.getTileStackById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(tileStackService, times(1)).getTileStackById(1);
    }

    @Test
    void getTileStackById_ShouldThrowException_WhenNotExists() {
        when(tileStackService.getTileStackById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tileStackController.getTileStackById(1));
        verify(tileStackService, times(1)).getTileStackById(1);
    }

    @Test
    void createTileStack_ShouldCreateTileStacksForBoard() {
        when(boardService.getBoardById(1)).thenReturn(Optional.of(board));
        mockStatic(ServletUriComponentsBuilder.class);
        ServletUriComponentsBuilder builder = mock(ServletUriComponentsBuilder.class);
        when(ServletUriComponentsBuilder.fromCurrentRequest()).thenReturn(builder);
        when(builder.toUriString()).thenReturn("http://localhost/api/v1/tileStack/1");

        ResponseEntity<TileStack> response = tileStackController.createTileStack(1);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        verify(boardService, times(1)).getBoardById(1);
        verify(tileStackService, times(29)).save(any(TileStack.class));
        verify(tileStackService, times(1)).actualizeTile(1);
    }

    @Test
    void createTileStack_ShouldThrowException_WhenBoardNotExists() {
        when(boardService.getBoardById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tileStackController.createTileStack(1));
        verify(boardService, times(1)).getBoardById(1);
        verify(tileStackService, never()).save(any(TileStack.class));
    }

    @Test
    void deleteTileStack_ShouldDeleteTileStackById() {
        doNothing().when(tileStackService).delete(1);

        ResponseEntity<Void> response = tileStackController.deleteTileStack(1);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(tileStackService, times(1)).delete(1);
    }

    @Test
    void updateTileStack_ShouldUpdateTileStack() {
        TileStack updatedTileStack = new TileStack();
        updatedTileStack.setId(1);
        updatedTileStack.setUsed(true);

        when(tileStackService.getTileStackById(1)).thenReturn(Optional.of(tileStack));

        ResponseEntity<Void> response = tileStackController.updateTileStack(updatedTileStack, 1);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(tileStackService, times(1)).getTileStackById(1);
        verify(tileStackService, times(1)).save(tileStack);
    }

    @Test
    void updateTileStack_ShouldThrowException_WhenTileStackNotExists() {
        TileStack updatedTileStack = new TileStack();
        updatedTileStack.setId(1);
        updatedTileStack.setUsed(true);

        when(tileStackService.getTileStackById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tileStackController.updateTileStack(updatedTileStack, 1));
        verify(tileStackService, times(1)).getTileStackById(1);
        verify(tileStackService, never()).save(any(TileStack.class));
    }
}
