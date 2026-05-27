package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.adapters.in.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.dto.request.CreateInstitutionRequest;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.dto.response.InstitutionResponse;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.mappers.ClassroomMapper;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.mappers.InstitutionMapper;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.in.*;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.adapters.out.external.CloudinaryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(InstitutionRest.class)
class InstitutionRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICreateInstitutionUseCase createInstitutionUseCase;

    @MockBean
    private IGetInstitutionUseCase getInstitutionUseCase;

    @MockBean
    private IUpdateInstitutionUseCase updateInstitutionUseCase;

    @MockBean
    private IDeleteInstitutionUseCase deleteInstitutionUseCase;

    @MockBean
    private IRestoreInstitutionUseCase restoreInstitutionUseCase;

    @MockBean
    private IGetClassroomUseCase getClassroomUseCase;

    @MockBean
    private InstitutionMapper institutionMapper;

    @MockBean
    private ClassroomMapper classroomMapper;

    @MockBean
    private CloudinaryService cloudinaryService;

    @Test
    @WithMockUser
    void getAll_ShouldReturnSuccess() {
        Institution inst = Institution.builder().id("1").name("Valle Grande").build();
        InstitutionResponse resp = InstitutionResponse.builder().id("1").name("Valle Grande").build();

        when(getInstitutionUseCase.findAll()).thenReturn(Flux.just(inst));
        when(institutionMapper.toResponse(inst)).thenReturn(resp);

        webTestClient.get().uri("/api/institutions")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data[0].name").isEqualTo("Valle Grande");
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreated() {
        CreateInstitutionRequest req = CreateInstitutionRequest.builder().name("New Inst").build();
        Institution inst = Institution.builder().name("New Inst").build();
        InstitutionResponse resp = InstitutionResponse.builder().name("New Inst").build();

        when(institutionMapper.toDomain(any(CreateInstitutionRequest.class))).thenReturn(inst);
        when(createInstitutionUseCase.execute(any())).thenReturn(Mono.just(inst));
        when(institutionMapper.toResponse(any())).thenReturn(resp);

        webTestClient.mutateWith(csrf())
                .post().uri("/api/institutions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo("New Inst");
    }
}
