package es.us.dp1.l4_04_24_25.Upstream.profile;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;





class GenderServiceTest {

    @Mock
    private GenderRepository repository;

    @InjectMocks
    private GenderService genderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Gender gender1 = new Gender();
        gender1.setId(1);
        gender1.setName("Male");
        Gender gender2 = new Gender();
        gender2.setId(2);
        gender2.setName("Female");
        List<Gender> mockGenders = Arrays.asList(gender1, gender2);
        when(repository.findAllGender()).thenReturn(mockGenders);

        List<Gender> result = genderService.findAll();

        assertEquals(2, result.size());
        assertEquals("Male", result.get(0).getName());
        assertEquals("Female", result.get(1).getName());
        verify(repository, times(1)).findAllGender();
    }

    @Test
    void testSave() {
        Gender gender = new Gender();
        gender.setId(1);
        gender.setName("Male");
        when(repository.save(gender)).thenReturn(gender);

        Gender result = genderService.save(gender);

        assertNotNull(result);
        assertEquals("Male", result.getName());
        verify(repository, times(1)).save(gender);
    }

    @Test
    void testDelete() {
        Gender gender = new Gender();
        gender.setId(1);
        gender.setName("Male");
        when(repository.findById(1)).thenReturn(Optional.of(gender));

        genderService.delete(1);

        verify(repository, times(1)).delete(gender);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testFindById() {
        Gender gender = new Gender();
        gender.setId(1);
        gender.setName("Male");
        when(repository.findById(1)).thenReturn(Optional.of(gender));

        Gender result = genderService.findById(1);

        assertNotNull(result);
        assertEquals("Male", result.getName());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testFindByIdThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> genderService.findById(1));

        verify(repository, times(1)).findById(1);
    }
}