package es.us.dp1.l4_04_24_25.Upstream.profile;

import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Profile findByUser(User user);
    @Query("select m from Match m join m.players p2 where p2.user.username=:username and m.finish is not null")
    List<Match> partidasJugadas(String username);
  
}
