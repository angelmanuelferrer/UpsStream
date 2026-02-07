package es.us.dp1.l4_04_24_25.Upstream.chat;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Message extends BaseEntity {
    
    @ManyToOne
    private Board board;
    
    private String message;

    @ManyToOne
    private Player player;
}
