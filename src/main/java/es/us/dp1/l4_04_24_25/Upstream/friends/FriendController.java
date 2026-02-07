package es.us.dp1.l4_04_24_25.Upstream.friends;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.l4_04_24_25.Upstream.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Friends", description = "API for the management of Friends")
@SecurityRequirement(name = "bearerAuth")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/friendsInvitation/{friendInvitationId}")
    @Operation(summary = "Añade una invitación de amistad", description = "Añade una invitación de amistad basado en su id.")
    public ResponseEntity<Void> addFriendInvitation(@PathVariable("friendInvitationId") Integer invitationId) {
        friendService.acceptInvitation(invitationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friendsInvitation/{friendInvitationId}")
    @Operation(summary = "Elimina una invitación de amistad", description = "Elimina una invitación de amistad basado en su id.")
    public ResponseEntity<Void> deleteFriendInvitation(@PathVariable("friendInvitationId") Integer invitationId) {
        friendService.declineInvitation(invitationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friends/{friendId}")
    @Operation(summary = "Elimina un amigo", description = "Elimina un amigo basado en su id.")
    public ResponseEntity<?> removeFriend(@PathVariable("friendId") Integer friendId) {
        try {
            friendService.removeFriend(friendId);
            return ResponseEntity.ok("Amigo eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/friends")
    @Operation(summary = "Obtiene todos los amigos", description = "Recupera todos los amigos.")
    public ResponseEntity<Set<User>> getFriends() {
        return new ResponseEntity<>(friendService.findFriends(), HttpStatus.OK);
    }

    @GetMapping("/friendsInvitation")
    @Operation(summary = "Obtiene todas las invitaciones de amistad", description = "Recupera todas las invitaciones de amistad.")
    public ResponseEntity<List<Invitation>> getInvitations() {
        return new ResponseEntity<>(friendService.findInvitations(), HttpStatus.OK);
    }

    @GetMapping("/matchInvitation")
    @Operation(summary = "Obtiene todas las invitaciones partido", description = "Recupera todas las invitaciones de partido.")
    public ResponseEntity<List<MatchInvitation>> getMatchInvitations() {
        return new ResponseEntity<>(friendService.findMatchInvitations(), HttpStatus.OK);
    }

    @PostMapping("/friends/{userName}")
    @Operation(summary = "Crea una invitación de amistad", description = "Crea una invitación de amistad basado en su nombre de usuario.")
    public ResponseEntity<Void> createReq (@PathVariable("userName") String userName) {
        friendService.createRequest(userName);
        return ResponseEntity.ok().build();
    } 

    @PostMapping("/matchInvitation/{userName}/{matchId}")
    @Operation(summary = "Crea una invitación de partido", description = "Crea una invitación de partido basado en su nombre de usuario y el id del partido.")
    public ResponseEntity<Void> createMatchReq (@PathVariable("userName") String userName, @PathVariable("matchId") Integer matchId) {
        friendService.createMatchRequest(userName, matchId);
        return ResponseEntity.ok().build();
    } 

    @PostMapping("/matchInvitation/{matchInvitationId}")
    @Operation(summary = "Añade una invitación de partido", description = "Añade una invitación de partido basado en su id.")
    public ResponseEntity<Void> addMatchInvitation(@PathVariable("matchInvitationId") Integer invitationId) {
        friendService.acceptMatchInvitation(invitationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/matchInvitation/{matchInvitationId}")
    @Operation(summary = "Elimina una invitación de partido", description = "Elimina una invitación de partido basado en su id.")
    public ResponseEntity<Void> deleteMatchInvitation(@PathVariable("matchInvitationId") Integer invitationId) {
        friendService.declineMatchInvitation(invitationId);
        return ResponseEntity.ok().build();
    }

}
