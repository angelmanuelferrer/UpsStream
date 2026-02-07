package es.us.dp1.l4_04_24_25.Upstream.salmon;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;



@Repository
public interface SalmonRepository extends CrudRepository<Salmon, Integer> {
        List<Salmon> findAll();

        Optional<Salmon> getSalmonById(Integer id);
        List<Salmon> findSalmonByBoard (Board board); 
        List<Salmon> findSalmonByPlayer (Player player);

        @Query("SELECT s FROM Salmon s WHERE s.x = :x AND s.y = :y AND s.board = :board")
        List<Salmon> findSalmonByTile(@Param("x") int x, @Param("y") int y, @Param("board") Board board);
        
}
