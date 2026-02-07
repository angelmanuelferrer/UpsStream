package es.us.dp1.l4_04_24_25.Upstream.profile;

import es.us.dp1.l4_04_24_25.Upstream.configuration.SecurityConfiguration;
import es.us.dp1.l4_04_24_25.Upstream.user.User;
import es.us.dp1.l4_04_24_25.Upstream.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = ProfileController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = WebSecurityConfigurer.class
    ),
    excludeAutoConfiguration = SecurityConfiguration.class
)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void testGetProfileByUsername_UserExists() throws Exception {
        String username = "testUser";
        User mockUser = new User();
        Profile mockProfile = new Profile();
        mockUser.setProfile(mockProfile);

        when(userService.findUser(username)).thenReturn(mockUser);

        mockMvc.perform(get("/api/profiles/by-username/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(userService, times(1)).findUser(username);
    }

    @Test
    @WithMockUser
    public void testGetProfileByUsername_UserNotFound() throws Exception {
        String username = "nonExistentUser";

        when(userService.findUser(username)).thenReturn(null);

        mockMvc.perform(get("/api/profiles/by-username/{username}", username))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findUser(username);
    }

    @Test
    @WithMockUser
    public void testGetTimeStatisticsByUsername_UserExists() throws Exception {
        String username = "testUser";
        User mockUser = new User();
        TimeStatistics mockStatistics = new TimeStatistics();

        when(userService.findUser(username)).thenReturn(mockUser);
        when(profileService.findStatistics(username)).thenReturn(mockStatistics);

        mockMvc.perform(get("/api/profiles/by-username/statistics/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(userService, times(1)).findUser(username);
        verify(profileService, times(1)).findStatistics(username);
    }

    @Test
    @WithMockUser
    public void testGetTimeStatisticsByUsername_UserNotFound() throws Exception {
        String username = "nonExistentUser";

        when(userService.findUser(username)).thenReturn(null);

        mockMvc.perform(get("/api/profiles/by-username/statistics/{username}", username))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findUser(username);
        verify(profileService, times(0)).findStatistics(username);
    }

    @Test
    @WithMockUser
    public void testUpdateProfile_Success() throws Exception {
        User mockUser = new User();
        Profile existingProfile = new Profile();
        Profile updatedProfile = new Profile();
        updatedProfile.setBio("Updated bio");

        mockUser.setProfile(existingProfile);

        when(userService.findCurrentUser()).thenReturn(mockUser);
        when(profileService.save(existingProfile)).thenReturn(updatedProfile);

        mockMvc.perform(put("/api/profiles")
                .with(csrf())  // importante para PUT
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bio\":\"Updated bio\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("Updated bio"));

        verify(userService, times(1)).findCurrentUser();
        verify(profileService, times(1)).save(existingProfile);
    }
}