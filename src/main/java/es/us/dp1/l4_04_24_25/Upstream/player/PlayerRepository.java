package es.us.dp1.l4_04_24_25.Upstream.player;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    List<Player> findAll();
}
