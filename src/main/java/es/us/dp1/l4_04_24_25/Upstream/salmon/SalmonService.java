package es.us.dp1.l4_04_24_25.Upstream.salmon;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.player.PlayerService;
import es.us.dp1.l4_04_24_25.Upstream.tile.Tile;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileRepository;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileService;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileType;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

@Service
public class SalmonService {

    private final SalmonRepository salmonRepository;
    private final BoardService boardService;
    private final TileService tileService;
    private final PlayerService playerService;
    private final TileRepository tileRepository;
    private final UserService userService;

    @Autowired
    public SalmonService(SalmonRepository salmonRepository, BoardService boardService, TileService tileService,
            PlayerService playerService, TileRepository tileRepository,UserService userService){
        this.salmonRepository = salmonRepository;
        this.boardService = boardService;
        this.tileService = tileService;
        this.playerService = playerService;
        this.tileRepository = tileRepository;
        this.userService=userService;
    }

    @Transactional(readOnly = true)
    public List<Salmon> getAllSalmons() {
        return salmonRepository.findAll();
    }

    @Transactional
    public Salmon save(Salmon s) {
        return salmonRepository.save(s);
    }

    @Transactional
    public void delete(Integer id) {
        salmonRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Salmon getSalmonById(Integer salmonId) {
        Optional<Salmon> salmonOptional = salmonRepository.findById(salmonId);
        return salmonOptional.orElseThrow(() -> new ResourceNotFoundException("salmon", "id", salmonId));
    }

    @Transactional(readOnly = true)
    public List<Salmon> getSalmonByBoard(Integer boardId) {
        return salmonRepository.findSalmonByBoard(boardService.getBoardById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId)));
    }

    @Transactional
    public boolean setSwim(Salmon salmonOrigin, Tile swimDestination) {
        if (swimDestination == null) {
            System.out.println("Invalid swim destination.");
            return false; // Si la loseta de destino es inválida, no hace nada
        }

        // Actualizar la posición del salmón a la de la loseta de destino
        salmonOrigin.setX(swimDestination.getX());
        salmonOrigin.setY(swimDestination.getY());

        return true;
    }

    @Transactional
    public boolean setJump(Salmon salmonOrigin, Tile jumpDestination) {
        if (jumpDestination == null) {
            System.out.println("Invalid jump destination.");
            return false; // Si la loseta de destino es inválida, no hace nada
        }

        // Actualizar la posición del salmón a la de la loseta de destino
        salmonOrigin.setX(jumpDestination.getX());
        salmonOrigin.setY(jumpDestination.getY());

        return true;
    }

    @Transactional
    public boolean adyacente(Salmon salmon, Tile destinationTile) {
        // Comprobación de si la loseta de destino es adyacente

        boolean isAdjacent = false;
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        // Los movimientos válidos son aquellos en los que las coordenadas
        // (x, y) de la loseta de destino son adyacentes a las del salmón.
        if (salmon.getY() == 1 && salmon.getX() != 11 && salmon.getX() != 12) {
            isAdjacent = ((salmon.getX() - targetX == -1 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 1 && salmon.getX() == targetX)); // Movimiento vertical
        } else if ((salmon.getY() == 0 || salmon.getY() == 2) && salmon.getX() == 10) {
            isAdjacent = (targetX == 11 && targetY == 1);
        } else if (salmon.getY() == 1 && salmon.getX() == 11) {
            isAdjacent = (targetX == 11 && targetY == 0);
        } else if (salmon.getY() == 0 && salmon.getX() == 11) {
            isAdjacent = (targetX == 12 && targetY == 1);
        } else if (salmon.getY() == 1 && salmon.getX() == 12) {
            isAdjacent = (targetX == 11 && targetY == 2);

        } else {
            isAdjacent = ((salmon.getX() - targetX == -1 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 1 && salmon.getX() - targetX == -1)); // Movimiento
                                                                                                   // vertical

        }
        return isAdjacent;
    }

    @Transactional
    public boolean isCapacitable(Tile destinationTile, Salmon salmon) {
        int capacity = destinationTile.getCapacity();
        boolean res = true;
        if (capacity <= 0) {
            throw new IllegalStateException("No pueden haber más salmones en la loseta.");
        }
        if (destinationTile.getType() == TileType.ROCK) {
            res = capacity > (salmon.getBoard().getMatch().getPlayers().size()) -
                    (salmon.getBoard().getMatch().getPlayers().size() - 1);

            if (capacity <= (salmon.getBoard().getMatch().getPlayers().size()) -
                    (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                throw new IllegalStateException(
                        "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
            }
        }
        return res;
    }

    @Transactional
    public void losetasSinColocar(Salmon salmon) {
        Integer count = salmon.getBoard().getTileCount();
        if ((salmon.getBoard().isSalmonMoved() && salmon.getBoard().getTurno().equals(Arrays.stream(Colour.values())
                .filter(c -> c.ordinal() == 12 % salmon.getBoard().getMatch().getNumberOfPlayers()).findFirst().get())
                || salmon.getBoard().getRoundPass())
                && count < 29) {
            throw new IllegalStateException("Tienes que colocar las 3 losetas antes de mover un salmón.");
        }
    }

    @Transactional
    public void conteoMasQueDoce(int count) {
        if (count < 12) {
            throw new IllegalStateException("El board debe tener al menos 12 losetas para poder mover salmones.");
        }
    }

    @Transactional
    public boolean canSwimToTile(Salmon salmon, Tile destinationTile) {
        Integer count = salmon.getBoard().getTileCount();
        // Comprobación de si la loseta de destino es adyacente
        boolean isAdjacent = adyacente(salmon, destinationTile);
        boolean isBlocked = false;
        losetasSinColocar(salmon);// Compruebo si se han colocado las losetas
        conteoMasQueDoce(count);// Compruebo si hay más de 12 losetas
        isCapacitable(destinationTile, salmon); // Si la capacidad no es válida no puede nadar

        Tile originTile = tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId());
        if ((destinationTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL)
                && isAdjacent) {
            isBlocked = isBlockedByTrunk(destinationTile, salmon.getX(), salmon.getY());
        }
        if (Tile.getPuntos(destinationTile.getX(), destinationTile.getY()) > 1) {
            throw new IllegalStateException("No puedes nadar a una loseta del desove.");
        }
        if (originTile != null && !isBlocked && isAdjacent) { // Si el movimiento ya no es válido no compruebo si está
                                                              // bloqueado
            if (originTile.getType() == TileType.BEAR || originTile.getType() == TileType.WATERFALL) {
                isBlocked = isBlockedInsedeTrunk(originTile, destinationTile);
            }
        }
        if (isBlocked) {
            isAdjacent = false;
        }

        return isAdjacent;
    }

    @Transactional
    public boolean isBlockedByBear(Tile bearTile, int xSal, int ySal) { // Comprueba si la loseta a la que me desplazo
                                                                        // es un
        // oso y sus troncos me impiden el paso
        int bearX = bearTile.getX();
        int bearY = bearTile.getY();
        int rotation = bearTile.getRotation();
        boolean isBlocked = false;
        if (rotation == 0) {
            if (bearY == ySal && bearX == xSal + 1) {
                isBlocked = true;
            } else if (bearX == xSal + 1 && bearY == ySal + 1 && bearY == 1) {
                isBlocked = true;
            } else if (bearX == xSal && bearY == ySal + 1 && bearY == 2) {
                isBlocked = true;
            }
        } else if (rotation == 60) {
            if (bearY == ySal + 1) {
                isBlocked = true;
            }
        } else if (rotation == 300) {
            if (bearY == ySal - 1) {
                isBlocked = true;
            } else if (bearY == ySal && bearX == xSal + 1) {
                isBlocked = true;
            }
        } else if (rotation == 240) {
            if (bearY == ySal - 1) {
                isBlocked = true;
            }
        }
        return isBlocked; // Movimiento permitido
    }

    @Transactional
    public boolean isBlockedInsedeBear(Tile bearTile, Tile destinationTile) { // Comprueba si la loseta desde la que me
                                                                              // desplazo es un oso y sus troncos me
                                                                              // impiden el paso
        int bearY = bearTile.getY();
        int rotation = bearTile.getRotation();
        boolean isBlocked = false;
        if (rotation == 60) {
            if (bearY == destinationTile.getY() + 1 || bearY == destinationTile.getY() + 2) {
                isBlocked = true;
            }
        } else if (rotation == 120) {
            if (bearY == destinationTile.getY()) {
                isBlocked = true;
            } else if (bearY == destinationTile.getY() + 1 || bearY == destinationTile.getY() + 2) {
                isBlocked = true;
            }
        } else if (rotation == 180) {
            if (bearY == destinationTile.getY() - 1 || bearY == destinationTile.getY() - 2) {
                isBlocked = true;
            } else if (bearY == destinationTile.getY()) {
                isBlocked = true;
            }
        } else if (rotation == 240) {
            if (bearY == destinationTile.getY() - 1 || bearY == destinationTile.getY() - 2) {
                isBlocked = true;
            }
        }
        return isBlocked; // Movimiento permitido
    }

    @Transactional
    public boolean isBlockedByWaterWaterfall(Tile destinationTile, int xSal, int ySal) { // Compruebo si la loseta a la
                                                                                         // que me desplazo es una
                                                                                         // cascada y sus troncos me
                                                                                         // impiden el paso
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        int rotation = destinationTile.getRotation();
        boolean isBlocked = false;

        if (rotation == 60) {
            if (targetY + 1 == ySal) {
                isBlocked = true;
            }
        } else if (rotation == 120) {
            if (targetY + 1 == ySal) {
                isBlocked = true;
            } else if (targetY == ySal && targetX == xSal + 1) {
                isBlocked = true;
            }
        } else if (rotation == 180) {
            isBlocked = true;
        } else if (rotation == 240) {
            if (targetY == ySal + 1) {
                isBlocked = true;
            } else if (targetY == ySal && targetX == xSal + 1) {
                isBlocked = true;
            }
        } else if (rotation == 300) {
            if (targetY == ySal + 1) {
                isBlocked = true;
            }
        }

        return isBlocked;
    }

    @Transactional
    public boolean isBlockedInsedeWaterfall(Tile WaterfallTile, Tile destinationTile) {// Compruebo si estoy dentro de
                                                                                       // una cascada y sus troncos me
                                                                                       // impiden el paso
        int waterfallY = WaterfallTile.getY();
        int rotation = WaterfallTile.getRotation();
        int targetY = destinationTile.getY();
        boolean isBlocked = false;
        if (rotation == 0) {
            isBlocked = true;
        } else if (rotation == 60) {
            if (!(targetY + 1 == waterfallY)) {
                isBlocked = true;
            }
        } else if (rotation == 120) {
            if (targetY == waterfallY + 1 || targetY == waterfallY + 2) {
                isBlocked = true;
            }
        } else if (rotation == 240) {
            if (targetY + 1 == waterfallY) {
                isBlocked = true;
            }
        } else if (rotation == 300) {
            if (!(targetY == waterfallY + 1)) {
                isBlocked = true;
            }
        }
        return isBlocked;
    }

    @Transactional
    public boolean isBlockedByTrunk(Tile destinationTile, int xSal, int ySal) { // Compruebo si en la loseta a la que me
                                                                                // desplazo tiene troncos que me impiden
                                                                                // el paso
        boolean isBlocked = false;
        if (destinationTile.getType() == TileType.BEAR) {
            isBlocked = isBlockedByBear(destinationTile, xSal, ySal);
        }
        if (destinationTile.getType() == TileType.WATERFALL && !isBlocked) {
            isBlocked = isBlockedByWaterWaterfall(destinationTile, xSal, ySal);
        }
        return isBlocked;
    }

    @Transactional
    public boolean isBlockedInsedeTrunk(Tile trunkTile, Tile destinationTile) { // Compruebo si la loseta en la que
                                                                                // estoy tiene troncos que me impiden el
                                                                                // paso
        boolean isBlocked = false;
        if (trunkTile.getType() == TileType.BEAR) {
            isBlocked = isBlockedInsedeBear(trunkTile, destinationTile);
        }
        if (trunkTile.getType() == TileType.WATERFALL) {
            isBlocked = isBlockedInsedeWaterfall(trunkTile, destinationTile);
        }
        return isBlocked;
    }

    @Transactional(readOnly = true)
    public Tile getIntermediateTile(Tile originTile, Tile destinationTile) {
        int x1 = originTile.getX();
        int y1 = originTile.getY();
        int x2 = destinationTile.getX();
        int y2 = destinationTile.getY();

        // Determinar las coordenadas intermedias basadas en la disposición del tablero
        int intermediateX;
        int intermediateY;

        // Ajustar la lógica para la fila desplazada
        if (y1 == y2) {
            // Si están en la misma fila, la Y no cambia
            intermediateY = y1;
            intermediateX = (x1 + x2) / 2;
        } else {
            // Si hay un salto diagonal
            intermediateX = x2;
            intermediateY = 1;
        }
        // Buscar y devolver la loseta intermedia
        return tileRepository.getTileByCoordenadas(intermediateX, intermediateY, originTile.getBoard().getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra ninguna ficha en esas coordenadas."));
    }

    @Transactional
    public boolean canJumpToTile(Salmon salmon, Tile destinationTile) {
        // Comprobación de si la loseta de destino es adyacente
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        int movement = salmon.getPlayer().getMovement();
        int count = salmon.getBoard().getTileCount();
        boolean isBlockedDestination = false;
        boolean isBlockedOrigin = false;
        Tile intermediateTile = null;
        Tile originTile = tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId());
        boolean distanciaValida = false;
        boolean canJump = true;
        boolean isAdjacent;
        isCapacitable(destinationTile, salmon);// Si la capacidad no es válida no puede saltar
        losetasSinColocar(salmon);// Compruebo si se han colocado las losetas
        conteoMasQueDoce(count);// Compruebo si hay más de 12 losetas
        isAdjacent = adyacente(salmon, destinationTile);
        if (!isAdjacent) {
            intermediateTile = getIntermediateTile(originTile, destinationTile);
        }
        if (movement % 5 == 4) {
            throw new IllegalStateException("No tienes suficientes movimientos para saltar");
        }

        if (Tile.getPuntos(destinationTile.getX(), destinationTile.getY()) > 1) {
            throw new IllegalStateException("No puedes nadar a una loseta del desove.");
        }

        // Los movimientos válidos son aquellos en los que las coordenadas
        // (x, y) de la loseta de destino son adyacentes a las del salmón.
        // Movimiento vertical

        if ((salmon.getY() == 0 || salmon.getY() == 2) && salmon.getX() == 9) {
            distanciaValida = false;
        } else if (salmon.getY() == 1 && salmon.getX() == 10) {
            distanciaValida = false;
        } else {
            distanciaValida = ((salmon.getX() - targetX == -2 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 2 && salmon.getX() - targetX == -1)); // Movimiento
                                                                                                   // vertical
        }

        if (isAdjacent) {// Comprobamos si no estas usando el salto para desplazar dos, si no si lo estas
                         // usando para saltar un tronco, en ese caso tienes que poder saltar
            if (destinationTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL) {
                isBlockedDestination = isBlockedByTrunk(destinationTile, salmon.getX(), salmon.getY());
            }
            if (originTile != null) { // Si el movimiento ya no es válido no compruebo si
                                      // está bloqueado
                if (originTile.getType() == TileType.BEAR || originTile.getType() == TileType.WATERFALL) {
                    isBlockedOrigin = isBlockedInsedeTrunk(originTile, destinationTile);
                }
            }

            if (!(isBlockedDestination && isBlockedOrigin) && (isBlockedDestination || isBlockedOrigin)) {
                distanciaValida = true;
            }
        } else if (intermediateTile != null && canJump) { // Compruebo que en el salto no haya una loseta intermedia en
                                                          // la que sus
            // troncos me impidan el paso
            if (destinationTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL) {
                if (isBlockedByTrunk(destinationTile, intermediateTile.getX(), intermediateTile.getY())) {
                    canJump = false;
                }
                if (originTile != null && !canJump) { // Si el movimiento ya no es válido no compruebo si
                                                      // está bloqueado
                    if (originTile.getType() == TileType.BEAR || originTile.getType() == TileType.WATERFALL) {
                        if (isBlockedInsedeTrunk(originTile, intermediateTile)) {
                            canJump = false;
                        }
                    }
                }
            } else {
                if (originTile.getType() == TileType.BEAR || originTile.getType() == TileType.WATERFALL) {
                    if (isBlockedInsedeTrunk(originTile, destinationTile)) {
                        distanciaValida = false;
                    }
                }
            }
            if ((intermediateTile.getType() == TileType.BEAR || intermediateTile.getType() == TileType.WATERFALL)
                    && canJump) {// si ya esta bloqueado no lo compruebo
                if (isBlockedByTrunk(intermediateTile, salmon.getX(), salmon.getY())) {// Compruebo si la loseta
                                                                                       // intermedia me bloquea
                    canJump = false;
                } else if (isBlockedInsedeTrunk(intermediateTile, destinationTile)) {
                    canJump = false;
                }
            }
        }
        return canJump && distanciaValida; // canJump comprueba que no haya ninguna regla de negocio que te impida el
                                           // salto y distanciaValida que la distancia sea la correcta
    }

    public static record Coordenadas(Integer x, Integer y) {
    }

    @Transactional
    public boolean canMoveToAnyTile(Player player, boolean dead) {
        List<Salmon> salmons = salmonRepository.findSalmonByPlayer(player);
        int nSalmon = 0;
        boolean salmonNuevo = false;
        boolean canMove = false;
        for (Salmon s : salmons) {
            salmonNuevo = true;
            for (Integer x = s.getX(); x <= s.getX() + 2; x++) {
                for (Integer y = s.getY() - 2; y <= s.getY() + 2; y++) {
                    Tile destinationTile = tileRepository.getTileByCoordenadas(x, y, s.getBoard().getId()).orElse(null);
                    if (destinationTile == null) {
                        continue;
                    }
                    try {
                        if (canSwimToTile(s, destinationTile)) {
                            if (salmonNuevo) {
                                salmonNuevo = false;
                                nSalmon++;
                            }
                            canMove = true;
                        }
                    } catch (IllegalStateException | ResourceNotFoundException e) {
                    }
                    if (player.getMovement() > 1) {
                        try {
                            if (canJumpToTile(s, destinationTile)) {
                                if (salmonNuevo) {
                                    salmonNuevo = false;
                                    nSalmon++;
                                }
                                canMove = true;
                            }
                        } catch (IllegalStateException | ResourceNotFoundException e) {
                        }
                    }
                }
            }
        }
        if(nSalmon==1 && dead) {
            canMove = false;
        }
        return canMove;
    }

    public boolean swim(Salmon salmon, Tile destinationTile) {
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        boolean dead = false;
        Tile destino = tileService.getTileByCoordenadas(targetX, targetY, salmon.getBoard().getId());
        Tile origen = tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId());
        boardService.checkTime(userService.findCurrentUser(),destinationTile.getBoard());
        // Verificar si el salmón puede nadar a la loseta de destino.
        if (canSwimToTile(salmon, destinationTile)) {
            Player player = salmon.getPlayer();
            Integer mult = salmon.isPair() ? 2 : 1;
            player.setPoints(player.getPoints() + Tile.getPuntos(destinationTile.getX(), destinationTile.getY())
                    - Tile.getPuntos(salmon.getX(), salmon.getY()));
            if (Tile.getPuntos(destinationTile.getX(), destinationTile.getY()).equals(1)) {
                player.setPoints(player.getPoints() + (mult));
            }

            playerService.save(player);
            setSwim(salmon, destinationTile);
            player.setMovement(salmon.getPlayer().getMovement() + 1);

            origen.setCapacity(origen.getCapacity() + 1);
            destino.setCapacity(destino.getCapacity() - 1);

            updatePosition(salmon.getId(), destino);
            if (destinationTile.getType() == TileType.EAGLE) {
                destinationTile.setType(TileType.WATER); // Voltear la loseta a agua
                destinationTile.setImage("/loseta_agua.png");
                if (salmon.isPair()) {
                    salmon.setPair(false);
                    if (player.getColour().equals(Colour.RED)) {
                        salmon.setImage("/ficharoja_unico.png");
                    } else if (player.getColour().equals(Colour.GREEN)) {
                        salmon.setImage("/fichaverde_unico.png");
                    } else if (player.getColour().equals(Colour.PURPLE)) {
                        salmon.setImage("/fichamorada_unico.png");
                    } else if (player.getColour().equals(Colour.YELLOW)) {
                        salmon.setImage("/fichaamarilla_unico.png");
                    } else {
                        salmon.setImage("/fichaorange_unico.png");
                    }

                    salmonRepository.save(salmon);
                } else {
                    destino.setCapacity(destino.getCapacity() + 1);
                    dead = true;
                }
                tileService.save(destinationTile);
            }

            Colour ronda = Arrays.stream(Colour.values())
                    .filter(c -> c.ordinal() == 12 % salmon.getBoard().getMatch().getNumberOfPlayers()).findFirst()
                    .get();
            if (player.getMovement() % 5 == 0 || !salmonRepository.findSalmonByPlayer(player).stream()
                    .anyMatch(s -> s.getX() >= 0 && s.getY() >= 0 && Tile.getPuntos(s.getX(), s.getY()) < 1)
                    || !canMoveToAnyTile(player, dead)) {
                boardService.updateTurno(salmon.getBoard());
                Board board = salmon.getBoard();
                board.setSalmonMoved(true);
                boardService.save(board);

                if (salmon.getBoard().getTileCount() >= 15 && board.getRoundPass()) {
                    if (board.getTileCount() >= 29) {
                        board.setRoundPass(false);
                    }
                    List<Tile> lstile = tileService.getTilesByBoardId(salmon.getBoard().getId());
                    Integer minX = lstile.stream().map(Tile::getX).min(Integer::compare).get();
                    for (Tile t : lstile) {
                        if (t.getX() == minX && (t.getY() != 1 || minX == 0)) {
                            salmonRepository.findSalmonByTile(t.getX(), t.getY(), t.getBoard())
                                    .forEach(s -> delete(s.getId()));
                            tileService.delete(t.getId());
                        }
                    }
                    Tile tile = lstile.stream().filter(t -> t.getX() == minX + 1 && t.getY() == 1).findFirst().get();
                    salmonRepository.findSalmonByTile(tile.getX(), tile.getY(), tile.getBoard())
                            .forEach(s -> delete(s.getId()));
                    tileService.delete(tile.getId());
                    List<Coordenadas> desoveCoordenadas = List.of(new Coordenadas(11, 1), new Coordenadas(11, 0),
                            new Coordenadas(12, 1), new Coordenadas(11, 2));
                    for (Salmon s : salmonRepository.findSalmonByBoard(salmon.getBoard())) {
                        Coordenadas c = new Coordenadas(s.getX(), s.getY());
                        Integer index = desoveCoordenadas.indexOf(c);
                        if (desoveCoordenadas.contains(c) && !index.equals(desoveCoordenadas.size() - 1)) {
                            Tile desove1 = tileService.getTileByCoordenadas(desoveCoordenadas.get(index).x(),
                                    desoveCoordenadas.get(index).y(), s.getBoard().getId());
                            Tile desove2 = tileService.getTileByCoordenadas(desoveCoordenadas.get(index + 1).x(),
                                    desoveCoordenadas.get(index + 1).y(), s.getBoard().getId());
                            s.getPlayer().setPoints(
                                    s.getPlayer().getPoints() + Tile.getPuntos(desove2.getX(), desove2.getY())
                                            - Tile.getPuntos(desove1.getX(), desove1.getY()));
                            desove1.getSalmons().remove(s);
                            desove2.getSalmons().add(s);
                            playerService.save(s.getPlayer());
                            tileService.save(desove1);
                            tileService.save(desove2);
                            s.setX(desoveCoordenadas.get(index + 1).x());
                            s.setY(desoveCoordenadas.get(index + 1).y());
                            salmonRepository.save(s);
                        }
                    }
                }
            }
            if (dead) {
                delete(salmon.getId());
            }
            return true;
        }
        return false;
    }

    public boolean jump(Salmon salmon, Tile destinationTile) {
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        boolean dead = false;
        Tile destino = tileService.getTileByCoordenadas(targetX, targetY, salmon.getBoard().getId());
        Tile origen = tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId());
        boardService.checkTime(userService.findCurrentUser(),destinationTile.getBoard());
        // Verificar si el salmón puede saltar a la loseta de destino.
        if (canJumpToTile(salmon, destinationTile)) {
            Player player = salmon.getPlayer();
            Integer mult = salmon.isPair() ? 2 : 1;
            player.setPoints(player.getPoints() + Tile.getPuntos(destinationTile.getX(), destinationTile.getY())
                    - Tile.getPuntos(salmon.getX(), salmon.getY()));
            if (Tile.getPuntos(destinationTile.getX(), destinationTile.getY()).equals(1)) {
                player.setPoints(player.getPoints() + (mult));
            }
            playerService.save(player);
            setJump(salmon, destinationTile);
            player.setMovement(salmon.getPlayer().getMovement() + 2);

            origen.setCapacity(origen.getCapacity() + 1);
            destino.setCapacity(destino.getCapacity() - 1);

            updatePosition(salmon.getId(), destino);

            // Regla para loseta de oso
            if (destinationTile.getType() == TileType.BEAR) {
                if (salmon.isPair()) {
                    salmon.setPair(false);
                    if (player.getColour().equals(Colour.RED)) {
                        salmon.setImage("/ficharoja_unico.png");
                    } else if (player.getColour().equals(Colour.GREEN)) {
                        salmon.setImage("/fichaverde_unico.png");
                    } else if (player.getColour().equals(Colour.PURPLE)) {
                        salmon.setImage("/fichamorada_unico.png");
                    } else if (player.getColour().equals(Colour.YELLOW)) {
                        salmon.setImage("/fichaamarilla_unico.png");
                    } else {
                        salmon.setImage("/fichaorange_unico.png");
                    }

                    salmonRepository.save(salmon);
                } else {
                    destino.setCapacity(destino.getCapacity() + 1);
                    dead = true;
                }
            }

            if (player.getMovement() % 5 == 0 || !salmonRepository.findSalmonByPlayer(player).stream()
                    .anyMatch(s -> s.getX() >= 0 && s.getY() >= 0 && Tile.getPuntos(s.getX(), s.getY()) < 1)
                    || !canMoveToAnyTile(player,dead)) {
                boardService.updateTurno(salmon.getBoard());
                Board board = salmon.getBoard();
                board.setSalmonMoved(true);
                boardService.save(board);

                if (salmon.getBoard().getTileCount() >= 15 && board.getRoundPass()) {
                    if (board.getTileCount() >= 29) {
                        board.setRoundPass(false);
                    }
                    List<Tile> lstile = tileService.getTilesByBoardId(salmon.getBoard().getId());
                    Integer minX = lstile.stream().map(Tile::getX).min(Integer::compare).get();
                    for (Tile t : lstile) {
                        if (t.getX() == minX && (t.getY() != 1 || minX == 0)) {
                            salmonRepository.findSalmonByTile(t.getX(), t.getY(), t.getBoard())
                                    .forEach(s -> delete(s.getId()));
                            tileService.delete(t.getId());
                        }
                    }
                    Tile tile = lstile.stream().filter(t -> t.getX() == minX + 1 && t.getY() == 1).findFirst().get();
                    salmonRepository.findSalmonByTile(tile.getX(), tile.getY(), tile.getBoard())
                            .forEach(s -> delete(s.getId()));
                    tileService.delete(tile.getId());
                    List<Coordenadas> desoveCoordenadas = List.of(new Coordenadas(11, 1), new Coordenadas(11, 0),
                            new Coordenadas(12, 1), new Coordenadas(11, 2));
                    for (Salmon s : salmonRepository.findSalmonByBoard(salmon.getBoard())) {
                        Coordenadas c = new Coordenadas(s.getX(), s.getY());
                        Integer index = desoveCoordenadas.indexOf(c);
                        if (desoveCoordenadas.contains(c) && !index.equals(desoveCoordenadas.size() - 1)) {
                            Tile desove1 = tileService.getTileByCoordenadas(desoveCoordenadas.get(index).x(),
                                    desoveCoordenadas.get(index).y(), s.getBoard().getId());
                            Tile desove2 = tileService.getTileByCoordenadas(desoveCoordenadas.get(index + 1).x(),
                                    desoveCoordenadas.get(index + 1).y(), s.getBoard().getId());

                            s.getPlayer().setPoints(
                                    s.getPlayer().getPoints() + Tile.getPuntos(desove2.getX(), desove2.getY())
                                            - Tile.getPuntos(desove1.getX(), desove1.getY()));
                            desove1.getSalmons().remove(s);
                            desove2.getSalmons().add(s);
                            playerService.save(s.getPlayer());
                            tileService.save(desove1);
                            tileService.save(desove2);
                            s.setX(desoveCoordenadas.get(index + 1).x());
                            s.setY(desoveCoordenadas.get(index + 1).y());
                            salmonRepository.save(s);
                        }
                    }
                }
            }
            if (dead) {
                delete(salmon.getId());
            }
            return true;
        }

        return false;
    }

    @Transactional
    public boolean updatePosition(Integer salmonId, Tile destinationTile) {
        Optional<Salmon> salmonOptional = salmonRepository.findById(salmonId);
        if (salmonOptional.isPresent()) {
            Salmon salmon = salmonOptional.get();
            salmon.setX(destinationTile.getX());
            salmon.setY(destinationTile.getY());
            salmonRepository.save(salmon);
            List<Salmon> salmons = destinationTile.getSalmons();
            salmons.add(salmon);
            destinationTile.setSalmons(salmons);
            tileService.save(destinationTile);
            return true;
        }
        return false;
    }
}
