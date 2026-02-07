package es.us.dp1.l4_04_24_25.Upstream.player;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/players")
@Tag(name = "Players", description = "API for the management of Players")
@SecurityRequirement(name = "bearerAuth")
public class PlayerController {
    
    
    private PlayerService playerService;

    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    @GetMapping()
    @Operation(summary = "Obtiene todos los Jugadores", description = "Recupera todos los jugadores.")
    public List<Player> getAllPlayers(){
        return playerService.getAllPlayers();
    
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un Jugador por su id", description = "Recupera un jugador basado en su id.")
    public Player getPlayerById(@PathVariable("id")Integer id){
        return playerService.getPlayerById(id);
    }
    
    @GetMapping("/{id}/movement")
    @Operation(summary = "Obtiene los movimientos de un Jugador por su id", description = "Recupera los movimientos de un jugador basado en su id.")
    public int getMovementByPlayerId(@PathVariable("id")Integer id){
        return playerService.getMovementByPlayerId(id);
    }
}
