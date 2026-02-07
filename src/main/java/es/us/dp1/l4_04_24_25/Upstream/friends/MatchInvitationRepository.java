package es.us.dp1.l4_04_24_25.Upstream.friends;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.us.dp1.l4_04_24_25.Upstream.user.User;

public interface  MatchInvitationRepository extends CrudRepository<MatchInvitation, Integer> {
    
    @Query("SELECT i FROM MatchInvitation i WHERE i.receiveUser = :user")
    List<MatchInvitation> findMatchInvitationsByUser(@Param("user") User user);
    
}
