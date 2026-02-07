package es.us.dp1.l4_04_24_25.Upstream.salmon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.lenient;
import org.mockito.junit.jupiter.MockitoExtension;
import es.us.dp1.l4_04_24_25.Upstream.tile.Tile;
import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.player.PlayerService;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonRepository;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonService;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileRepository;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileType;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

@ExtendWith(MockitoExtension.class)
public class SalmonServiceTest {

    @Mock
    private SalmonRepository salmonRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private TileService tileService;

    @Mock
    private PlayerService playerService;

    @Mock
    private TileRepository tileRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private SalmonService salmonService;

    private Board board;
    private Match match;
    private Salmon salmon;
    private Tile originTile;
    private Tile adjacentTile; // Una loseta adyacente para nadar
    private Tile distantTile; // Una loseta a dos posiciones para saltar
    private Player player;
    private Tile destinationTile;
    private List<Salmon> salmons;

    @BeforeEach
    void setUp() {
        match = new Match();
        match.setNumberOfPlayers(4);

        // Inicializamos la lista de jugadores
        Player player1 = new Player();
        player1.setId(1);
        player1.setPoints(0);

        Player player2 = new Player();
        player2.setId(2);
        player2.setPoints(0);

        Player player3 = new Player();
        player3.setId(3);
        player3.setPoints(0);

        Player player4 = new Player();
        player4.setId(4);
        player4.setPoints(0);

        match.setPlayers(List.of(player1, player2, player3, player4)); // Aquí inicializamos la lista

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
        
        salmons = new ArrayList<>();
        salmons.add(salmon);

        destinationTile = new Tile();
        lenient().when(tileService.getTileByCoordenadas(0, 0, board.getId())).thenReturn(originTile);
        lenient().when(tileService.getTileByCoordenadas(1, 0, board.getId())).thenReturn(adjacentTile);
        lenient().when(tileService.getTileByCoordenadas(2, 0, board.getId())).thenReturn(distantTile);
        lenient().when(tileRepository.getTileByCoordenadas(1, 0, board.getId())).thenReturn(Optional.of(adjacentTile));
        lenient().when(salmonRepository.findById(1)).thenReturn(Optional.of(salmon));
        

    }

    // TEST ADYACENTE
    @Test
    void testAdyacenteHorizontal() {
        // Salmon en (0, 1), destino en (1, 1) -> Adyacente horizontalmente
        salmon.setX(0);
        salmon.setY(1);
        destinationTile.setX(1);
        destinationTile.setY(1);

        boolean isAdjacent = salmonService.adyacente(salmon, destinationTile);

        assertTrue(isAdjacent);
    }

    @Test
    void testAdyacenteVertical() {
        // Salmon en (0, 1), destino en (0, 0) -> Adyacente verticalmente
        salmon.setX(0);
        salmon.setY(1);
        destinationTile.setX(0);
        destinationTile.setY(0);

        boolean isAdjacent = salmonService.adyacente(salmon, destinationTile);

        assertTrue(isAdjacent);
    }

    @Test
    void testAdyacenteDiagonal() {
        // Salmon en (11, 0), destino en (12, 1) -> Adyacente diagonalmente
        salmon.setX(11);
        salmon.setY(0);
        destinationTile.setX(12);
        destinationTile.setY(1);

        boolean isAdjacent = salmonService.adyacente(salmon, destinationTile);

        assertTrue(isAdjacent);
    }

    @Test
    void testNoAdyacenteDistantTile() {
        // Salmon en (0, 0), destino en (2, 0) -> No adyacente (demasiado lejos)
        salmon.setX(0);
        salmon.setY(0);
        destinationTile.setX(2);
        destinationTile.setY(0);

        boolean isAdjacent = salmonService.adyacente(salmon, destinationTile);

        assertFalse(isAdjacent);
    }

    @Test
    void testAdyacenteSpecialCase() {
        // Salmon en (10, 2), destino en (11, 1) -> Adyacente especial (por la regla del
        // método)
        salmon.setX(10);
        salmon.setY(2);
        destinationTile.setX(11);
        destinationTile.setY(1);

        boolean isAdjacent = salmonService.adyacente(salmon, destinationTile);

        assertTrue(isAdjacent);
    }

