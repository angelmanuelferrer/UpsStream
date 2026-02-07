package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface PlatformRepository extends CrudRepository<Platform,Integer>{
    @Query("select g from Platform g")    
    public List<Platform> findAllPlatform();
    
}