package es.us.dp1.l4_04_24_25.Upstream.match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardRepository;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;
import es.us.dp1.l4_04_24_25.Upstream.player.PlayerRepository;
import es.us.dp1.l4_04_24_25.Upstream.user.User;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository, PlayerRepository playerRepository, BoardRepository boardRepository) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Match> getMatchById(Integer id) {
        return matchRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Match getMatchByName(String name) {
        return matchRepository.findByName(name);
    }

    @Transactional
    public Match save(Match m) {
        return matchRepository.save(m);
    }

    @Transactional
    public void delete(Integer id) {
        matchRepository.deleteById(id);
    }
    @Transactional(readOnly=true)
    public Integer findCountMatchYesterday(User user){
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime yesterday=now.minusDays(1);
        return matchRepository.findCountMatchYesterday(user,yesterday);
    }


    @Transactional
    public void addPlayerToMatch(Integer matchId, User user, Integer playerIndex) {
        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId));

        // Verificar si el número de jugadores ya alcanzó el límite
        if (match.getPlayers().size() >= match.getNumberOfPlayers()) {
            throw new IllegalArgumentException("Cannot join the match. The maximum number of players has been reached.");
        }
        Player player = new Player();
        player.setUser(user);
        player.setMatch(match);
        playerRepository.save(player);
    }

    @Transactional(readOnly = true)
    public List<Player> findPlayersByMatchId(Integer matchId) {
        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId));
        return match.getPlayers();
    }

    @Transactional(readOnly = true)
    public Boolean getMatchStatus(Integer matchId) {
        Optional<Board> board = boardRepository.findBoardByMatchId(matchId);
        return board.isPresent();
    }
}