    @Test
    void testNoAdyacenteSpecialCase() {
        // Salmon en (11, 0), destino en (13, 1) -> No adyacente (demasiado lejos)
        salmon.setX(11);
        salmon.setY(0);
        destinationTile.setX(13);
        destinationTile.setY(1);

        boolean isAdjacent = salmonService.adyacente(salmon, destinationTile);

        assertFalse(isAdjacent);
    }

    // TEST ISCAPACITABLE

    @Test
    void testIsCapacitableValid() {
        // Configuramos la loseta con capacidad válida
        destinationTile.setCapacity(2);
        destinationTile.setType(TileType.WATER);
        destinationTile.setBoard(board);

        boolean result = salmonService.isCapacitable(destinationTile, salmon);

        assertTrue(result);
    }

    @Test
    void testIsCapacitableInvalidCapacity() {
        // Configuramos la loseta con capacidad inválida
        destinationTile.setCapacity(0);
        destinationTile.setType(TileType.WATER);
        destinationTile.setBoard(board);

        // Validamos que se lanza una excepción
        assertThrows(IllegalStateException.class, () -> salmonService.isCapacitable(destinationTile, salmon));
    }

    @Test
    void testIsCapacitableRockValid() {
        // Configuramos la loseta tipo ROCK con capacidad válida
        destinationTile.setCapacity(2);
        destinationTile.setType(TileType.ROCK);
        destinationTile.setBoard(board);

        boolean result = salmonService.isCapacitable(destinationTile, salmon);

        assertTrue(result);
    }

    @Test
    void testIsCapacitableRockInvalid() {
        // Configuramos la loseta tipo ROCK con capacidad insuficiente
        destinationTile.setCapacity(0);
        destinationTile.setType(TileType.ROCK);
        destinationTile.setBoard(board);

        // Validamos que se lanza una excepción
        assertThrows(IllegalStateException.class, () -> salmonService.isCapacitable(destinationTile, salmon));
    }

    @Test
    void testIsCapacitableRockEqualsPlayers() {
        // Configuramos la loseta tipo ROCK con capacidad igual al número de jugadores
        // menos la diferencia de 1
        destinationTile.setCapacity(1);
        destinationTile.setType(TileType.ROCK);
        destinationTile.setBoard(board);

        // Validamos que se lanza una excepción
        assertThrows(IllegalStateException.class, () -> salmonService.isCapacitable(destinationTile, salmon));
    }

    // TEST LOSETASSINCOLOCAR

    @Test
    void testLosetasSinColocarDoesNotThrowWhenConditionsNotMet() {
        // Configuración para que las condiciones del método no se cumplan
        board.setTileCount(30); // Más de 29 losetas
        board.setSalmonMoved(false);
        board.setRoundPass(false);
        board.setTurno(Colour.RED);
        match.setNumberOfPlayers(4);
        salmon.setBoard(board);

        assertDoesNotThrow(() -> salmonService.losetasSinColocar(salmon));
    }

    @Test
    void testLosetasSinColocarThrowsOnRoundPass() {
        // Configuración para que lance excepción cuando RoundPass sea true
        board.setTileCount(28); // Menos de 29 losetas
        board.setSalmonMoved(false);
        board.setRoundPass(true);
        board.setTurno(Colour.RED);
        match.setNumberOfPlayers(4);
        salmon.setBoard(board);

        assertThrows(IllegalStateException.class, () -> salmonService.losetasSinColocar(salmon));
    }

    // TEST CONTEOMASQUEDOCE

    @Test
    void testConteoMasQueDoceThrowsWhenLessThanTwelve() {
        // Llamamos al método con un valor menor a 12
        int count = 10;

        assertThrows(IllegalStateException.class, () -> salmonService.conteoMasQueDoce(count));
    }

