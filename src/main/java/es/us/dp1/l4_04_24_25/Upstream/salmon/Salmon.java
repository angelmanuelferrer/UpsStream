package es.us.dp1.l4_04_24_25.Upstream.salmon;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Salmon extends BaseEntity {
    
    private boolean pair;

    private int x;

    private int y; 

    private String image;

    public Salmon(int x , int y){
        this.x = x;
        this.y = y;
    }
       // Constructor sin parámetros (necesario para JPA)
       public Salmon() {}

    @Enumerated(EnumType.STRING)
    private SalmonState state;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}