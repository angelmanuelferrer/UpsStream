package es.us.dp1.l4_04_24_25.Upstream.salmon;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.tile.Coordenadas;
import es.us.dp1.l4_04_24_25.Upstream.tile.Tile;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileType;

@ExtendWith(MockitoExtension.class)
public class SalmonControllerTest {

    @Mock
    private SalmonService salmonService;

    @Mock
    private TileService tileService;

    private SalmonController salmonController;

    private Board board;
    private Match match;
    private Salmon salmon;
    private Tile originTile;
    private Tile adjacentTile; // Una loseta adyacente para nadar
    private Tile distantTile; // Una loseta a dos posiciones para saltar
    private Player player;
    private List<Salmon> salmons;
    private Tile notValidTile;

    @BeforeEach
    void setUp() {
        salmonController = new SalmonController(salmonService, tileService);        
        board = new Board();
        board.setId(1);
        board.setTileCount(12); // Aseguramos que el board tiene al menos 12 losetas
        board.setMatch(match);
        board.setTurno(Colour.RED); // Inicializamos el turno con un valor de Colour

        player = new Player();
        player.setId(1);
        player.setPoints(0);
        player.setColour(Colour.RED);
        player.setMatch(match);

        salmon = new Salmon();
        salmon.setId(1);
        salmon.setX(0);
        salmon.setY(0);
        salmon.setBoard(board);
        salmon.setPlayer(player);
        salmon.setPair(true);

        originTile = new Tile();
        originTile.setX(0);
        originTile.setY(0);
        originTile.setCapacity(1);
        originTile.setType(TileType.WATER);
        originTile.setBoard(board);

        adjacentTile = new Tile();
        adjacentTile.setX(1);
        adjacentTile.setY(0);
        adjacentTile.setCapacity(1);
        adjacentTile.setType(TileType.WATER);
        adjacentTile.setBoard(board);

        distantTile = new Tile();
        distantTile.setX(2);
        distantTile.setY(0);
        distantTile.setCapacity(1);
        distantTile.setType(TileType.WATER);
        distantTile.setBoard(board);

        notValidTile = new Tile();
        notValidTile.setX(5);
        notValidTile.setY(0);
        notValidTile.setCapacity(1);
        notValidTile.setType(TileType.WATER);
        notValidTile.setBoard(board);
        
        salmons = new ArrayList<>();
        salmons.add(salmon);



    }

    @Test
void testMove_whenSwimIsSuccessful() {
    // Configuración del mock
    Integer salmonId = 1;
    Coordenadas destinationTile = new Coordenadas(1, 0);


    // Configurar el mock de salmonService
    when(salmonService.getSalmonById(salmonId)).thenReturn(salmon);
    when(tileService.getTileByCoordenadas(destinationTile.getX(), destinationTile.getY(), board.getId()))
            .thenReturn(adjacentTile);
    when(salmonService.canSwimToTile(salmon, adjacentTile)).thenReturn(true);
    when(salmonService.swim(salmon, adjacentTile)).thenReturn(true);

    // Llamada al método
    ResponseEntity<String> response = salmonController.move(salmonId, destinationTile);

    // Verificaciones
    assertEquals(200, response.getStatusCodeValue());
    assertTrue(response.getBody().contains("El salmón se ha movido a una nueva loseta."));
    verify(salmonService).swim(salmon, adjacentTile);
}

    

    @Test
    void testMove_whenJumpIsSuccessful() {
        int salmonId = 1;
        Coordenadas destinationTile = new Coordenadas(2, 0);

        when(salmonService.getSalmonById(salmonId)).thenReturn(salmon);
        when(tileService.getTileByCoordenadas(distantTile.getX(), distantTile.getY(), salmon.getBoard().getId())).thenReturn(distantTile);
        when(salmonService.canSwimToTile(salmon, distantTile)).thenReturn(false);
        when(salmonService.canJumpToTile(salmon, distantTile)).thenReturn(true);
        when(salmonService.jump(salmon, distantTile)).thenReturn(true);

        // Llamada al método
        ResponseEntity<String> response = salmonController.move(salmonId, destinationTile);

        // Verificaciones
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("El salmón se ha movido a una nueva loseta."));
        verify(salmonService).jump(salmon, distantTile);
    }

    @Test
    void testMove_whenMoveIsNotAllowed() {
        // Configuración del mock
        Integer salmonId = 1;
        Coordenadas destinationTile = new Coordenadas(5, 0);

        when(salmonService.getSalmonById(salmonId)).thenReturn(salmon);
        when(tileService.getTileByCoordenadas(destinationTile.getX(), destinationTile.getY(), salmon.getBoard().getId())).thenReturn(notValidTile);
        when(salmonService.canSwimToTile(salmon, notValidTile)).thenReturn(false);
        when(salmonService.canJumpToTile(salmon, notValidTile)).thenReturn(false);

        // Llamada al método y verificación de excepción
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            salmonController.move(salmonId, destinationTile);
        });

        assertEquals("Movimiento no permitido.", exception.getMessage());
    }

    @Test
    void testGetSalmonByBoard() {
        // Configuración del mock
        Integer boardId = 1;
        List<Salmon> salmonList = List.of(new Salmon(1, 1), new Salmon(2, 2));

        when(salmonService.getSalmonByBoard(boardId)).thenReturn(salmonList);

        // Llamada al método
        ResponseEntity<List<Salmon>> response = salmonController.getSalmonByBoard(boardId);

        // Verificaciones
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(salmonList, response.getBody());
    }
}
