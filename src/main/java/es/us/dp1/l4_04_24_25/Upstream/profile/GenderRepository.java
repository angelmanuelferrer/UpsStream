package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface GenderRepository extends CrudRepository<Gender,Integer>{
    @Query("select g from Gender g")    
    public List<Gender> findAllGender();
    
}