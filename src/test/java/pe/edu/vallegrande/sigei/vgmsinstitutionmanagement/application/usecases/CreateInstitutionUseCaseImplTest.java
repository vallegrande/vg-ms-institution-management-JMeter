package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.out.IInstitutionRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateInstitutionUseCaseImplTest {

    @Mock
    private IInstitutionRepository institutionRepository;

    @InjectMocks
    private CreateInstitutionUseCaseImpl createInstitutionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ShouldReturnSavedInstitution() {
        Institution inst = Institution.builder().name("Test").build();
        when(institutionRepository.save(any())).thenReturn(Mono.just(inst));

        StepVerifier.create(createInstitutionUseCase.execute(inst))
                .expectNext(inst)
                .verifyComplete();
    }
}
