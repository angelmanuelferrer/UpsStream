package es.us.dp1.l4_04_24_25.Upstream.tileStack;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TileStackRepository extends CrudRepository<TileStack,Integer> {
    List<TileStack> findAll();

    Optional<TileStack> getTileStackById(Integer id);

    @Query("SELECT ts FROM TileStack ts WHERE ts.board.id = :id")
    List<TileStack> getTileStackByBoardId(Integer id);
    
}
