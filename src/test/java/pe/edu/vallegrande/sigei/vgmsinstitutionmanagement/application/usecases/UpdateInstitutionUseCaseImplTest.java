package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.exceptions.InstitutionNotFoundException;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.out.IInstitutionRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UpdateInstitutionUseCaseImplTest {

    @Mock
    private IInstitutionRepository institutionRepository;

    @InjectMocks
    private UpdateInstitutionUseCaseImpl updateInstitutionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_WhenInstitutionExists_ShouldUpdateAndSave() {
        // Arrange
        String id = "inst-1";
        Institution existing = Institution.builder()
                .id(id)
                .name("Old Name")
                .build();
        
        Institution updatedData = Institution.builder()
                .name("New Name")
                .codeInstitution("CODE123")
                .build();

        when(institutionRepository.findById(id)).thenReturn(Mono.just(existing));
        when(institutionRepository.save(any(Institution.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act & Assert
        StepVerifier.create(updateInstitutionUseCase.execute(id, updatedData))
                .assertNext(result -> {
                    assertEquals("New Name", result.getName());
                    assertEquals("CODE123", result.getCodeInstitution());
                    assertNotNull(result.getUpdatedAt());
                })
                .verifyComplete();

        verify(institutionRepository).findById(id);
        verify(institutionRepository).save(any(Institution.class));
    }

    @Test
    void execute_WhenInstitutionDoesNotExist_ShouldThrowException() {
        // Arrange
        String id = "invalid-id";
        when(institutionRepository.findById(id)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(updateInstitutionUseCase.execute(id, Institution.builder().build()))
                .expectError(InstitutionNotFoundException.class)
                .verify();

        verify(institutionRepository).findById(id);
        verify(institutionRepository, never()).save(any());
    }
}
