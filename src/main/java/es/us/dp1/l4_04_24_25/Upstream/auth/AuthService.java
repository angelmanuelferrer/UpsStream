package es.us.dp1.l4_04_24_25.Upstream.auth;

import java.util.ArrayList;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import es.us.dp1.l4_04_24_25.Upstream.profile.Profile;

import es.us.dp1.l4_04_24_25.Upstream.auth.payload.request.SignupRequest;
import es.us.dp1.l4_04_24_25.Upstream.user.Authorities;
import es.us.dp1.l4_04_24_25.Upstream.user.AuthoritiesService;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;
import es.us.dp1.l4_04_24_25.Upstream.profile.ProfileService;
@Service
public class AuthService {

	private final PasswordEncoder encoder;
	private final AuthoritiesService authoritiesService;
	private final UserService userService;
	//private final PlayerService playerService;
	private final ProfileService profileService;
	

	@Autowired
	public AuthService(PasswordEncoder encoder, AuthoritiesService authoritiesService, UserService userService, ProfileService profileService
			// PlayerService playerService
			) {
		this.encoder = encoder;
		this.authoritiesService = authoritiesService;
		this.userService = userService;
		this.profileService=profileService;
		//this.playerService = ownerService;		
	}

	@Transactional
	public void createUser(@Valid SignupRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		String strRoles = request.getAuthority();
		Authorities role;

		switch (strRoles.toLowerCase()) {
		case "admin":
			role = authoritiesService.findByAuthority("ADMIN");
			user.setAuthority(role);
			userService.saveUser(user);
			break;
		default:
			role = authoritiesService.findByAuthority("PLAYER");
			user.setAuthority(role);
			userService.saveUser(user);
			Profile np = new Profile();
			np.setLocation(request.getLocation());
			np=profileService.save(np);
			user.setProfile(np);
			/*Player player = new Player();
			player.setFirstName(request.getFirstName());
			player.setLastName(request.getLastName());
			player.setAddress(request.getAddress());
			player.setCity(request.getCity());
			player.setTelephone(request.getTelephone());
			player.setUser(user);
			playerService.savePlayer(player);
			*/
		}
	}

}
