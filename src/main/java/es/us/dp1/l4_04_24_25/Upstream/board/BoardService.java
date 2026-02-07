package es.us.dp1.l4_04_24_25.Upstream.board;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchService;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchStatus;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.player.PlayerService;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonRepository;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonService.Coordenadas;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonState;
import es.us.dp1.l4_04_24_25.Upstream.statistics.StatisticsService;
import es.us.dp1.l4_04_24_25.Upstream.tile.Tile;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileType;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {

    private BoardRepository boardRepository;
    private SalmonRepository salmonRepository;
    private MatchService matchService;
    private TileService tileService;
    private UserService userService;
    private StatisticsService statisticsService;

    @Autowired
    public BoardService(BoardRepository boardRepository,
            PlayerService playerService, SalmonRepository salmonRepository, MatchService matchService, TileService tileService, UserService userService, StatisticsService statisticsService) {
        this.boardRepository = boardRepository;
        this.salmonRepository = salmonRepository;
        this.matchService = matchService;
        this.tileService = tileService;
        this.userService = userService;
        this.statisticsService = statisticsService;
    }

    @Transactional(readOnly = true)
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Board> getBoardById(Integer id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Transactional
    public void delete(Integer id) {
        boardRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Board> getBoardByMatchId(Integer id) {
        return boardRepository.findBoardByMatchId(id);
    }
    @Transactional
    public void checkTime(User user,Board b){
        LocalDateTime now=LocalDateTime.now();
        if(user.getProfile().isOccasionalPlayer() && ChronoUnit.SECONDS.between(b.getMatch().getStart(),now)>=6000){
            b.setUserTimeExceeded(user);
            b.getMatch().setFinish(now);
            b.getMatch().setStatus(MatchStatus.FINISHED);
            matchService.save(b.getMatch());
            boardRepository.save(b);
        }
    }

    // inicializa los salmones en el tablero y devuelve la lista de salmones creados
    @Transactional
    public List<Salmon> initializeSalmones(Integer boardId) {
        List<Salmon> existingSalmons = boardRepository.findSalmonsByBoardId(boardId);
        if (existingSalmons != null && !existingSalmons.isEmpty()) {
            return existingSalmons;
        }

        Board board = getBoardById(boardId).orElseThrow(() -> new IllegalArgumentException("Tablero no encontrado"));
        if (board.getMatch() == null) {
            throw new IllegalStateException("El tablero no tiene una partida asociada.");
        }

        List<Player> players = matchService.findPlayersByMatchId(board.getMatch().getId());
        if (players.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida.");
        }

        List<Salmon> salmons = createSalmon(board, players);
        return salmons;
    }

    @Transactional
    public List<Salmon> createSalmon(Board board, List<Player> players) {
        List<Salmon> salmons = new ArrayList<>();

        // Definir las posiciones iniciales
        List<SimpleEntry<Integer, Integer>> initialTiles = Arrays.asList(
                new SimpleEntry<>(0, 0),
                new SimpleEntry<>(1, 1),
                new SimpleEntry<>(0, 1),
                new SimpleEntry<>(0, 2)
        );

        // Crear los salmones
        for (SimpleEntry<Integer, Integer> tileCoords : initialTiles) {
            for (Player player : players) {
                Salmon salmon = new Salmon(tileCoords.getKey(), tileCoords.getValue());
                salmon.setPlayer(player);
                salmon.setPair(true);
                salmon.setState(SalmonState.LIVE);
                salmon.setBoard(board);

                switch (player.getColour()) {
                    case RED:
                        salmon.setImage("/ficharoja.png");
                        break;
                    case GREEN:
                        salmon.setImage("/fichaverde.png");
                        break;
                    case PURPLE:
                        salmon.setImage("/fichamorada.png");
                        break;
                    case YELLOW:
                        salmon.setImage("/fichaamarilla.png");
                        break;
                    default:
                        salmon.setImage("/fichaorange.png");
                        break;
                }

                salmons.add(salmonRepository.save(salmon));
            }
        }

        return salmons;
    }

    @Transactional
    public BoardTurnos updateTurno(Board board) {
        BoardTurnos boardTurnos = new BoardTurnos();
        heronRule(board);
        
        List<Colour> ls = new ArrayList<>(Arrays.asList(Colour.values())
                .subList(0, board.getMatch().getPlayers().size()));
        Integer turnoActual = ls.indexOf(board.getTurno());
        Integer turno = turnoActual;
        List<Salmon> salmonList = salmonRepository.findSalmonByBoard(board);
        while (true) {
            turno = (turno + 1) % ls.size();
            final Integer turno2 = turno;
            if (board.getTileCount() > 12 && turno.equals(12%board.getMatch().getPlayers().size())) {
                board.setRoundPass(true);
                
            }
            if (salmonList.stream().anyMatch(salmon -> salmon.getPlayer().getColour().equals(ls.get(turno2)) 
                && salmon.getX() >= 0 && salmon.getY() >= 0 && Tile.getPuntos(salmon.getX(), salmon.getY()) < 1)) {
                    board.setTurno(ls.get(turno));
                    break;
            }
            if (turno.equals(turnoActual)) {
                board.getMatch().setFinish(LocalDateTime.now());
                board.getMatch().setStatus(MatchStatus.FINISHED);
                Integer maxPoints = board.getMatch().getPlayers().stream()
                        .max(Comparator.comparing(Player::getPoints))
                        .map(Player::getPoints)
                        .orElse(0);
                List<Player> lsGanadores = board.getMatch().getPlayers().stream()
                        .filter(player -> player.getPoints() == maxPoints)
                        .toList();
                if (lsGanadores.size() == 1) {
                    board.setWinner(lsGanadores.get(0).getUser());
                    break;
                }
                Integer maxSalmons = lsGanadores.stream()
                        .mapToInt(p->salmonList.stream().filter(salmon -> salmon.getPlayer().
                        equals(p)).mapToInt(salmon-> salmon.isPair() ? 2 : 1).sum()).max().orElse(0);
                lsGanadores = lsGanadores.stream()
                        .filter(player -> salmonList.stream().filter(salmon -> salmon.getPlayer().
                        equals(player)).mapToInt(salmon-> salmon.isPair() ? 2 : 1).sum() == maxSalmons)
                        .toList();
                if (lsGanadores.size() == 1) {
                    board.setWinner(lsGanadores.get(0).getUser());
                    break;
                }
                Integer maxFichasSalmons = lsGanadores.stream()
                        .mapToInt(p->salmonList.stream().filter(salmon -> salmon.getPlayer().
                        equals(p)).mapToInt(salmon-> 1).sum()).max().orElse(0);
                lsGanadores = lsGanadores.stream()
                        .filter(player -> salmonList.stream().filter(salmon -> salmon.getPlayer().
                        equals(player)).mapToInt(salmon-> 1).sum() == maxFichasSalmons)
                        .toList();
                if (lsGanadores.size() == 1) {
                    board.setWinner(lsGanadores.get(0).getUser());
                    break;
                }
                User winner = lsGanadores.get(0).getUser();
                List<Coordenadas> desoveCoordenadas = List.of(new Coordenadas(11, 1), new Coordenadas(11, 0),
                            new Coordenadas(12, 1), new Coordenadas(11, 2));
                for (int y = desoveCoordenadas.size() - 1; y >= 0; y -- ) {
                    final int y2 = y;
                    Integer maxFichaAdelantada = lsGanadores.stream() 
                        .mapToInt(p->salmonList.stream().filter(salmon -> salmon.getPlayer().
                        equals(p) && salmon.getX() == desoveCoordenadas.get(y2).x() && salmon.getY() == desoveCoordenadas.get(y2).y()).mapToInt(salmon-> 1).sum()).max().orElse(0);
                        lsGanadores = lsGanadores.stream()
                        .filter(player -> salmonList.stream().filter(salmon -> salmon.getPlayer().
                        equals(player) && salmon.getX() == desoveCoordenadas.get(y2).x() && salmon.getY() == desoveCoordenadas.get(y2).y()).mapToInt(salmon-> 1).sum() == maxFichaAdelantada)
                        .toList();
                    if (lsGanadores.size() == 1) {
                        winner = lsGanadores.get(0).getUser();                      
                        break;
                    }
                }
                board.setWinner(winner);
                boardRepository.save(board);
                    break; 
            }
        }
        board.getMatch().getPlayers().forEach(player -> player.setMovement(0));
        boardTurnos.setTurno(board.getTurno());
        boardTurnos.setTurnoTile(board.getTurnoTile());
        boardRepository.save(board);
        if (board.getWinner() != null) {
            statisticsService.updateStatistics(board);
        }
        return boardTurnos;
    }
    
    @Transactional
    public BoardTurnos updateTurnoTile(Board board) {
        BoardTurnos boardTurnos = new BoardTurnos();
        List<Colour> ls = new ArrayList<>(Arrays.asList(Colour.values())
                .subList(0, board.getMatch().getPlayers().size()));
        board.setTurnoTile(ls.get((ls.indexOf(board.getTurnoTile()) + 1) % ls.size()));
        board.getMatch().getPlayers().forEach(player -> player.setMovement(0));
        boardTurnos.setTurno(board.getTurno());
        boardTurnos.setTurnoTile(board.getTurnoTile());
        boardRepository.save(board);
        return boardTurnos;
    }

    @Transactional
    public void heronRule(Board board) {
        Salmon salmon1 = null;
        List<Salmon> salmons = salmonRepository.findSalmonByBoard(board);
        for (Salmon salmon : salmons) {
            Player player = salmon.getPlayer();
            if((player.getMovement() % 5 == 0) && player.getColour().equals(board.getTurno()) 
            && tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), board.getId()).getType().equals(TileType.HERON)){
                if (salmon1 == null) {
                    salmon1 = salmon;
                    break;
                }
            }
        }
        if(salmon1 != null){
            heronRule1(salmon1);
        }
    }

    @Transactional
    public void heronRule1(Salmon salmon){
        if(salmon.isPair()){
            salmon.setPair(false);
            if (salmon.getPlayer().getColour().equals(Colour.RED)) {
                salmon.setImage("/ficharoja_unico.png");
            } else if (salmon.getPlayer().getColour().equals(Colour.GREEN)) {
                salmon.setImage("/fichaverde_unico.png");
            } else if (salmon.getPlayer().getColour().equals(Colour.PURPLE)) {
                salmon.setImage("/fichamorada_unico.png");
            } else if (salmon.getPlayer().getColour().equals(Colour.YELLOW)) {
                salmon.setImage("/fichaamarilla_unico.png");
            } else {
                salmon.setImage("/fichaorange_unico.png");
            }
            salmonRepository.save(salmon);
        }else{
            tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId())
            .setCapacity(tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId()).getCapacity() + 1);
            tileService.save(tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId()));
            salmonRepository.delete(salmon);
        }
    }

    @Transactional
    public void validateTilePlacement(Integer boardId, int x, int y) {
        Board board = getBoardById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        int tileCount = board.getTileCount();
        int setIndex; // Calcula el índice del conjunto permitido
        if (y == 1) {
            setIndex = tileCount / 3 + 2;
        } else {
            setIndex = tileCount / 3 + 1;
        }
        if (x != setIndex) {
            throw new IllegalArgumentException("Quedan posiciones por rellenar con losetas");
        }
    }

    @Transactional
    public List<Board> getFriendsBoards(){
        return boardRepository.findBoardsByUserFriends(userService.findCurrentUser());
    }
}
