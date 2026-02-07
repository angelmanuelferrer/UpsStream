package es.us.dp1.l4_04_24_25.Upstream.profile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;




class SagaServiceTest {

    @Mock
    private SagaRepository repository;

    @InjectMocks
    private SagaService sagaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Saga> mockSagas = Arrays.asList(new Saga(), new Saga());
        when(repository.findAllSaga()).thenReturn(mockSagas);

        // Act
        List<Saga> result = sagaService.findAll();

        // Assert
        assertEquals(mockSagas.size(), result.size());
        verify(repository, times(1)).findAllSaga();
    }

    @Test
    void testSave() {
        // Arrange
        Saga saga = new Saga();
        when(repository.save(saga)).thenReturn(saga);

        // Act
        Saga result = sagaService.save(saga);

        // Assert
        assertNotNull(result);
        verify(repository, times(1)).save(saga);
    }

    @Test
    void testDelete() {
        // Arrange
        int sagaId = 1;
        Saga saga = new Saga();
        when(repository.findById(sagaId)).thenReturn(Optional.of(saga));

        // Act
        sagaService.delete(sagaId);

        // Assert
        verify(repository, times(1)).delete(saga);
        verify(repository, times(1)).findById(sagaId);
    }

    @Test
    void testFindById_Success() {
        // Arrange
        int sagaId = 1;
        Saga saga = new Saga();
        when(repository.findById(sagaId)).thenReturn(Optional.of(saga));

        // Act
        Saga result = sagaService.findById(sagaId);

        // Assert
        assertNotNull(result);
        assertEquals(saga, result);
        verify(repository, times(1)).findById(sagaId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        int sagaId = 1;
        when(repository.findById(sagaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> sagaService.findById(sagaId));
        verify(repository, times(1)).findById(sagaId);
    }
}