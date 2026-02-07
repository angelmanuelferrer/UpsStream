package es.us.dp1.l4_04_24_25.Upstream.tileStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;

@SpringBootTest
public class TileStackServiceTest {

    @Mock
    private TileStackRepository tileStackRepository;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private TileStackService tileStackService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTileStacks() {
        TileStack tileStack1 = new TileStack();
        TileStack tileStack2 = new TileStack();
        List<TileStack> tileStacks = Arrays.asList(tileStack1, tileStack2);

        when(tileStackRepository.findAll()).thenReturn(tileStacks);

        List<TileStack> result = tileStackService.getAllTileStacks();

        assertEquals(2, result.size());
        verify(tileStackRepository, times(1)).findAll();
    }

    @Test
    public void testGetTileStackById() {
        TileStack tileStack = new TileStack();
        when(tileStackRepository.getTileStackById(1)).thenReturn(Optional.of(tileStack));

        Optional<TileStack> result = tileStackService.getTileStackById(1);

        assertTrue(result.isPresent());
        verify(tileStackRepository, times(1)).getTileStackById(1);
    }

    @Test
    public void testSaveTileStack() {
        TileStack tileStack = new TileStack();
        when(tileStackRepository.save(tileStack)).thenReturn(tileStack);

        TileStack result = tileStackService.save(tileStack);

        assertNotNull(result);
        verify(tileStackRepository, times(1)).save(tileStack);
    }

    @Test
    public void testDeleteTileStack() {
        doNothing().when(tileStackRepository).deleteById(1);

        tileStackService.delete(1);

        verify(tileStackRepository, times(1)).deleteById(1);
    }

    @Test
    public void testActualizeTile() {
        TileStack tileStack1 = new TileStack();
        tileStack1.setUsed(false);
        TileStack tileStack2 = new TileStack();
        tileStack2.setUsed(false);
        List<TileStack> tileStacks = Arrays.asList(tileStack1, tileStack2);

        Board board = new Board();
        when(tileStackRepository.getTileStackByBoardId(1)).thenReturn(tileStacks);
        when(boardService.getBoardById(1)).thenReturn(Optional.of(board));
        when(boardService.save(board)).thenReturn(board);

        tileStackService.actualizeTile(1);

        verify(tileStackRepository, times(1)).getTileStackByBoardId(1);
        verify(boardService, times(1)).getBoardById(1);
        verify(boardService, times(1)).save(board);
    }

    @Test
    public void testGetTileStackByBoardId() {
        TileStack tileStack = new TileStack();
        Board board = new Board();
        board.setTileStack(tileStack);
        when(boardService.getBoardById(1)).thenReturn(Optional.of(board));

        TileStack result = tileStackService.getTileStackByBoardId(1);

        assertNotNull(result);
        verify(boardService, times(1)).getBoardById(1);
    }

    @Test
    public void testActualizeTileWithNoTileStacks() {
        when(tileStackRepository.getTileStackByBoardId(1)).thenReturn(Arrays.asList());

        assertThrows(ResourceNotFoundException.class, () -> {
            tileStackService.actualizeTile(1);
        });

        verify(tileStackRepository, times(1)).getTileStackByBoardId(1);
    }

    @Test
    public void testActualizeTileWithAllUsedTileStacks() {
        TileStack tileStack1 = new TileStack();
        tileStack1.setUsed(true);
        TileStack tileStack2 = new TileStack();
        tileStack2.setUsed(true);
        List<TileStack> tileStacks = Arrays.asList(tileStack1, tileStack2);

        when(tileStackRepository.getTileStackByBoardId(1)).thenReturn(tileStacks);

        tileStackService.actualizeTile(1);

        verify(tileStackRepository, times(1)).getTileStackByBoardId(1);
    }

    @Test
    public void testGetTileStackByBoardIdWithNonExistentBoard() {
        when(boardService.getBoardById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tileStackService.getTileStackByBoardId(1);
        });

        verify(boardService, times(1)).getBoardById(1);
    }
}