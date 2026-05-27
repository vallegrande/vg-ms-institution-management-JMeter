package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Classroom;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.out.IClassroomRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UpdateClassroomUseCaseImplTest {

    @Mock
    private IClassroomRepository classroomRepository;

    @InjectMocks
    private UpdateClassroomUseCaseImpl updateClassroomUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ShouldUpdate() {
        Classroom existing = Classroom.builder().id("1").classroomName("Old").build();
        Classroom updated = Classroom.builder().classroomName("New").build();

        when(classroomRepository.findById("1")).thenReturn(Mono.just(existing));
        when(classroomRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(updateClassroomUseCase.execute("1", updated))
                .assertNext(res -> {
                    org.junit.jupiter.api.Assertions.assertEquals("New", res.getClassroomName());
                })
                .verifyComplete();
    }
}
