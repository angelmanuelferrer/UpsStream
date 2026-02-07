package es.us.dp1.l4_04_24_25.Upstream.salmon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.l4_04_24_25.Upstream.tile.Coordenadas;
import es.us.dp1.l4_04_24_25.Upstream.tile.Tile;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/salmon")
@Tag(name = "Salmons", description = "API for the management of Salmons")
public class SalmonController {


    private final SalmonService salmonService; 
    private final TileService tileService;

    @Autowired
    public SalmonController(SalmonService salmonService, TileService tileService) {
        this.salmonService = salmonService;
        this.tileService = tileService;
    }

    @PutMapping("/{salmonId}/move")
    @Operation(summary = "Mueve un Salmon a una Tile", description = "Mueve un Salmon a una Tile basado en unas coordenadas.")
    public ResponseEntity<String> move(@PathVariable Integer salmonId, @RequestBody Coordenadas destinationTile) {
        Salmon salmon = salmonService.getSalmonById(salmonId);
        Tile tile = tileService.getTileByCoordenadas(destinationTile.getX(), destinationTile.getY(), salmon.getBoard().getId());

        if (salmonService.canSwimToTile(salmon, tile)) {
            boolean success = salmonService.swim(salmon, tile);
            return ResponseEntity.ok("El salmón se ha movido a una nueva loseta." + success);
        } else if(salmonService.canJumpToTile(salmon, tile)) {
            boolean success = salmonService.jump(salmon, tile);
            return ResponseEntity.ok("El salmón se ha movido a una nueva loseta." + success);
        } else {
            throw new IllegalStateException("Movimiento no permitido.");
        }  
    }

    @GetMapping("/{board}")
    @Operation(summary = "Obtiene todos los Salmones de un Board", description = "Recupera todos los salmones de un Board.")
    public ResponseEntity<List<Salmon>> getSalmonByBoard(@PathVariable Integer board) {
        return ResponseEntity.ok(salmonService.getSalmonByBoard(board));
    }
}