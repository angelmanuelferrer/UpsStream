package es.us.dp1.l4_04_24_25.Upstream.tile;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.AccessDeniedException;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ErrorTile;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.tileStack.TileStackService;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/tile")
@Tag(name = "Tiles", description = "API for the management of Tiles")
public class TileController {

    private final TileService tileService;
    private final BoardService boardService;
    private final TileStackService tileStackService;
    private final UserService userService;


    @Autowired
    public TileController(TileService tileService, BoardService boardService, TileStackService tileStackService, UserService userService){
        this.tileService = tileService;
        this.boardService = boardService;
        this.tileStackService = tileStackService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Obtiene todas las losetas", description = "Recupera todas las losetas.")
    public List<Tile> getAllTiles() {
        return tileService.getAllTile();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una loseta por su id", description = "Recupera una loseta basada en su id.")
    public Tile getTileById(@PathVariable("id") Integer id) {
        Tile t = tileService.getTileById(id);
        if (t == null)
            throw new ResourceNotFoundException("Tile", "id", id);
        return t;
    }

    @PostMapping("/{id}")
    @Operation(summary = "Crea una loseta en un Board", description = "Crea una loseta en un Board basado en su id.")
    public ResponseEntity<Tile> createTile(@PathVariable("id")Integer id, @RequestBody Tile tileRequest) throws ErrorTile {
        Board currentBoard = boardService.getBoardById(id).orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        boardService.checkTime(userService.findCurrentUser(),currentBoard);
        boardService.validateTilePlacement(id, tileRequest.getX(), tileRequest.getY());
        
        Player currentPlayer = currentBoard.getMatch().getPlayers().stream().filter(p -> 
                p.getUser().getId().equals(userService.findCurrentUser().getId())).findFirst().orElseThrow(()-> new AccessDeniedException("No puedes jugar en este tablero"));
        currentBoard.setTileCount(currentBoard.getTileCount() + 1);
        
        
        if(currentBoard.getTileCount()>= 13 && currentBoard.getTileCount() <30 && currentBoard.isSalmonMoved() && (currentBoard.getTurno().equals(Arrays.stream(Colour.values()).filter(c -> 
                c.ordinal() == 12 % currentBoard.getMatch().getNumberOfPlayers()).findFirst().get()) || currentBoard.getRoundPass()) 
                && currentBoard.getTurnoTile().equals(currentPlayer.getColour())){    
                if (currentBoard.getTileCount()%3 == 0) {
                    boardService.updateTurnoTile(currentBoard);
                    currentBoard.setSalmonMoved(false);
                    currentBoard.setRoundPass(false);
                }
        } else if(currentBoard.getTileCount() >= 13){
            throw new ErrorTile("Tienes que agotar tus movimientos de salmón antes de colocar una loseta");
        } else{
            boardService.updateTurno(currentBoard);
        }

        Tile tile = tileService.createTile(currentBoard, tileRequest);
        tileRequest.getTileStack().setUsed(true);
        tileStackService.actualizeTile(id);
        boardService.save(currentBoard);

        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(tile.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(tile); 
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una loseta", description = "Elimina una loseta basada en su id.")
    public ResponseEntity<Void> deleteTile(@PathVariable("id") Integer id) {
        tileService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/board/{id}")
    @Operation(summary = "Obtiene las losetas de un Board", description = "Recupera las losetas de un Board según su id.")
    public List<Tile> getTilesByBoardId(@PathVariable("id") Integer id) {
        return tileService.getTilesByBoardId(id);
    }
}