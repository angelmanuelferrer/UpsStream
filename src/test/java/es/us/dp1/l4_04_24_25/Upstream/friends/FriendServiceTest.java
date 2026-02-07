package es.us.dp1.l4_04_24_25.Upstream.friends;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.l4_04_24_25.Upstream.match.Match;
import es.us.dp1.l4_04_24_25.Upstream.match.MatchService;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserRepository;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @InjectMocks
    private FriendService friendService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private MatchInvitationRepository matchInvitationRepository;

    @Mock
    private MatchService matchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUserById() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = friendService.findUserById(1);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindUserByUsername() {
        User user = new User();
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        User result = friendService.findUserByUsername("username");

        assertEquals(user, result);
    }

    @Test
    public void testAddFriend() {
        User user = new User();
        User friend = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(friend));

        friendService.addFriend(1, 2);

        verify(userRepository).save(user);
        verify(userRepository).save(friend);
    }

    @Test
    public void testRemoveFriend() {
        User user = new User();
        User friend = new User();
        when(userService.findCurrentUser()).thenReturn(user);
        when(userRepository.findById(2)).thenReturn(Optional.of(friend));

        friendService.removeFriend(2);

        verify(userRepository).save(user);
        verify(userRepository).save(friend);
    }

    @Test
    public void testCreateRequest() {
        User user = new User();
        User friend = new User();
        when(userService.findCurrentUser()).thenReturn(user);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(friend));

        friendService.createRequest("username");

        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    public void testFindFriends() {
        User user = mock(User.class);
        Set<User> friends = Set.of(new User());
        when(userService.findCurrentUser()).thenReturn(user);
        when(user.getFriends()).thenReturn(friends);

        Set<User> result = friendService.findFriends();

        assertEquals(friends, result);
    }

    @Test
    public void testFindInvitations() {
        User user = new User();
        List<Invitation> invitations = List.of(new Invitation());
        when(userService.findCurrentUser()).thenReturn(user);
        when(invitationRepository.findInvitationsByUser(user)).thenReturn(invitations);

        List<Invitation> result = friendService.findInvitations();

        assertEquals(invitations, result);
    }

    @Test
    public void testAcceptInvitation() {
        User user = new User();
        User sendUser = new User();
        Invitation invitation = new Invitation();
        invitation.setReceiveUser(user);
        invitation.setSendUser(sendUser);
        when(userService.findCurrentUser()).thenReturn(user);
        when(invitationRepository.findById(1)).thenReturn(Optional.of(invitation));

        friendService.acceptInvitation(1);

        verify(userRepository).save(user);
        verify(userRepository).save(sendUser);
        verify(invitationRepository).delete(invitation);
    }

    @Test
    public void testDeclineInvitation() {
        User user = new User();
        Invitation invitation = new Invitation();
        invitation.setReceiveUser(user);
        when(userService.findCurrentUser()).thenReturn(user);
        when(invitationRepository.findById(1)).thenReturn(Optional.of(invitation));

        friendService.declineInvitation(1);

        verify(invitationRepository).delete(invitation);
    }

    @Test
    public void testFindMatchInvitations() {
        User user = new User();
        List<MatchInvitation> matchInvitations = List.of(new MatchInvitation());
        when(userService.findCurrentUser()).thenReturn(user);
        when(matchInvitationRepository.findMatchInvitationsByUser(user)).thenReturn(matchInvitations);

        List<MatchInvitation> result = friendService.findMatchInvitations();

        assertEquals(matchInvitations, result);
    }

    @Test
    public void testAcceptMatchInvitation() {
        User user = new User();
        Match match = new Match();
        MatchInvitation matchInvitation = new MatchInvitation();
        user.setId(1); 
        match.setId(10);
        matchInvitation.setId(1);
        matchInvitation.setReceiveUser(user);
        matchInvitation.setMatch(match);
        when(userService.findCurrentUser()).thenReturn(user);
        when(matchInvitationRepository.findById(1)).thenReturn(Optional.of(matchInvitation));
        friendService.acceptMatchInvitation(1);
        verify(matchService).addPlayerToMatch(10, user, 1);
        verify(matchInvitationRepository).delete(matchInvitation);
    }

    @Test
    public void testDeclineMatchInvitation() {
        User user = new User();
        MatchInvitation matchInvitation = new MatchInvitation();
        matchInvitation.setReceiveUser(user);
        when(userService.findCurrentUser()).thenReturn(user);
        when(matchInvitationRepository.findById(1)).thenReturn(Optional.of(matchInvitation));

        friendService.declineMatchInvitation(1);

        verify(matchInvitationRepository).delete(matchInvitation);
    }

    @Test
    public void testCreateMatchRequest() {
        User user = new User();
        User friend = new User();
        Match match = new Match();
        when(userService.findCurrentUser()).thenReturn(user);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(friend));
        when(matchService.getMatchById(1)).thenReturn(Optional.of(match));

        friendService.createMatchRequest("username", 1);

        verify(matchInvitationRepository).save(any(MatchInvitation.class));
    }

    @Test
    public void testDelete() {
        Integer id = 1;

        friendService.delete(id);

        verify(invitationRepository).deleteById(id);
    }
}
