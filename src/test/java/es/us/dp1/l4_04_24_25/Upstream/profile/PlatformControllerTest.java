package es.us.dp1.l4_04_24_25.Upstream.profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.l4_04_24_25.Upstream.configuration.SecurityConfiguration;

@WebMvcTest(controllers = PlatformController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class))
class PlatformControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlatformService platformService;

    @Test
    @WithMockUser
    void testFindAll() throws Exception {
        Platform platform1 = new Platform();
        platform1.setId(1);
        platform1.setName("Platform1");

        Platform platform2 = new Platform();
        platform2.setId(2);
        platform2.setName("Platform2");

        List<Platform> platforms = Arrays.asList(platform1, platform2);
        when(platformService.findAll()).thenReturn(platforms);

        mockMvc.perform(get("/api/v1/platform"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(platforms.size()))
                .andExpect(jsonPath("$[0].id").value(platforms.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(platforms.get(0).getName()));
    }

    @Test
    @WithMockUser
    void testFindById() throws Exception {
        Platform platform = new Platform();
        platform.setId(1);
        platform.setName("Platform1");
        when(platformService.findById(1)).thenReturn(platform);

        mockMvc.perform(get("/api/v1/platform/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(platform.getId()))
                .andExpect(jsonPath("$.name").value(platform.getName()));
    }

    @Test
    @WithMockUser(roles = "admin")
    void testCreate() throws Exception {
        Platform platform = new Platform();
        platform.setId(1);
        platform.setName("Platform1");
        when(platformService.save(any(Platform.class))).thenReturn(platform);

        mockMvc.perform(post("/api/v1/platform")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(platform))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(platform.getId()))
                .andExpect(jsonPath("$.name").value(platform.getName()));
    }

    @Test
    @WithMockUser(roles = "admin")
    void testEdit() throws Exception {
        Platform platform = new Platform();
        platform.setId(1);
        platform.setName("UpdatedPlatform");
        when(platformService.save(any(Platform.class))).thenReturn(platform);

        mockMvc.perform(put("/api/v1/platform/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(platform))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(platform.getId()))
                .andExpect(jsonPath("$.name").value(platform.getName()));
    }

    @Test
    @WithMockUser(roles = "admin")
    void testDelete() throws Exception {
        doNothing().when(platformService).delete(1);

        mockMvc.perform(delete("/api/v1/platform/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}