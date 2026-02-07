package es.us.dp1.l4_04_24_25.Upstream.match;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.l4_04_24_25.Upstream.model.NamedEntity;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Match extends NamedEntity {
    
    private String code;
    private LocalDateTime start;
    private LocalDateTime finish;
    private Integer numberOfPlayers;
    

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "match_id")
    private List<Player> players; 

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}