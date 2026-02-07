package es.us.dp1.l4_04_24_25.Upstream.profile;

import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.FilterType;
import es.us.dp1.l4_04_24_25.Upstream.configuration.SecurityConfiguration;

@WebMvcTest(
    controllers = SagaController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = WebSecurityConfigurer.class
    ),
    excludeAutoConfiguration = SecurityConfiguration.class
)
public class SagaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SagaService sagaService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SagaController sagaController;

    @Test
    @WithMockUser
    public void testFindAll() throws Exception {
        Saga saga1 = new Saga();
        saga1.setId(1);
        saga1.setName("Saga1");

        Saga saga2 = new Saga();
        saga2.setId(2);
        saga2.setName("Saga2");

        List<Saga> sagas = Arrays.asList(saga1, saga2);
        when(sagaService.findAll()).thenReturn(sagas);

        mockMvc.perform(get("/api/v1/saga"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(sagas.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Saga1"));
    }
    @WithMockUser
    @Test
    public void testFindById() throws Exception {
        Saga saga = new Saga();
        saga.setId(1);
        saga.setName("Saga1");
        when(sagaService.findById(1)).thenReturn(saga);

        mockMvc.perform(get("/api/v1/saga/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Saga1"));
    }

    @Test
    @WithMockUser(roles="admin")
    public void testCreate() throws Exception {
        Saga saga = new Saga();
        saga.setName("New Saga");

        Saga savedSaga = new Saga();
        //savedSaga.setId(1);
        savedSaga.setName("New Saga");
        when(sagaService.save(any(Saga.class))).thenReturn(savedSaga);

        mockMvc.perform(post("/api/v1/saga")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
               
                .content(objectMapper.writeValueAsString(saga)))
                
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Saga"));
    }

    @Test
    @WithMockUser(roles="admin")
    public void testEdit() throws Exception {
        Saga saga = new Saga();
        saga.setId(1);
        saga.setName("Updated Saga");
        when(sagaService.save(any(Saga.class))).thenReturn(saga);

        mockMvc.perform(put("/api/v1/saga/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saga)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Saga"));
    }

    @Test
    @WithMockUser(roles="admin")
    public void testDelete() throws Exception {
        doNothing().when(sagaService).delete(1);

        mockMvc.perform(delete("/api/v1/saga/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}