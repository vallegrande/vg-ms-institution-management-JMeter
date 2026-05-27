package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.exceptions.InstitutionNotFoundException;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Classroom;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.out.IClassroomRepository;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.out.IInstitutionRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateClassroomUseCaseImplTest {

    @Mock
    private IClassroomRepository classroomRepository;

    @Mock
    private IInstitutionRepository institutionRepository;

    @InjectMocks
    private CreateClassroomUseCaseImpl createClassroomUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_WhenInstitutionExists_ShouldReturnSavedClassroom() {
        // Arrange
        String institutionId = "inst-123";
        Classroom classroom = Classroom.builder()
                .institutionId(institutionId)
                .classroomName("Aula A")
                .build();
        
        Institution institution = Institution.builder().id(institutionId).build();

        when(institutionRepository.findById(institutionId)).thenReturn(Mono.just(institution));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(Mono.just(classroom));

        // Act & Assert
        StepVerifier.create(createClassroomUseCase.execute(classroom))
                .expectNext(classroom)
                .verifyComplete();

        verify(institutionRepository).findById(institutionId);
        verify(classroomRepository).save(classroom);
    }

    @Test
    void execute_WhenInstitutionDoesNotExist_ShouldThrowException() {
        // Arrange
        String institutionId = "inst-not-found";
        Classroom classroom = Classroom.builder()
                .institutionId(institutionId)
                .build();

        when(institutionRepository.findById(institutionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(createClassroomUseCase.execute(classroom))
                .expectError(InstitutionNotFoundException.class)
                .verify();

        verify(institutionRepository).findById(institutionId);
        verify(classroomRepository, never()).save(any());
    }
}
