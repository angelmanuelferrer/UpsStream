package es.us.dp1.l4_04_24_25.Upstream.statistic;

import es.us.dp1.l4_04_24_25.Upstream.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Achievement extends NamedEntity {
    @NotBlank
    private String description;
    private String badgeImage;
    @Min(0)
    private Integer threshold;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private Metric metric;

    public String getActualDescription(){
        return description.replace("<THRESHOLD>",String.valueOf(threshold));
    }
}
