package es.us.dp1.l4_04_24_25.Upstream.friends;

import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class MatchInvitation extends BaseEntity {

    @ManyToOne
    private User sendUser;

    @ManyToOne
    private User receiveUser;
    
    @ManyToOne
    private Match match;
    
}
