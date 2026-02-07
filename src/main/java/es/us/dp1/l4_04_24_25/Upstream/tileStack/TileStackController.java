package es.us.dp1.l4_04_24_25.Upstream.tileStack;

import java.net.URI;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tileStack")
@Tag(name = "TileStack", description = "API for the management of TileStack")
public class TileStackController {

    private final TileStackService tileStackService;
    private final BoardService boardService;

    @Autowired
    public TileStackController(TileStackService tileStackService, BoardService boardService) {
        this.tileStackService = tileStackService;
        this.boardService = boardService;
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los TileStacks", description = "Recupera todos los TileStacks.")
    public List<TileStack> getAllTileStacks() {
        return tileStackService.getAllTileStacks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un TileStack por su id", description = "Recupera un TileStack basado en su id.")
    public TileStack getTileStackById(@PathVariable("id") Integer id) {
        return tileStackService.getTileStackById(id).orElseThrow(() -> new ResourceNotFoundException("TileStack", "id", id));
    }

    @PostMapping("/{id}")
    @Operation(summary = "Crea los TileStacks de un Board", description = "Crea los TileStacks de un Board basado en su id.")
    public ResponseEntity<TileStack> createTileStack(@PathVariable("id") Integer id) {
        Board currentBoard = boardService.getBoardById(id).orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));

        for (int i = 0; i < 4; i++) {
            TileStack tileStack = new TileStack();
            tileStack.setBoard(currentBoard);
            tileStack.setImage("/salto_agua.png");
            tileStack.setType(TileType.WATERFALL);
            tileStack.setUsed(false);
            tileStackService.save(tileStack);
        }

        for (int i = 0; i < 5; i++) {
            TileStack tileStack = new TileStack();
            tileStack.setBoard(currentBoard);
            tileStack.setImage("/aguila.png");
            tileStack.setType(TileType.EAGLE);
            tileStack.setUsed(false);
            tileStackService.save(tileStack);
        }

        for (int i = 0; i < 3; i++) {
            TileStack tileStack = new TileStack();
            tileStack.setBoard(currentBoard);
            tileStack.setImage("/oso.png");
            tileStack.setType(TileType.BEAR);
            tileStack.setUsed(false);
            tileStackService.save(tileStack);
        }

        for (int i = 0; i < 5; i++) {
            TileStack tileStack = new TileStack();
            tileStack.setBoard(currentBoard);
            tileStack.setImage("/garza.png");
            tileStack.setType(TileType.HERON);
            tileStack.setUsed(false);
            tileStackService.save(tileStack);
        }

        for (int i = 0; i < 5; i++) {
            TileStack tileStack = new TileStack();
            tileStack.setBoard(currentBoard);
            tileStack.setImage("/roca.png");
            tileStack.setType(TileType.ROCK);
            tileStack.setUsed(false);
            tileStackService.save(tileStack);
        }

        for (int i = 0; i < 7; i++) {
            TileStack tileStack = new TileStack();
            tileStack.setBoard(currentBoard);
            tileStack.setImage("/loseta_agua.png");
            tileStack.setType(TileType.WATER);
            tileStack.setUsed(false);
            tileStackService.save(tileStack);
        }

        tileStackService.actualizeTile(id);

        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString())).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un TileStack por su id", description = "Elimina un TileStack basado en su id.")
    public ResponseEntity<Void> deleteTileStack(@PathVariable("id") Integer id) {
        tileStackService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Actualiza un TileStack", description = "Actualiza un TileStack.")
    public ResponseEntity<Void> updateTileStack(@Valid @RequestBody TileStack tileStack, @PathVariable("id") Integer id) {
        TileStack tsToUpdate = getTileStackById(id);

        // Asegurarse de copiar el número de jugadores en el objeto a actualizar
        BeanUtils.copyProperties(tileStack, tsToUpdate, "id", "board", "image", "type");
        tileStackService.save(tsToUpdate);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/board")
    @Operation(summary = "Obtiene los TileStacks de un Board", description = "Recupera los TileStacks de un Board según su id.")
    public TileStack getTileStackByBoardId(@PathVariable("id") Integer id) {
        return tileStackService.getTileStackByBoardId(id);
    }

    
}
