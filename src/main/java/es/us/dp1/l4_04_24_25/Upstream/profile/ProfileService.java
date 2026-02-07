package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;
@Service
public class ProfileService {

    private ProfileRepository profileRepository;
    @Autowired
    public ProfileService(ProfileRepository profileRepository){
        this.profileRepository=profileRepository;
    }
    @Transactional
    public Profile save(Profile p){
        return profileRepository.save(p);
    }
    @Transactional
    public TimeStatistics findStatistics(String username ){
        List<Match> partidasJugadas=profileRepository.partidasJugadas(username);
        TimeStatistics statistics=new TimeStatistics();
        statistics.setAvg(partidasJugadas.stream().mapToDouble(m->ChronoUnit.SECONDS.between(m.getStart(),m.getFinish())).average().orElse(0.));
        statistics.setSum(partidasJugadas.stream().mapToInt(m->(int)ChronoUnit.SECONDS.between(m.getStart(),m.getFinish())).sum());
        return statistics;
    }
}
