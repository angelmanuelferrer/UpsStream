package es.us.dp1.l4_04_24_25.Upstream.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import es.us.dp1.l4_04_24_25.Upstream.match.Match;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp1() {
        user = new User();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserByUsername_Success() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findUser(username);

        // Assert
        assertEquals(username, foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindUserByUsername_NotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findUser(username));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindUserById_Success() {
        // Arrange
        Integer id = 1;
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findUser(id);

        // Assert
        assertEquals(id, foundUser.getId());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testFindUserById_NotFound() {
        // Arrange
        Integer id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findUser(id));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testFindCurrentUser_Success() {
        // Arrange
        String username = "authenticatedUser";
        User user = new User();
        user.setUsername(username);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        User currentUser = userService.findCurrentUser();

        // Assert
        assertEquals(username, currentUser.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindCurrentUser_NotAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findCurrentUser());
    }

    @Test
    void testExistsUser() {
        // Arrange
        String username = "testUser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        Boolean exists = userService.existsUser(username);

        // Assert
        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void testFindAll() {
        // Act
        userService.findAll();

        // Assert
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        // Arrange
        Integer idToUpdate = 1;
        User existingUser = new User();
        existingUser.setId(idToUpdate);

        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");

        when(userRepository.findById(idToUpdate)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User result = userService.updateUser(updatedUser, idToUpdate);

        // Assert
        assertEquals("updatedUsername", result.getUsername());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Integer id = 1;
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(id);

        // Assert
        verify(userRepository, times(1)).delete(user);
    }
    @Test
    void testHasAuthority() {
        // Arrange
        Authorities authority = new Authorities();
        authority.setAuthority("ROLE_ADMIN");
        user.setAuthority(authority);

        // Act & Assert
        assertTrue(user.hasAuthority("ROLE_ADMIN"));
        assertFalse(user.hasAuthority("ROLE_USER"));
    }

    @Test
    void testHasAnyAuthority() {
        // Arrange
        Authorities authority = new Authorities();
        authority.setAuthority("ROLE_USER");
        user.setAuthority(authority);

        // Act & Assert
        assertTrue(user.hasAnyAuthority("ROLE_ADMIN", "ROLE_USER"));
        assertFalse(user.hasAnyAuthority("ROLE_GUEST", "ROLE_MANAGER"));

        // Additional Test Cases
        assertFalse(user.hasAnyAuthority()); // No authorities provided
        assertTrue(user.hasAnyAuthority("ROLE_USER")); // Single valid authority
    }

    @Test
    void testAddMatch() {
        // Arrange
        Match match = new Match();
        List<Match> matches = new ArrayList<>();
        user.setMatches(matches);

        // Act
        user.addMatch(match);

        // Assert
        assertEquals(1, user.getMatches().size());
        assertEquals(user, match.getUser());
    }

    @Test
    void testRemoveMatch() {
        // Arrange
        Match match = new Match();
        List<Match> matches = new ArrayList<>();
        matches.add(match);
        user.setMatches(matches);

        // Act
        user.removeMatch(match);

        // Assert
        assertEquals(0, user.getMatches().size());
    }

    @Test
    void testAddFriend() {
        // Arrange
        User friend = new User();
        Set<User> friends = new HashSet<>();
        user.setFriends(friends);

        // Act
        user.addFriend(friend);

        // Assert
        assertTrue(user.getFriends().contains(friend));
        assertTrue(friend.getFriends().contains(user));
    }

    @Test
    void testRemoveFriend() {
        // Arrange
        User friend = new User();
        Set<User> friends = new HashSet<>();
        friends.add(friend);
        user.setFriends(friends);
        friend.setFriends(new HashSet<>(Set.of(user)));

        // Act
        user.removeFriend(friend);

        // Assert
        assertFalse(user.getFriends().contains(friend));
        assertFalse(friend.getFriends().contains(user));
    }
}