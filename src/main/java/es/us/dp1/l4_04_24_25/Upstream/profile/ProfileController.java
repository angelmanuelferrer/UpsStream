package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserRepository;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @GetMapping("/by-username/{username}")
    public ResponseEntity<Profile> getProfileByUsername(@PathVariable("username") String username) {
        User user = userService.findUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getProfile());
    }

     @GetMapping("/by-username/statistics/{username}")
    public ResponseEntity<TimeStatistics> getTimeStatisticsByUsername(@PathVariable("username") String username) {
        User user = userService.findUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(profileService.findStatistics(username),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(
           
            @RequestBody Profile updatedProfile
            ) {       
        User currentUser=userService.findCurrentUser();
        Profile existing = currentUser.getProfile();
        existing.setBio(updatedProfile.getBio());
        existing.setLocation(updatedProfile.getLocation());
        existing.setBirthDate(updatedProfile.getBirthDate());
        existing.setAvatarUrl(updatedProfile.getAvatarUrl());
        existing.setFavoriteGenres(updatedProfile.getFavoriteGenres());
        existing.setFavoritePlatforms(updatedProfile.getFavoritePlatforms());
        existing.setFavoriteSagas(updatedProfile.getFavoriteSagas());
        existing.setOccasionalPlayer(updatedProfile.isOccasionalPlayer());

        profileService.save(existing);
        return ResponseEntity.ok(existing);
    }
}
