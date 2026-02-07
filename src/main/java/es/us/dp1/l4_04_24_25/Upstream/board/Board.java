package es.us.dp1.l4_04_24_25.Upstream.board;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.player.Colour;
import es.us.dp1.l4_04_24_25.Upstream.tileStack.TileStack;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Board extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @Enumerated(EnumType.STRING)
    private Colour turno;

    @Enumerated(EnumType.STRING)
    private Colour turnoTile;

    @JsonIgnore
    @OneToOne
    private TileStack tileStack;

    @NotNull
    @Min(0)
    private int tileCount;

    private boolean salmonMoved;

    private boolean canRemoveTile;

    @ManyToOne
    private User winner;
    @ManyToOne
    private User userTimeExceeded;

    Boolean roundPass = false;

}