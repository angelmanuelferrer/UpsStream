package es.us.dp1.l4_04_24_25.Upstream.board;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import es.us.dp1.l4_04_24_25.Upstream.chat.ChatService;
import es.us.dp1.l4_04_24_25.Upstream.chat.Message;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.AccessDeniedException;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchService;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchStatus;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/board")
@Tag(name = "Boards", description = "API for the management of Boards")
@SecurityRequirement(name = "bearerAuth")
public class BoardController {

    private  BoardService boardService;
    private  MatchService matchService;
    private  TileService tileService;
    private ChatService chatService;
    private UserService userService;
   

    @Autowired
    public BoardController(BoardService boardService, MatchService matchService, TileService tileService, ChatService chatService, UserService userService) {    
        this.boardService = boardService;
        this.matchService = matchService;
        this.tileService = tileService;
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los Boards", description = "Recupera todos los boards.")
    public List<Board> getAllBoards(){
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un Board por su id", description = "Recupera un board basado en su id.")
    public Board getBoardById(@PathVariable("id")Integer id){
        Optional<Board> b = boardService.getBoardById(id);
        if(!b.isPresent())
            throw new ResourceNotFoundException("Board", "id", id);
        return b.get();
    }
    
    private static final Colour[] COLOURS = {Colour.GREEN, Colour.RED, Colour.PURPLE, Colour.YELLOW, Colour.ORANGE};
    
    @PostMapping("/{id}")
    @Operation(summary = "Crea un Board para un Match", description = "Crea un Board para un Match basado en su id.")
    public ResponseEntity<Board> createBoard(@PathVariable("id")Integer id){
        Match currentMatch = matchService.getMatchById(id).orElseThrow(() -> new ResourceNotFoundException("match", "id", id));
        currentMatch.setStatus(MatchStatus.PLAYING);
        matchService.save(currentMatch);
        
        // Asignar colores a los jugadores
        List<Colour> ls = new ArrayList<>(Arrays.asList(COLOURS).subList(0, currentMatch.getPlayers().size()));
        Random number = new Random(System.nanoTime());
        for(Player p : currentMatch.getPlayers()){
            Colour cl = ls.get(number.nextInt(0, ls.size()));
            p.setColour(cl);
            ls.remove(cl);
        }
        Board board = new Board();
        board.setMatch(currentMatch);
        board.setTurno(Colour.GREEN);
        board.setTurnoTile(Colour.GREEN);
        Board savedBoard = boardService.save(board);

        boardService.initializeSalmones(savedBoard.getId());
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedBoard.getId())
                .toUri();
        tileService.initializeTile(savedBoard);
        return ResponseEntity.created(location).body(savedBoard);
    }

    @PutMapping(value="/{id}")
    @Operation(summary = "Actualiza un Board", description = "Actualiza un Board.")
    public ResponseEntity<Void> updateBoard(@Valid @RequestBody Board board,@PathVariable("id")Integer id){
        Board mToUpdate = getBoardById(id);
        
        // Asegurarse de copiar el número de jugadores en el objeto a actualizar
        BeanUtils.copyProperties(board, mToUpdate, "id");
        boardService.save(mToUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un Board", description = "Elimina un Board.")
    public ResponseEntity<Void> deleteBoard(@PathVariable("id")Integer id){
        if(getBoardById(id)!=null) boardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/match/{id}")
    @Operation(summary = "Obtiene un Board por su Match id", description = "Recupera un board basado en su match id.")
    public Board getBoardByMatchId(@PathVariable("id")Integer id){
        Optional<Board> b = boardService.getBoardByMatchId(id);
        if(!b.isPresent())
            throw new ResourceNotFoundException("Board", "match_id", id);
        return b.get();
    }

    @GetMapping("/{id}/turno")
    @Operation(summary = "Obtiene el turno de un Board", description = "Recupera el turno de un board basado en su id.")
    public Colour getTurnoByBoardId(@PathVariable("id") Integer id) {
        Board board = boardService.getBoardById(id).orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        return board.getTurno();
    }

    @PostMapping("/{boardId}/initializeSalmones")
    @Operation(summary = "Inicializa los salmons de un Board", description = "Inicializa los salmons de un Board basado en su id.")
    public ResponseEntity<?> initializeSalmones(@PathVariable Integer boardId) {
    try {
        List<Salmon> salmons = boardService.initializeSalmones(boardId);
        return ResponseEntity.ok(salmons);
    } catch (NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board or player not found.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error initializing salmons.");
    }
    }

    @GetMapping("/{id}/tileCount")
    @Operation(summary = "Obtiene el número de losetas de un Board", description = "Recupera el número de losetas de un board basado en su id.")
    public int getTileCountByBoardId(@PathVariable("id") Integer id) {
        Board board = boardService.getBoardById(id).orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        return board.getTileCount();
    }

    @GetMapping("/{id}/turnoTile")
    @Operation(summary = "Obtiene el turno de losetas de un Board", description = "Recupera el turno de losetas de un board basado en su id.")
    public Colour getTurnoTileByBoardId(@PathVariable("id") Integer id) {
        Board board = boardService.getBoardById(id).orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        return board.getTurnoTile();
    }


    @GetMapping("/{id}/winner")
    @Operation(summary = "Obtiene el ganador de un Board", description = "Recupera el ganador de un board basado en su id.")
    public ResponseEntity<User> getWinner (@PathVariable("id") Integer id) {
        Board board = boardService.getBoardById(id).orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        return new ResponseEntity<>(board.getWinner(), HttpStatus.OK);
    }
     @GetMapping("/{id}/userTimeExceeded")
    @Operation(summary = "Obtiene el usuario con el tiempo excedido de un Board", description = "Recupera el ganador de un board basado en su id.")
    public ResponseEntity<User> getUserTimeExceeded (@PathVariable("id") Integer id) {
        Board board = boardService.getBoardById(id).orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        return new ResponseEntity<>(board.getUserTimeExceeded(), HttpStatus.OK);
    }

    @PostMapping("/{id}/sendChat")
    @Operation(summary = "Envía un mensaje al chat de un Board", description = "Envía un mensaje al chat de un board basado en su id.")
    public ResponseEntity<?> sendMessageToChat(
            @PathVariable("id") Integer id,
            @RequestParam("message") String message) {
        try {
            // Obtener el Board y el Player
            Board board = boardService.getBoardById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Board", "id",id));
            Player player = board.getMatch().getPlayers().stream()
                    .filter(p -> p.getUser().getId().equals(userService.findCurrentUser().getId()))
                    .findFirst()
                    .orElseThrow(() -> new AccessDeniedException("Tienes que estar en la partida para poder escribir en el chat"));

            // Guardar el mensaje en el chat
            Message chat = chatService.saveMessage(board, player, message);
            return ResponseEntity.ok(chat);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message.");
        }
    }

    @GetMapping("/{id}/chat")
    @Operation(summary = "Obtiene los mensajes del chat de un Board", description = "Recupera los mensajes del chat de un board basado en su id.")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable("id") Integer boardId) {
        try {
            // Obtener el Board
            Board board = boardService.getBoardById(boardId)
                    .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));

            // Obtener los mensajes del chat
            List<Message> messages = chatService.getMessagesByBoard(board);
            return ResponseEntity.ok(messages);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/friendsBoard")
    @Operation(summary = "Obtiene los Boards de los amigos", description = "Recupera los Boards de los amigos.")
    public ResponseEntity<List<Board>> getFriendsBoards() {
        return new ResponseEntity<>(boardService.getFriendsBoards(), HttpStatus.OK);
    }
}