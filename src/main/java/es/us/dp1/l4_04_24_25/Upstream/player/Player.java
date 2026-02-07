package es.us.dp1.l4_04_24_25.Upstream.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
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
public class Player extends BaseEntity{
    
    @Enumerated(EnumType.STRING)
    private Colour colour;
    
    private int movement;
    private int points;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    private Match match;
}