    @Test
    void testConteoMasQueDoceDoesNotThrowWhenGreaterOrEqualTwelve() {
        // Llamamos al método con un valor mayor o igual a 12
        int count = 12;

        assertDoesNotThrow(() -> salmonService.conteoMasQueDoce(count));
    }

    // TEST SWIM

    @Test
    void testSwimValidMove() {
        boolean result = salmonService.swim(salmon, adjacentTile);
        assertTrue(result);
        assertEquals(1, salmon.getX());
        assertEquals(0, salmon.getY());
        verify(salmonRepository, times(1)).save(salmon);
    }

    @Test
    void testSwimInvalidMove() {
        boolean result = salmonService.swim(salmon, distantTile);  // Distant move is invalid for swim
    
        assertFalse(result);
        assertEquals(0, salmon.getX()); // No cambia la posición
        assertEquals(0, salmon.getY());
        verify(salmonRepository, never()).save(salmon);
    }
    
    @Test
    void testSwimBlockedByBear() {
        Tile bearTile = new Tile();
        bearTile.setX(1);
        bearTile.setY(0);
        bearTile.setType(TileType.BEAR);
        bearTile.setCapacity(1);
        bearTile.setBoard(board);
        bearTile.setRotation(0);

        boolean result = salmonService.swim(salmon, bearTile);

        assertFalse(result);
        verify(salmonRepository, never()).save(salmon);
    }

    @Test
    void testSwimBlockedInsedeWaterfall() {
        Tile waterfallTile = new Tile();
        waterfallTile.setX(0);
        waterfallTile.setY(0);
        waterfallTile.setType(TileType.WATERFALL);
        waterfallTile.setCapacity(1);
        waterfallTile.setBoard(board);
        waterfallTile.setRotation(0);

        boolean result = salmonService.swim(salmon, waterfallTile);

        assertFalse(result);
        verify(salmonRepository, never()).save(salmon);
    }
    @Test
    void testSwimBlockedInsedeBear() {
        originTile.setType(TileType.BEAR);
        originTile.setRotation(180);

        boolean result = salmonService.swim(salmon, adjacentTile);

        assertFalse(result);
        verify(salmonRepository, never()).save(salmon);
    }

    @Test
    void SwimToEagle(){
        adjacentTile.setType(TileType.EAGLE);
        boolean result = salmonService.swim(salmon, adjacentTile);
        assertTrue(result);
        assertEquals(1, salmon.getX());
        assertEquals(0, salmon.getY());
        assertFalse(salmon.isPair());
        assertEquals(TileType.WATER, adjacentTile.getType());
        assertEquals("/loseta_agua.png", adjacentTile.getImage());
        verify(salmonRepository, times(2)).save(salmon);
    }

    @Test
    void testSalmonMovesToEagleTileNotPair() {
        adjacentTile.setType(TileType.EAGLE);
        salmon.setPair(false); // Aseguramos que el salmón no es par
        boolean result = salmonService.swim(salmon, adjacentTile);
        assertTrue(result);
        assertEquals(TileType.WATER, adjacentTile.getType());
        assertEquals("/loseta_agua.png", adjacentTile.getImage());
        verify(salmonRepository, times(1)).deleteById(salmon.getId());
    }

    // TEST JUMP
    @Test
    void testJumpValidMove() {

        boolean result = salmonService.jump(salmon, distantTile);  // Distant move is valid for jump
        assertTrue(result);
        assertEquals(2, salmon.getX());
        assertEquals(0, salmon.getY());
        verify(salmonRepository, times(1)).save(salmon);
    }

    @Test
    void testJumpInvalidMove() {
        lenient().when(salmonRepository.findById(1)).thenReturn(Optional.of(salmon));

        boolean result = salmonService.jump(salmon, adjacentTile);  // Adjacent move is invalid for jump

        assertFalse(result);
        assertEquals(0, salmon.getX()); // No cambia la posición
        assertEquals(0, salmon.getY());
        verify(salmonRepository, never()).save(salmon);
    }

    // TEST CAN SWIM TO TILE

