package es.us.dp1.l4_04_24_25.Upstream.chat;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;

public interface ChatRepository extends CrudRepository<Message, Integer> {
    List<Message> findByBoard(Board board); 
    List<Message> findByPlayer(Player player); 

    
}
