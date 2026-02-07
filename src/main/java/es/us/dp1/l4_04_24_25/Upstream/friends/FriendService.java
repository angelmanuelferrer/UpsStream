package es.us.dp1.l4_04_24_25.Upstream.friends;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.AccessDeniedException;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchService;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserRepository;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

@Service
public class FriendService {

    
    private UserRepository userRepository;
    private UserService userService;
    private InvitationRepository invitationRepository;
    private MatchInvitationRepository matchInvitationRepository;
    private MatchService matchService;

    @Autowired
    public FriendService(UserRepository userRepository, UserService userService, InvitationRepository invitationRepository, 
    MatchInvitationRepository matchInvitationRepository, MatchService matchService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.invitationRepository = invitationRepository;
        this.matchInvitationRepository = matchInvitationRepository;
        this.matchService = matchService;
    }

    public Optional<User> findUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void addFriend(Integer userId, Integer friendId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<User> friendOpt = userRepository.findById(friendId);

        if (userOpt.isPresent() && friendOpt.isPresent()) {
            User user = userOpt.get();
            User friend = friendOpt.get();

            user.addFriend(friend); 
            userRepository.save(user);
            userRepository.save(friend); 
        } else {
            throw new IllegalArgumentException("Usuario o amigo no encontrado");
        }
    }

    public void removeFriend(Integer friendId) {
        User user = userService.findCurrentUser();
        Optional<User> friendOpt = userRepository.findById(friendId);

        if (friendOpt.isPresent()) {
            User friend = friendOpt.get();
            user.removeFriend(friend); 
            userRepository.save(user);
            userRepository.save(friend); 
        } else {
            throw new IllegalArgumentException("Usuario o amigo no encontrado");
        }
    }

    public void createRequest(String userName){
        Invitation inv = new Invitation();
        inv.setSendUser(userService.findCurrentUser());
        inv.setReceiveUser(findUserByUsername(userName));
        invitationRepository.save(inv);
    }

    public Set<User> findFriends() {
        return userService.findCurrentUser().getFriends();
    }

    public List<Invitation> findInvitations() {
        return invitationRepository.findInvitationsByUser(userService.findCurrentUser());
    }

    public void acceptInvitation(Integer invitationId) {
        Invitation inv = invitationRepository.findById(invitationId).orElseThrow(() -> new ResourceNotFoundException("invitation", "id", invitationId));
        User user = userService.findCurrentUser();
        if (inv.getReceiveUser().equals(user)) {
            user.addFriend(inv.getSendUser());
            userRepository.save(user);
            userRepository.save(inv.getSendUser());
            invitationRepository.delete(inv);
        } else {
            throw new AccessDeniedException("No tienes permiso para aceptar esta invitación");
        }
    }

    public void declineInvitation(Integer invitationId) {
        Invitation inv = invitationRepository.findById(invitationId).orElseThrow(() -> new ResourceNotFoundException("invitation", "id", invitationId));
        User user = userService.findCurrentUser();
        if (inv.getReceiveUser().equals(user)) {
            invitationRepository.delete(inv);
        } else {
            throw new AccessDeniedException("No tienes permiso para aceptar esta invitación");
        }
    }

    public List<MatchInvitation> findMatchInvitations() {
        return matchInvitationRepository.findMatchInvitationsByUser(userService.findCurrentUser());
    }

    public void acceptMatchInvitation(Integer invitationId) {
        MatchInvitation inv = matchInvitationRepository.findById(invitationId).orElseThrow(() -> new ResourceNotFoundException("invitation", "id", invitationId));
        User user = userService.findCurrentUser();
        if (inv.getReceiveUser().equals(user)) {
            matchService.addPlayerToMatch(inv.getMatch().getId(), user, inv.getId());
            matchInvitationRepository.delete(inv);
        } else {
            throw new AccessDeniedException("No tienes permiso para aceptar esta invitación");
        }
    }

    public void declineMatchInvitation(Integer invitationId) {
        MatchInvitation inv = matchInvitationRepository.findById(invitationId).orElseThrow(() -> new ResourceNotFoundException("invitation", "id", invitationId));
        User user = userService.findCurrentUser();
        if (inv.getReceiveUser().equals(user)) {
            matchInvitationRepository.delete(inv);
        } else {
            throw new AccessDeniedException("No tienes permiso para aceptar esta invitación");
        }
    }

    public void createMatchRequest(String userName, Integer matchId){
        MatchInvitation inv = new MatchInvitation();
        inv.setSendUser(userService.findCurrentUser());
        inv.setReceiveUser(findUserByUsername(userName));
        inv.setMatch(matchService.getMatchById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId)));
        matchInvitationRepository.save(inv);
    }

    public void delete(Integer id) {
        invitationRepository.deleteById(id);
    }
}
