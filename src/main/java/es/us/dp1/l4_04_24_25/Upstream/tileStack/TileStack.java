package es.us.dp1.l4_04_24_25.Upstream.tileStack;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.tile.TileType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class TileStack extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String image;

    @Enumerated(EnumType.STRING)
    private TileType type;

    private Boolean used;  
}