package es.us.dp1.l4_04_24_25.Upstream.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

@Service
public class StatisticsService {

    StatisticsRepository statisticsRepository;
    UserService userService;

    @Autowired
    public StatisticsService (StatisticsRepository sr, UserService userService){
        this.statisticsRepository = sr;
        this.userService = userService;
    }

    @Transactional(readOnly = true) 
    List<Statistics> getStatistics(){
        return statisticsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Statistics getStatisticsByName(String name){
        return (Statistics) statisticsRepository.findByName(name);
    }

    @Transactional
    public void updateStatistics(Board board){
        for (Player player : board.getMatch().getPlayers()){
            Statistics statistics = statisticsRepository.findByUser(player.getUser());
        if (statistics == null) {
            statistics = new Statistics();
            statistics.setUser(player.getUser()); 
            }

            statistics.setMatchesPlayed(statistics.getMatchesPlayed() + 1);
            if (board.getWinner().equals(player.getUser())){
                statistics.setMatchesWon(statistics.getMatchesWon() + 1);
            } else {
                statistics.setMatchesLost(statistics.getMatchesLost() + 1);
            }
            statistics.setPoints(statistics.getPoints() + player.getPoints());
            statisticsRepository.save(statistics);
        }
    }
}
