package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.adapters.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Classroom;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.entities.ClassroomEntity;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.mappers.ClassroomPersistenceMapper;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.repositories.ClassroomR2dbcRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClassroomRepositoryImplTest {

    @Mock
    private ClassroomR2dbcRepository r2dbcRepository;

    @Mock
    private ClassroomPersistenceMapper mapper;

    @InjectMocks
    private ClassroomRepositoryImpl classroomRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldReturnDomain() {
        Classroom c = Classroom.builder().id("f47ac10b-58cc-4372-a567-0e02b2c3d479").build();
        ClassroomEntity entity = ClassroomEntity.builder().build();

        when(mapper.toEntity(any())).thenReturn(entity);
        when(r2dbcRepository.save(any())).thenReturn(Mono.just(entity));
        when(mapper.toDomain(any())).thenReturn(c);

        StepVerifier.create(classroomRepository.save(c))
                .expectNext(c)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnDomain() {
        String id = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Classroom c = Classroom.builder().id(id).build();
        ClassroomEntity entity = ClassroomEntity.builder().build();

        when(r2dbcRepository.findById(UUID.fromString(id))).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(c);

        StepVerifier.create(classroomRepository.findById(id))
                .expectNext(c)
                .verifyComplete();
    }
}