    @Test
    void testCanSwimToTileValid() {
        lenient().when(tileService.getTileByCoordenadas(1, 0, salmon.getBoard().getId())).thenReturn(adjacentTile);
        lenient().when(tileService.getTileByCoordenadas(0, 0, salmon.getBoard().getId())).thenReturn(originTile);

        boolean result = salmonService.canSwimToTile(salmon, adjacentTile);

        assertTrue(result);
    }

    @Test
    void testCanSwimToTileInvalid() {
        lenient().when(tileService.getTileByCoordenadas(2, 0, salmon.getBoard().getId())).thenReturn(distantTile);

        boolean result = salmonService.canSwimToTile(salmon, distantTile);  // Distant move is invalid for swim

        assertFalse(result);
    }

    // TEST JUMP
    @Test
    void testJumpBear() {
        Tile bearTile = new Tile();
        bearTile.setX(1);
        bearTile.setY(0);
        bearTile.setType(TileType.BEAR);
        bearTile.setCapacity(1);
        bearTile.setBoard(board);
        bearTile.setRotation(0);
        assertTrue(salmon.isPair());
        boolean result = salmonService.jump(salmon, bearTile);
        assertFalse(salmon.isPair());//comprueba que te come un salmon
        assertTrue(result);
        verify(salmonRepository, times(2)).save(salmon);
    }
    @Test
    void testJumpWaterfall() {
        originTile.setType(TileType.WATERFALL);
        originTile.setRotation(0);
        boolean result = salmonService.jump(salmon, adjacentTile);
        assertTrue(result);
        verify(salmonRepository, times(1)).save(salmon);
    }
    @Test
    void testJumpInvalidMoveBlockedByBear() {
        distantTile.setType(TileType.BEAR);
        distantTile.setRotation(0);
        boolean result = salmonService.jump(salmon, distantTile);
        assertFalse(result);
        verify(salmonRepository, never()).save(salmon);
    }
    @Test
    void testJumpInvalidMoveBlockedByIntermediateWaterfall(){
        adjacentTile.setType(TileType.WATERFALL);
        adjacentTile.setRotation(0);
        boolean result = salmonService.jump(salmon, distantTile);
        assertFalse(result);
        verify(salmonRepository, never()).save(salmon);
    }
    @Test
    void testJumpInvalidMoveBlockedByIntermediateBear(){
        adjacentTile.setType(TileType.BEAR);
        adjacentTile.setRotation(180);
        boolean result = salmonService.jump(salmon, distantTile);
        assertFalse(result);
        verify(salmonRepository, never()).save(salmon);
    }

    @Test
    void testJumpInvalidByCapacity() {
        distantTile.setCapacity(0);
        assertThrows(IllegalStateException.class, () -> {
            salmonService.jump(salmon, distantTile);
        });
        verify(salmonRepository, never()).save(salmon);
    }

    // TEST CAN JUMP TO TILE

    @Test
    void testCanJumpToTileValid() {

        boolean result = salmonService.canJumpToTile(salmon, distantTile);  // Distant move is valid for jump

        assertTrue(result);
    }

    @Test
    void testCanJumpToTileInvalid() {

        boolean result = salmonService.canJumpToTile(salmon, adjacentTile);  // Adjacent move is invalid for jump

        assertFalse(result);
    }

    @Test
    void testCanJumpToTileInvalidByMovement() {
        player.setMovement(4);
        assertThrows(IllegalStateException.class, () -> {
            salmonService.canJumpToTile(salmon, distantTile);
        });
        verify(salmonRepository, never()).save(salmon);
    }


   

    // TEST INTERMEDIATE TILE
    @Test
    void testInvalidIntermediateTile() {
        assertThrows(ResourceNotFoundException.class, () -> {
            salmonService.getIntermediateTile(originTile, adjacentTile);
        });
    }

    // TEST CRUD OPERATIONS

    @Test
    void testGetAllSalmons() {
        List<Salmon> salmons = List.of(salmon);
        lenient().when(salmonRepository.findAll()).thenReturn(salmons);

        List<Salmon> result = salmonService.getAllSalmons();

        assertEquals(salmons, result);
        verify(salmonRepository, times(1)).findAll();
    }

