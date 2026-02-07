package es.us.dp1.l4_04_24_25.Upstream.tile;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.salmon.SalmonRepository;

@Service
public class TileService {

    private final TileRepository tileRepository;
    private final SalmonRepository salmonRepository;

    @Autowired
    public TileService(TileRepository tileRepository, SalmonRepository salmonRepository) {
        this.tileRepository = tileRepository;
        this.salmonRepository = salmonRepository;
    }

    @Transactional
    public void initializeTile(Board board) {
        Tile t1 = new Tile();
        t1.setCapacity(Integer.MAX_VALUE);
        t1.setType(TileType.DESOVE);
        t1.setImage("/desoveizquierda.png");
        t1.setX(11);
        t1.setY(0);
        t1.setBoard(board);
        save(t1);

        Tile t2 = new Tile();
        t2.setCapacity(Integer.MAX_VALUE);
        t2.setType(TileType.DESOVE);
        t2.setImage("/desoveabajo.png");
        t2.setX(11);
        t2.setY(1);
        t2.setBoard(board);
        save(t2);

        Tile t3 = new Tile();
        t3.setCapacity(Integer.MAX_VALUE);
        t3.setType(TileType.DESOVE);
        t3.setImage("/desovederecha.png");
        t3.setX(11);
        t3.setY(2);
        t3.setBoard(board);
        save(t3);

        Tile t4 = new Tile();
        t4.setCapacity(Integer.MAX_VALUE);
        t4.setType(TileType.DESOVE);
        t4.setImage("/desovearriba.png");
        t4.setX(12);
        t4.setY(1);
        t4.setBoard(board);
        save(t4);

        Tile t5 = new Tile();
        t5.setCapacity(0);
        t5.setType(TileType.WATER);
        t5.setImage("/loseta_agua.png");
        t5.setX(0);
        t5.setY(0);
        t5.setBoard(board);
        save(t5);

        Tile t6 = new Tile();
        t6.setCapacity(0);
        t6.setType(TileType.WATER);
        t6.setImage("/loseta_agua.png");
        t6.setX(0);
        t6.setY(1);
        t6.setBoard(board);
        save(t6);

        Tile t7 = new Tile();
        t7.setCapacity(0);
        t7.setType(TileType.WATER);
        t7.setImage("/loseta_agua.png");
        t7.setX(1);
        t7.setY(1);
        t7.setBoard(board);
        save(t7);

        Tile t8 = new Tile();
        t8.setCapacity(0);
        t8.setType(TileType.WATER);
        t8.setImage("/loseta_agua.png");
        t8.setX(0);
        t8.setY(2);
        t8.setBoard(board);
        save(t8);
    }

    @Transactional(readOnly = true)
    public List<Tile> getAllTile() {
        return tileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tile getTileById(Integer id) {
        Optional<Tile> t = tileRepository.getTileById(id);
        return t.isEmpty() ? null : t.get();
    }

    @Transactional
    public Tile save(Tile t) {
        return tileRepository.save(t);
    }

    @Transactional
    public void delete(Integer id) {
        tileRepository.deleteById(id);
    }

    @Transactional
    public Tile createTile(Board currentBoard, Tile tileRequest) {
        Tile tile = new Tile();
        tile.setBoard(currentBoard);
        tile.setTileStack(tileRequest.getTileStack());
        tile.setCapacity(tileRequest.getCapacity());
        tile.setRotation(tileRequest.getRotation());
        tile.setImage(tileRequest.getImage());
        tile.setType(tileRequest.getType());
        tile.setX(tileRequest.getX());
        tile.setY(tileRequest.getY());
        return save(tile);
    }

    @Transactional
    public void moveSalmonToTile(Integer salmonId, Integer tileId) {
        Optional<Salmon> so = salmonRepository.getSalmonById(salmonId);
        Optional<Tile> to = tileRepository.getTileById(tileId);
        if (so.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException("Salmón o loseta no encontrados");
        }
        if (to.get().getCapacity() > to.get().getSalmons().size()) {
            Tile t = to.get();
            Salmon s = so.get();
            Optional<Tile> tileWithSalmon = tileRepository.findTileBySalmonId(salmonId);
            if (tileWithSalmon.isPresent()) {
                Tile tWS = tileWithSalmon.get();
                List<Salmon> ls = tWS.getSalmons();
                ls.removeIf(x -> x.getId().equals(salmonId));
                tWS.setSalmons(ls);
                save(tWS);
            }
            List<Salmon> sal = t.getSalmons();
            sal.add(s);
            t.setSalmons(sal);
            save(t);
        } else {
            throw new IllegalArgumentException("La loseta no tiene espacio suficiente");
        }
    }

    @Transactional
    public Tile getTileByCoordenadas(Integer x, Integer y, Integer boardId){
        Optional<Tile> t = tileRepository.getTileByCoordenadas(x, y, boardId);
        return t.orElseThrow(() -> new ResourceNotFoundException("No se encuentra ninguna ficha en esas coordenadas"));
    }

    @Transactional(readOnly = true)
    public List<Tile> getTilesByBoardId(Integer id) {
        return tileRepository.getTilesByBoardId(id);
    }


}
