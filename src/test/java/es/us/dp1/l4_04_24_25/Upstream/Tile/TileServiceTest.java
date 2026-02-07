package es.us.dp1.l4_04_24_25.Upstream.tile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonRepository;

public class TileServiceTest {

    @Mock
    private TileRepository tileRepository;

    @Mock
    private SalmonRepository salmonRepository;

    @InjectMocks
    private TileService tileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    private Tile tile;

    @BeforeEach
    void setUp1() {
        tile = new Tile();
    }

    @Test
    public void testInitializeTile() {
        Board board = new Board();
        tileService.initializeTile(board);
        verify(tileRepository, times(8)).save(any(Tile.class));
    }

    @Test
    public void testGetAllTile() {
        List<Tile> tiles = Arrays.asList(new Tile(), new Tile());
        when(tileRepository.findAll()).thenReturn(tiles);
        List<Tile> result = tileService.getAllTile();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetTileById() {
        Tile tile = new Tile();
        when(tileRepository.getTileById(1)).thenReturn(Optional.of(tile));
        Tile result = tileService.getTileById(1);
        assertNotNull(result);
    }

    @Test
    public void testGetTileById_NotFound() {
        when(tileRepository.getTileById(1)).thenReturn(Optional.empty());
        Tile result = tileService.getTileById(1);
        assertNull(result);
    }

    @Test
    public void testSave() {
        Tile tile = new Tile();
        when(tileRepository.save(tile)).thenReturn(tile);
        Tile result = tileService.save(tile);
        assertNotNull(result);
    }

    @Test
    public void testDelete() {
        tileService.delete(1);
        verify(tileRepository, times(1)).deleteById(1);
    }

    @Test
    public void testCreateTile() {
        Board board = new Board();
        Tile tileRequest = new Tile();
        tileRequest.setCapacity(10);
        when(tileRepository.save(any(Tile.class))).thenReturn(tileRequest);
        Tile result = tileService.createTile(board, tileRequest);
        assertNotNull(result);
        assertEquals(10, result.getCapacity());
    }

    @Test
    public void testMoveSalmonToTile_NoSpace() {
        Salmon salmon = new Salmon();
        Tile tile = new Tile();
        tile.setCapacity(0);
        when(salmonRepository.getSalmonById(1)).thenReturn(Optional.of(salmon));
        when(tileRepository.getTileById(1)).thenReturn(Optional.of(tile));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tileService.moveSalmonToTile(1, 1);
        });
        assertEquals("La loseta no tiene espacio suficiente", exception.getMessage());
    }

    @Test
    public void testGetTileByCoordenadas() {
        Tile tile = new Tile();
        when(tileRepository.getTileByCoordenadas(1, 1, 1)).thenReturn(Optional.of(tile));
        Tile result = tileService.getTileByCoordenadas(1, 1, 1);
        assertNotNull(result);
    }

    @Test
    public void testGetTileByCoordenadas_NotFound() {
        when(tileRepository.getTileByCoordenadas(1, 1, 1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            tileService.getTileByCoordenadas(1, 1, 1);
        });
        assertEquals("No se encuentra ninguna ficha en esas coordenadas", exception.getMessage());
    }

    @Test
    public void testGetTilesByBoardId() {
        List<Tile> tiles = Arrays.asList(new Tile(), new Tile());
        when(tileRepository.getTilesByBoardId(1)).thenReturn(tiles);
        List<Tile> result = tileService.getTilesByBoardId(1);
        assertEquals(2, result.size());
    }

    @Test
    void testAddSalmon() {
        // Preparación
        Salmon salmon = new Salmon();
        
        // Ejecución
        tile.addSalmon(salmon);

        // Verificación
        List<Salmon> salmons = tile.getSalmons();
        assertNotNull(salmons, "La lista de salmones no debe ser nula");
        assertEquals(1, salmons.size(), "Debe haber exactamente 1 salmón en la lista");
        assertTrue(salmons.contains(salmon), "El salmón agregado debe estar presente en la lista");
    }

    @Test
    void testRemoveSalmon() {
        // Preparación
        Salmon salmon = new Salmon();
        tile.addSalmon(salmon);

        // Ejecución
        tile.removeSalmon(salmon);

        // Verificación
        List<Salmon> salmons = tile.getSalmons();
        assertNotNull(salmons, "La lista de salmones no debe ser nula");
        assertEquals(0, salmons.size(), "La lista de salmones debe estar vacía después de eliminar el salmón");
        assertFalse(salmons.contains(salmon), "El salmón eliminado no debe estar presente en la lista");
    }

    @Test
    void testGetPuntos() {
        // Casos específicos con resultados esperados
        assertEquals(1, Tile.getPuntos(11, 1), "Los puntos para (11, 1) deben ser 1");
        assertEquals(2, Tile.getPuntos(11, 0), "Los puntos para (11, 0) deben ser 2");
        assertEquals(3, Tile.getPuntos(12, 1), "Los puntos para (12, 1) deben ser 3");
        assertEquals(9, Tile.getPuntos(11, 2), "Los puntos para (11, 2) deben ser 9");

        // Caso por defecto (no coincide con ninguna condición)
        assertEquals(0, Tile.getPuntos(10, 10), "Los puntos para (10, 10) deben ser 0");
        assertEquals(0, Tile.getPuntos(0, 0), "Los puntos para (0, 0) deben ser 0");
    }
}