    @Test
    void testSaveSalmon() {
        lenient().when(salmonRepository.save(salmon)).thenReturn(salmon);

        Salmon result = salmonService.save(salmon);

        assertEquals(salmon, result);
        verify(salmonRepository, times(1)).save(salmon);
    }

    @Test
    void testDeleteSalmon() {
        lenient().when(salmonRepository.findById(1)).thenReturn(Optional.of(salmon));

        salmonService.delete(1);

        verify(salmonRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetSalmonById() {
        lenient().when(salmonRepository.findById(1)).thenReturn(Optional.of(salmon));

        Salmon result = salmonService.getSalmonById(1);

        assertEquals(salmon, result);
        verify(salmonRepository, times(1)).findById(1);
    }

    // TEST blocked
    @Test
    void testIsBlockedByBear() {
        Tile bearTile = new Tile();
        bearTile.setX(1);
        bearTile.setY(0);
        bearTile.setType(TileType.BEAR);
        bearTile.setCapacity(1);
        bearTile.setBoard(board);

        // Test rotation 0
        bearTile.setRotation(0);
        assertTrue(salmonService.isBlockedByBear(bearTile, 0, 0));
        assertFalse(salmonService.isBlockedByBear(bearTile, 1, 1));
        assertFalse(salmonService.isBlockedByBear(bearTile, 0, 1));

        // Test rotation 60
        bearTile.setRotation(60);
        assertTrue(salmonService.isBlockedByBear(bearTile, 0, -1));
        assertFalse(salmonService.isBlockedByBear(bearTile, 0, 0));
        assertFalse(salmonService.isBlockedByBear(bearTile, 1, 1));

        // Test rotation 300
        bearTile.setRotation(300);
        bearTile.setY(1);
        assertTrue(salmonService.isBlockedByBear(bearTile, 0, 2));
        assertTrue(salmonService.isBlockedByBear(bearTile, 0, 1));
        assertFalse(salmonService.isBlockedByBear(bearTile, 0, 0));

        // Test rotation 240
        bearTile.setRotation(240);
        assertTrue(salmonService.isBlockedByBear(bearTile, 0, 2));
        assertFalse(salmonService.isBlockedByBear(bearTile, 0, 1));
        assertFalse(salmonService.isBlockedByBear(bearTile, 0, 0));
    }

    @Test
    void testIsBlockedInsedeBear() {
        Tile bearTile = new Tile();
        bearTile.setX(0);
        bearTile.setY(1);
        bearTile.setType(TileType.BEAR);
        bearTile.setCapacity(1);
        bearTile.setBoard(board);

        Tile destinationTile = new Tile();
        destinationTile.setX(1);
        destinationTile.setY(0);
        destinationTile.setType(TileType.WATER);
        destinationTile.setCapacity(1);
        destinationTile.setBoard(board);

        // Test rotation 60
        bearTile.setRotation(60);
        assertTrue(salmonService.isBlockedInsedeBear(bearTile, destinationTile));
        destinationTile.setY(1);
        assertFalse(salmonService.isBlockedInsedeBear(bearTile, destinationTile));

        // Test rotation 120
        bearTile.setRotation(120);
        destinationTile.setY(0);
        assertTrue(salmonService.isBlockedInsedeBear(bearTile, destinationTile));
        destinationTile.setX(1);
        destinationTile.setY(1);
        assertTrue(salmonService.isBlockedInsedeBear(bearTile, destinationTile));
        destinationTile.setX(1);
        destinationTile.setY(2);
        assertFalse(salmonService.isBlockedInsedeBear(bearTile, destinationTile));

        // Test rotation 180
        bearTile.setRotation(180);
        destinationTile.setX(1);
        destinationTile.setY(1);
        assertTrue(salmonService.isBlockedInsedeBear(bearTile, destinationTile));
        destinationTile.setY(2);
        assertTrue(salmonService.isBlockedInsedeBear(bearTile, destinationTile));
        destinationTile.setY(0);
        assertFalse(salmonService.isBlockedInsedeBear(bearTile, destinationTile));

        // Test rotation 240
        bearTile.setRotation(240);
        destinationTile.setY(2);
        assertTrue(salmonService.isBlockedInsedeBear(bearTile, destinationTile));
        destinationTile.setY(0);
        assertFalse(salmonService.isBlockedInsedeBear(bearTile, destinationTile));
    }

    @Test
    void testIsBlockedByWaterWaterfall() {
        Tile waterfallTile = new Tile();
        waterfallTile.setX(1);
        waterfallTile.setY(0);
        waterfallTile.setType(TileType.WATERFALL);
        waterfallTile.setCapacity(1);
        waterfallTile.setBoard(board);

        // Test rotation 60
        waterfallTile.setRotation(60);
        assertTrue(salmonService.isBlockedByWaterWaterfall(waterfallTile, 1, 1));
        assertFalse(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 0));

        // Test rotation 120
        waterfallTile.setRotation(120);
        waterfallTile.setY(1);
        assertTrue(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 1));
        assertTrue(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 2));
        assertFalse(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 0));

        // Test rotation 180
        waterfallTile.setRotation(180);
        assertTrue(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 1));
        // Test rotation 240
        waterfallTile.setRotation(240);
        assertTrue(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 0));
        assertTrue(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 1));
        assertFalse(salmonService.isBlockedByWaterWaterfall(waterfallTile, 1, 2));

        // Test rotation 300
        waterfallTile.setRotation(300);
        assertTrue(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 0));
        assertFalse(salmonService.isBlockedByWaterWaterfall(waterfallTile, 0, 1));
    }

    @Test
    void testIsBlockedInsedeWaterfall() {
        Tile waterfallTile = new Tile();
        waterfallTile.setX(0);
        waterfallTile.setY(1);
        waterfallTile.setType(TileType.WATERFALL);
        waterfallTile.setCapacity(1);
        waterfallTile.setBoard(board);

        Tile destinationTile = new Tile();
        destinationTile.setX(1);
        destinationTile.setY(1);
        destinationTile.setType(TileType.WATER);
        destinationTile.setCapacity(1);
        destinationTile.setBoard(board);

        // Test rotation 0
        waterfallTile.setRotation(0);
        assertTrue(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));

        // Test rotation 60
        waterfallTile.setRotation(60);
        assertTrue(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));
        destinationTile.setY(2);
        assertTrue(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));
        destinationTile.setY(0);
        assertFalse(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));

        // Test rotation 120
        waterfallTile.setRotation(120);
        destinationTile.setY(2);
        assertTrue(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));
        destinationTile.setY(0);
        assertFalse(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));

        // Test rotation 240
        waterfallTile.setRotation(240);
        destinationTile.setY(0);
        assertTrue(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));
        destinationTile.setY(1);
        assertFalse(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));

        // Test rotation 300
        waterfallTile.setRotation(300);
        destinationTile.setY(0);
        assertTrue(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));
        destinationTile.setY(1);
        assertTrue(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));
        destinationTile.setY(2);
        assertFalse(salmonService.isBlockedInsedeWaterfall(waterfallTile, destinationTile));
    }


    @Test
    void testCanMoveToAnyTile() {
        // Configurar mocks para el test
        lenient().when(salmonRepository.findSalmonByPlayer(player)).thenReturn(salmons);
        lenient().when(tileRepository.getTileByCoordenadas(1, 0, board.getId())).thenReturn(Optional.of(adjacentTile));
        lenient().when(tileRepository.getTileByCoordenadas(2, 0, board.getId())).thenReturn(Optional.of(distantTile));
        lenient().when(tileRepository.getTileByCoordenadas(0, 0, board.getId())).thenReturn(Optional.of(originTile));
        lenient().when(tileRepository.getTileByCoordenadas(1, -1, board.getId())).thenReturn(Optional.empty());


        boolean result = salmonService.canMoveToAnyTile(player, false);
        assertTrue(result);

        salmon.setX(7);
        result = salmonService.canMoveToAnyTile(player, false);
        assertFalse(result);
    }
}

