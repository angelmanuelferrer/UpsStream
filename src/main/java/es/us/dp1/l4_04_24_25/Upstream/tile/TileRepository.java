package es.us.dp1.l4_04_24_25.Upstream.tile;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface TileRepository extends CrudRepository<Tile,Integer> {
    
    List<Tile> findAll();

    Optional<Tile> getTileById(Integer id);

    @Query("SELECT t FROM Tile t JOIN t.salmons s WHERE s.id = :id")
    Optional<Tile> findTileBySalmonId(@Param("id") int id) throws DataAccessException;

    @Query("SELECT t FROM Tile t WHERE t.board.id = :id")
    List<Tile> getTilesByBoardId(Integer id);

    @Query("SELECT t FROM Tile t WHERE t.x = :x AND t.y = :y AND t.board.id = :boardId")
    Optional<Tile> getTileByCoordenadas(@Param("x") int x, @Param("y") int y, @Param("boardId") Integer boardId);
}
