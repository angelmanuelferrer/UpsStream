package es.us.dp1.l4_04_24_25.Upstream.friends;

import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Invitation extends BaseEntity{


    @ManyToOne
    private User sendUser;

    @ManyToOne
    private User receiveUser;

}
