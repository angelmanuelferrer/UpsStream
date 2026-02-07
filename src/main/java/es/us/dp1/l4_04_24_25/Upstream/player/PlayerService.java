package es.us.dp1.l4_04_24_25.Upstream.player;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;


@Service
public class PlayerService {
    
    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly=true)
    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    @Transactional
    public Player save(Player p){
        return playerRepository.save(p);
    }

    @Transactional
    public void delete(Integer id) {
        playerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Player getPlayerById(Integer id){
        return playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("player", "id", id));
    }

    @Transactional(readOnly = true)
    public int getMovementByPlayerId(Integer id){
        return playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("player", "id", id)).getMovement();
    }
}
