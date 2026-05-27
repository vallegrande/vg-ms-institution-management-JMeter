package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.exceptions.InstitutionNotFoundException;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.InstitutionStatus;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.out.IInstitutionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class GetInstitutionUseCaseImplTest {

    @Mock
    private IInstitutionRepository institutionRepository;

    @InjectMocks
    private GetInstitutionUseCaseImpl getInstitutionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnFlux() {
        Institution inst = Institution.builder().id("1").build();
        when(institutionRepository.findAll()).thenReturn(Flux.just(inst));

        StepVerifier.create(getInstitutionUseCase.findAll())
                .expectNext(inst)
                .verifyComplete();
    }

    @Test
    void findById_WhenExists_ShouldReturnInstitution() {
        Institution inst = Institution.builder().id("1").build();
        when(institutionRepository.findById("1")).thenReturn(Mono.just(inst));

        StepVerifier.create(getInstitutionUseCase.findById("1"))
                .expectNext(inst)
                .verifyComplete();
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(institutionRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(getInstitutionUseCase.findById("1"))
                .expectError(InstitutionNotFoundException.class)
                .verify();
    }
}
