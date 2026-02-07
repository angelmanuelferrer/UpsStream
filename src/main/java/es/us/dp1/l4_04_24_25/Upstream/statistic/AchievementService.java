package es.us.dp1.l4_04_24_25.Upstream.statistic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.statistics.Statistics;
import es.us.dp1.l4_04_24_25.Upstream.statistics.StatisticsRepository;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;
import jakarta.validation.Valid;

@Service
public class AchievementService {
        
    private AchievementRepository repo;
    private UserService userService;
    private StatisticsRepository statisticsRepository;

    @Autowired
    public AchievementService(AchievementRepository repo, UserService userService, StatisticsRepository statisticsRepository){
        this.repo=repo;
        this.userService=userService;
        this.statisticsRepository=statisticsRepository;
    }

    @Transactional(readOnly = true)    
    List<Achievement> getAchievements(){
        return repo.findAll();
    }
    
    @Transactional(readOnly = true)    
    public Achievement getById(int id){
        Optional<Achievement> result=repo.findById(id);
        return result.isPresent()?result.get():null;
    }

    @Transactional
    public Achievement saveAchievement(@Valid Achievement newAchievement) {
        return repo.save(newAchievement);
    }

    
    @Transactional
    public void deleteAchievementById(int id){
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Achievement getAchievementByName(String name){
        return repo.findByName(name);
    }

    @Transactional(readOnly = true) 
    public List<AchievementStatus> achivementsProgress(){
        List<Achievement> achievements=repo.findAll();
        Statistics statistic = statisticsRepository.findByUser(userService.findCurrentUser()) == null ? new Statistics() : statisticsRepository.findByUser(userService.findCurrentUser());
        return achievements.stream().map(a -> new AchievementStatus(a, statistic)).toList();
    }

}
