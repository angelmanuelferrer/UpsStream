package es.us.dp1.l4_04_24_25.Upstream.match;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.chat.ChatService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.GameLimitException;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.friends.FriendService;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.player.PlayerService;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonService;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.tileStack.TileStackService;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "API for the management of Matches")
@SecurityRequirement(name = "bearerAuth")
public class MatchController {

    private final MatchService matchService;
    private final UserService userService;
    private final BoardService boardService;
    private final PlayerService playerService;
    private final TileService tileService;
    private final TileStackService tileStackService;
    private final SalmonService salmonService;
    private final ChatService chatService;
    private final FriendService friendService;

    public MatchController(MatchService matchService, UserService userService, BoardService boardService, PlayerService playerService, 
    TileService tileService, TileStackService tileStackService, SalmonService salmonService, ChatService chatService, FriendService friendService) {
        this.matchService = matchService;
        this.userService = userService;
        this.boardService = boardService;
        this.playerService = playerService;
        this.tileService = tileService;
        this.tileStackService = tileStackService;
        this.salmonService = salmonService;
        this.chatService = chatService;
        this.friendService = friendService;
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los partidos", description = "Obtiene todos los partidos.")
    public List<Match> getAllMatches(@ParameterObject() @RequestParam(value="name",required = false) String name, @ParameterObject @RequestParam(value="status",required = false) MatchStatus status){
        return matchService.getAllMatches();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un partido por un ID", description = "Obtiene un partidos por un ID.")
    public Match getMatchById(@PathVariable("id")Integer id){
        Optional<Match> m = matchService.getMatchById(id);
        if(!m.isPresent())
            throw new ResourceNotFoundException("Match", "id", id);
        return m.get();
    }
    private void validateGameLimit(User user) throws GameLimitException{
      if(!(!user.getProfile().isOccasionalPlayer() || matchService.findCountMatchYesterday(user)<2)){
            throw new GameLimitException("Game limit of 2 reached");
      }
    }

    @PostMapping()
    @Operation(summary = "Crear un nuevo partido", description = "Crea un nuevo partido.")
    public ResponseEntity<Match> createMatch(@Valid @RequestBody Match m) throws GameLimitException{ 
        User currentUser = userService.findCurrentUser();
        m.setUser(currentUser);

        // Guardar el nuevo campo de número de jugadores
        Match savedMatch = matchService.save(m);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedMatch.getId())
                .toUri();
        validateGameLimit(currentUser);
        
        return ResponseEntity.created(location).body(savedMatch);
    }

    @PutMapping(value="/{id}")
    @Operation(summary = "Actualiza un partido", description = "Actualiza un partido.")
    public ResponseEntity<Void> updateMatch(@Valid @RequestBody Match m,@PathVariable("id")Integer id){
        Match mToUpdate = matchService.getMatchById(id).orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
        
        BeanUtils.copyProperties(m, mToUpdate, "id", "players", "user", "finish", "start", "status");
        matchService.save(mToUpdate);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value="/{id}/start")
    @Operation(summary = "Actualiza la fecha de inicio de un partido", description = "Actualiza la fecha de inicio de un partido.")
    public ResponseEntity<Void> updateMatchStartDate(@Valid @RequestBody Match m,@PathVariable("id")Integer id){
        Match mToUpdate = matchService.getMatchById(id).orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
        
        BeanUtils.copyProperties(m, mToUpdate, "id", "players", "user", "code", "finish", "numberOfPlayers", "name", "status");
        matchService.save(mToUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un partido", description = "Elimina un partido.")
    public ResponseEntity<Void> deleteMatch(@PathVariable("id") Integer id) {
        if (getMatchById(id) != null) {

            if (boardService.getBoardByMatchId(id).isPresent()) {
                Board b = boardService.getBoardByMatchId(id).get();
                tileService.getAllTile().stream()
                    .filter(t -> t.getBoard().getId().equals(b.getId()))
                    .forEach(t -> {
                        tileService.delete(t.getId());
                    });

                salmonService.getAllSalmons().stream()
                    .filter(s -> s.getBoard().getId().equals(b.getId()))
                    .forEach(s -> {
                        salmonService.delete(s.getId());
                    });

                b.setTileStack(null);
                boardService.save(b);

                tileStackService.getAllTileStacks().stream()
                    .filter(ts -> ts.getBoard().getId().equals(b.getId()))
                    .forEach(ts -> {
                        tileStackService.delete(ts.getId());
                    });

                chatService.getMessagesByBoard(b).stream()
                    .forEach(m -> {
                        chatService.delete(m.getId());
                    });

                friendService.findMatchInvitations().stream()
                    .filter(f -> f.getMatch().getId().equals(id))
                    .forEach(f -> {
                        friendService.delete(f.getId());
                    });
                    
                boardService.delete(b.getId());
            }

            matchService.getMatchById(id).get().getPlayers().forEach(p -> {
                playerService.delete(p.getId());
            });

            matchService.delete(id);
        }
        return ResponseEntity.noContent().build();
    }

    //Para unirse a un juego:
    @PostMapping("/{matchId}/join")
    @Operation(summary = "Unirse a un partido", description = "Unirse a un partido.")
    public ResponseEntity<String> joinMatch(@PathVariable("matchId") Integer id)  throws GameLimitException{
        User user=userService.findCurrentUser();
        validateGameLimit(user);
        Match match = matchService.getMatchById(id).orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));

        if(match.getFinish() != null) {
            throw new IllegalStateException("Partida ya terminada.");
        } else if(match.getStart() != null) {
            throw new IllegalStateException("Partida ya empezada.");
        }

        match.setStatus(MatchStatus.WAITING);
        matchService.save(match);

        User currentUser = userService.findCurrentUser();
        int playerIndex = matchService.findPlayersByMatchId(id).size(); // Obtener el índice del jugador basado en el número de jugadores actuales
        matchService.addPlayerToMatch(id, currentUser, playerIndex); 
        return ResponseEntity.ok("Player joined the match successfully.");
    } 

    @GetMapping("/{matchId}/players")
    @Operation(summary = "Obtiene los jugadores de un Partido", description = "Obtiene los jugadores de un Partido por un ID.")
    public ResponseEntity<List<Player>> getPlayersByMatchId(@PathVariable("matchId") Integer id) {
        List<Player> players = matchService.findPlayersByMatchId(id);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{matchId}/status")
    @Operation(summary = "Obtiene el status de un partido", description = "Obtiene el status de un partido que se pone a true cuando se crea un partido.")
    public Boolean getMatchStatus(@PathVariable("matchId") Integer id) {
        Boolean status = matchService.getMatchStatus(id); 
        return status;
    }

    
}