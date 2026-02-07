package es.us.dp1.l4_04_24_25.Upstream.match;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import java.time.LocalDateTime;
public interface MatchRepository extends CrudRepository<Match, Integer> {
    List<Match> findAll();
    Match findByName(String name);
    @Query("select count(m) from Match m join m.players p where p.user=:user and m.start>=:yesterday")
    public Integer findCountMatchYesterday(User user,LocalDateTime yesterday);
}