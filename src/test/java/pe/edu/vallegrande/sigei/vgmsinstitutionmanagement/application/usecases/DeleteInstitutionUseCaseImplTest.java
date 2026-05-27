package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.InstitutionStatus;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.out.IInstitutionRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DeleteInstitutionUseCaseImplTest {

    @Mock
    private IInstitutionRepository institutionRepository;

    @InjectMocks
    private DeleteInstitutionUseCaseImpl deleteInstitutionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ShouldDeactivate() {
        Institution inst = Institution.builder().id("1").status(InstitutionStatus.ACTIVE).build();
        when(institutionRepository.findById("1")).thenReturn(Mono.just(inst));
        when(institutionRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(deleteInstitutionUseCase.execute("1"))
                .assertNext(res -> {
                    org.junit.jupiter.api.Assertions.assertEquals(InstitutionStatus.INACTIVE, res.getStatus());
                })
                .verifyComplete();
    }
}
