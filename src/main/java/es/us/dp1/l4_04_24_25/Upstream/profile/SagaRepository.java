package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface SagaRepository extends CrudRepository<Saga,Integer>{
    @Query("select s from Saga s")    
    public List<Saga> findAllSaga();
    
}