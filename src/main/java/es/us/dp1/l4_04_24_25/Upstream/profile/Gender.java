package es.us.dp1.l4_04_24_25.Upstream.profile;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;

@Entity
@Getter
@Setter
public class Gender extends BaseEntity {
    @NotNull
    String name;
}
