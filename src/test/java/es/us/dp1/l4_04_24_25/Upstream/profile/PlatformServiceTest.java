package es.us.dp1.l4_04_24_25.Upstream.profile;

import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




class PlatformServiceTest {
    @Mock
    private PlatformRepository repository;
    @InjectMocks
    private PlatformService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PlatformRepository.class);
        service = new PlatformService(repository);
    }

    @Test
    void testFindAll() {
        List<Platform> platforms = Arrays.asList(new Platform(), new Platform());
        when(repository.findAllPlatform()).thenReturn(platforms);

        List<Platform> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAllPlatform();
    }

    @Test
    void testSave() {
        Platform platform = new Platform();
        when(repository.save(platform)).thenReturn(platform);

        Platform result = service.save(platform);

        assertNotNull(result);
        verify(repository, times(1)).save(platform);
    }

    @Test
    void testDelete() {
        Platform platform = new Platform();
        platform.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(platform));

        service.delete(1);

        verify(repository, times(1)).delete(platform);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testFindById() {
        Platform platform = new Platform();
        platform.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(platform));

        Platform result = service.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testFindByIdThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
        verify(repository, times(1)).findById(1);
    }
}