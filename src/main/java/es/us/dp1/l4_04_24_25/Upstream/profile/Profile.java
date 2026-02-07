package es.us.dp1.l4_04_24_25.Upstream.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Profile extends BaseEntity{
    

    @OneToOne
    @JsonIgnore
    private User user;

    @Column(length = 1000)
    private String bio;

    @Column(nullable = false)
    private String location;

    private LocalDate birthDate;

    private String avatarUrl;

    private boolean occasionalPlayer;

    @OneToMany
    private List<Gender> favoriteGenres;

    @OneToMany
    private List<Platform> favoritePlatforms;

    @OneToMany
    private List<Saga> favoriteSagas;
}
