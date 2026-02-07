package es.us.dp1.l4_04_24_25.Upstream.statistics;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.us.dp1.l4_04_24_25.Upstream.user.User;

public interface  StatisticsRepository extends CrudRepository<Statistics, Integer> {
    List<Statistics> findAll();
    List<Statistics> findByName (String name);
    Statistics findByUser (User user);
    
}
