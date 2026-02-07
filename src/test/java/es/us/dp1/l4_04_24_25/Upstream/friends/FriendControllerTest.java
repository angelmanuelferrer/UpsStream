package es.us.dp1.l4_04_24_25.Upstream.friends;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import es.us.dp1.l4_04_24_25.Upstream.user.User;

@ExtendWith(MockitoExtension.class)
public class FriendControllerTest {

    @Mock
    private FriendService friendService;

    @InjectMocks
    private FriendController friendController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFriendInvitation() {
        Integer invitationId = 1;

        ResponseEntity<Void> response = friendController.addFriendInvitation(invitationId);

        verify(friendService, times(1)).acceptInvitation(invitationId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void testDeleteFriendInvitation() {
        Integer invitationId = 1;

        ResponseEntity<Void> response = friendController.deleteFriendInvitation(invitationId);

        verify(friendService, times(1)).declineInvitation(invitationId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void testRemoveFriendSuccess() {
        Integer friendId = 1;

        ResponseEntity<?> response = friendController.removeFriend(friendId);

        verify(friendService, times(1)).removeFriend(friendId);
        assertEquals(ResponseEntity.ok("Amigo eliminado exitosamente."), response);
    }

    @Test
    public void testRemoveFriendFailure() {
        Integer friendId = 1;
        String errorMessage = "Friend not found";

        doThrow(new IllegalArgumentException(errorMessage)).when(friendService).removeFriend(friendId);

        ResponseEntity<?> response = friendController.removeFriend(friendId);

        verify(friendService, times(1)).removeFriend(friendId);
        assertEquals(ResponseEntity.badRequest().body(errorMessage), response);
    }

    @Test
    public void testGetFriends() {
        Set<User> friends = Set.of(new User(), new User());

        when(friendService.findFriends()).thenReturn(friends);

        ResponseEntity<Set<User>> response = friendController.getFriends();

        verify(friendService, times(1)).findFriends();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(friends, response.getBody());
    }

    @Test
    public void testGetInvitations() {
        List<Invitation> invitations = List.of(new Invitation(), new Invitation());

        when(friendService.findInvitations()).thenReturn(invitations);

        ResponseEntity<List<Invitation>> response = friendController.getInvitations();

        verify(friendService, times(1)).findInvitations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(invitations, response.getBody());
    }

    @Test
    public void testCreateReq() {
        String userName = "testUser";

        ResponseEntity<Void> response = friendController.createReq(userName);

        verify(friendService, times(1)).createRequest(userName);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void testCreateMatchReq() {
        String userName = "testUser";
        Integer matchId = 1;

        ResponseEntity<Void> response = friendController.createMatchReq(userName, matchId);

        verify(friendService, times(1)).createMatchRequest(userName, matchId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void testAddMatchInvitation() {
        Integer invitationId = 1;

        ResponseEntity<Void> response = friendController.addMatchInvitation(invitationId);

        verify(friendService, times(1)).acceptMatchInvitation(invitationId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void testDeleteMatchInvitation() {
        Integer invitationId = 1;

        ResponseEntity<Void> response = friendController.deleteMatchInvitation(invitationId);

        verify(friendService, times(1)).declineMatchInvitation(invitationId);
        assertEquals(ResponseEntity.ok().build(), response);
    }

}
