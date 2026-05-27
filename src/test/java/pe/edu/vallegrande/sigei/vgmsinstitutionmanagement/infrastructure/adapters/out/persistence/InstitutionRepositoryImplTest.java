package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.adapters.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.entities.InstitutionEntity;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.mappers.InstitutionPersistenceMapper;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.repositories.InstitutionR2dbcRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InstitutionRepositoryImplTest {

    @Mock
    private InstitutionR2dbcRepository r2dbcRepository;

    @Mock
    private InstitutionPersistenceMapper mapper;

    @InjectMocks
    private InstitutionRepositoryImpl institutionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldReturnDomain() {
        Institution inst = Institution.builder().id("f47ac10b-58cc-4372-a567-0e02b2c3d479").build();
        InstitutionEntity entity = InstitutionEntity.builder().build();

        when(mapper.toEntity(any())).thenReturn(entity);
        when(r2dbcRepository.save(any())).thenReturn(Mono.just(entity));
        when(mapper.toDomain(any())).thenReturn(inst);

        StepVerifier.create(institutionRepository.save(inst))
                .expectNext(inst)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnDomain() {
        String id = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Institution inst = Institution.builder().id(id).build();
        InstitutionEntity entity = InstitutionEntity.builder().build();

        when(r2dbcRepository.findById(UUID.fromString(id))).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(inst);

        StepVerifier.create(institutionRepository.findById(id))
                .expectNext(inst)
                .verifyComplete();
    }
}
