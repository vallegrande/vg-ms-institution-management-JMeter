package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.exceptions.ClassroomNotFoundException;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Classroom;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.ClassroomStatus;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.out.IClassroomRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class GetClassroomUseCaseImplTest {

    @Mock
    private IClassroomRepository classroomRepository;

    @InjectMocks
    private GetClassroomUseCaseImpl getClassroomUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnFluxOfClassrooms() {
        Classroom c = Classroom.builder().id("1").build();
        when(classroomRepository.findAll()).thenReturn(Flux.just(c));

        StepVerifier.create(getClassroomUseCase.findAll())
                .expectNext(c)
                .verifyComplete();
    }

    @Test
    void findById_WhenExists_ShouldReturnClassroom() {
        Classroom c = Classroom.builder().id("1").build();
        when(classroomRepository.findById("1")).thenReturn(Mono.just(c));

        StepVerifier.create(getClassroomUseCase.findById("1"))
                .expectNext(c)
                .verifyComplete();
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(classroomRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(getClassroomUseCase.findById("1"))
                .expectError(ClassroomNotFoundException.class)
                .verify();
    }

    @Test
    void findByStatus_ShouldReturnFlux() {
        Classroom c = Classroom.builder().status(ClassroomStatus.ACTIVE).build();
        when(classroomRepository.findByStatus(ClassroomStatus.ACTIVE)).thenReturn(Flux.just(c));

        StepVerifier.create(getClassroomUseCase.findByStatus(ClassroomStatus.ACTIVE))
                .expectNext(c)
                .verifyComplete();
    }
}
