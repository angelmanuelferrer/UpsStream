package es.us.dp1.l4_04_24_25.Upstream.profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.l4_04_24_25.Upstream.configuration.SecurityConfiguration;

@WebMvcTest(controllers = GenderController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class))
class GenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenderService genderService;

    @Test
    @WithMockUser
    void testFindAll() throws Exception {
        Gender gender1 = new Gender();
        gender1.setId(1);
        gender1.setName("Male");
        Gender gender2 = new Gender();
        gender2.setId(2);
        gender2.setName("Female");
        when(genderService.findAll()).thenReturn(Arrays.asList(gender1, gender2));

        mockMvc.perform(get("/api/v1/gender"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Male"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Female"));

        verify(genderService, times(1)).findAll();
    }

    @Test
    @WithMockUser
    void testFindById() throws Exception {
        Gender gender = new Gender();
        gender.setId(1);
        gender.setName("Male");
        when(genderService.findById(1)).thenReturn(gender);

        mockMvc.perform(get("/api/v1/gender/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Male"));

        verify(genderService, times(1)).findById(1);
    }

    @Test
    @WithMockUser(roles = "admin")
    void testCreate() throws Exception {
        Gender gender = new Gender();
        gender.setName("Non-Binary");
        Gender savedGender = new Gender();
        savedGender.setId(3);
        savedGender.setName("Non-Binary");
        when(genderService.save(any(Gender.class))).thenReturn(savedGender);

        mockMvc.perform(post("/api/v1/gender")
                .with(csrf())
                .contentType("application/json")
                .content("{\"name\":\"Non-Binary\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Non-Binary"));

        verify(genderService, times(1)).save(any(Gender.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void testEdit() throws Exception {
        Gender gender = new Gender();
        gender.setId(1);
        gender.setName("Updated Gender");
        when(genderService.save(any(Gender.class))).thenReturn(gender);

        mockMvc.perform(put("/api/v1/gender/1")
                .with(csrf())
                .contentType("application/json")
                .content("{\"id\":1,\"name\":\"Updated Gender\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Gender"));

        verify(genderService, times(1)).save(any(Gender.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void testDelete() throws Exception {
        doNothing().when(genderService).delete(1);

        mockMvc.perform(delete("/api/v1/gender/1")
                .with(csrf()))
                .andExpect(status().isOk());

        verify(genderService, times(1)).delete(1);
    }
}