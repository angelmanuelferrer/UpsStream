package es.us.dp1.l4_04_24_25.Upstream.tile;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.tileStack.TileStack;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Tile extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "tileStack_id")
    private TileStack tileStack;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TileType type;

    @NotNull
    @PositiveOrZero
    Integer capacity;

    private String image;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private int x;

    private int y;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "tile_id")
    private List<Salmon> salmons = new ArrayList<>();

    public void addSalmon(Salmon salmon){
        this.salmons.add(salmon);
    }

    public void removeSalmon(Salmon salmon) {
        this.salmons.remove(salmon);
    } 

    public static Integer getPuntos(Integer x, Integer y){
        if(x==11 && y==1){
            return 1;
        }else if(x==11 && y==0){
            return 2;
        }else if(x==12 && y==1){
            return 3;
        }else if(x==11 && y==2){
            return 9;
    }
    else {
        return 0;
    }
    }
    
    @Max(360)
    @Min(0)
    private Integer rotation;
}
