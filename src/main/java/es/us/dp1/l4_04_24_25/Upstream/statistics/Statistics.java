package es.us.dp1.l4_04_24_25.Upstream.statistics;

import es.us.dp1.l4_04_24_25.Upstream.model.NamedEntity;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Statistics extends NamedEntity {
    
    private Integer matchesPlayed = 0;
    
    private Integer matchesWon = 0;
    
    private Integer matchesLost = 0;
    
    private Integer points = 0;

    @OneToOne
    private User user;
}
