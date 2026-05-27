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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteClassroomUseCaseImplTest {

    @Mock
    private IClassroomRepository classroomRepository;

    @InjectMocks
    private DeleteClassroomUseCaseImpl deleteClassroomUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_WhenExists_ShouldDeactivate() {
        Classroom c = Classroom.builder().id("1").status(ClassroomStatus.ACTIVE).build();
        when(classroomRepository.findById("1")).thenReturn(Mono.just(c));
        when(classroomRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(deleteClassroomUseCase.execute("1"))
                .assertNext(res -> {
                    org.junit.jupiter.api.Assertions.assertEquals(ClassroomStatus.INACTIVE, res.getStatus());
                    org.junit.jupiter.api.Assertions.assertNotNull(res.getDeletedAt());
                })
                .verifyComplete();
    }

    @Test
    void execute_WhenNotExists_ShouldThrowException() {
        when(classroomRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(deleteClassroomUseCase.execute("1"))
                .expectError(ClassroomNotFoundException.class)
                .verify();
    }
}
