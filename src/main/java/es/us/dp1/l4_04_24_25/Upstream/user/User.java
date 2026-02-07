package es.us.dp1.l4_04_24_25.Upstream.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.model.BaseEntity;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.profile.Profile;
import es.us.dp1.l4_04_24_25.Upstream.statistic.Achievement;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "appusers")
public class User extends BaseEntity {

    @Column(unique = true)
    String username;

    String password;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "authority")
    Authorities authority;

    public Boolean hasAuthority(String auth) {
        return authority.getAuthority().equals(auth);
    }

    public Boolean hasAnyAuthority(String... authorities) {
        Boolean cond = false;
        for (String auth : authorities) {
            if (auth.equals(authority.getAuthority())) {
                cond = true;
            }
        }
        return cond;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Player> players;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Match> matches;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Achievement> achievements;

    // Método para añadir una partida a la lista del User
    public void addMatch(Match match) {
        matches.add(match);
        match.setUser(this);  // Asegura que el Match también conozca al User
    }

    // Método para eliminar una partida de la lista del User
    public void removeMatch(Match match) {
        matches.remove(match);
    }

    // Relación bidireccional para amigos
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnore
    private Set<User> friends = new HashSet<>();

    public void addFriend(User friend) {
        if (!friends.contains(friend) && !friend.equals(this)) {
            friends.add(friend);
            friend.getFriends().add(this); // Relación recíproca
        }
    }

    public void removeFriend(User friend) {
        if (friends.contains(friend)) {
            friends.remove(friend);
            friend.getFriends().remove(this); // Relación recíproca
        }
    }

    @OneToOne
    private Profile profile;


}
