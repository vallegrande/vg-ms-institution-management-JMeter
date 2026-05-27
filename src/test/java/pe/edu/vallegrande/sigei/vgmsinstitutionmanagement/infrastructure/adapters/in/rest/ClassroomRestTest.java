package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.adapters.in.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.dto.request.CreateClassroomRequest;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.dto.response.ClassroomResponse;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.mappers.ClassroomMapper;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Classroom;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.ports.in.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(ClassroomRest.class)
class ClassroomRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICreateClassroomUseCase createClassroomUseCase;

    @MockBean
    private IGetClassroomUseCase getClassroomUseCase;

    @MockBean
    private IUpdateClassroomUseCase updateClassroomUseCase;

    @MockBean
    private IDeleteClassroomUseCase deleteClassroomUseCase;

    @MockBean
    private IRestoreClassroomUseCase restoreClassroomUseCase;

    @MockBean
    private ClassroomMapper classroomMapper;

    @Test
    @WithMockUser
    void getAll_ShouldReturnSuccess() {
        Classroom c = Classroom.builder().id("1").build();
        ClassroomResponse resp = ClassroomResponse.builder().id("1").build();

        when(getClassroomUseCase.findAll()).thenReturn(Flux.just(c));
        when(classroomMapper.toResponse(c)).thenReturn(resp);

        webTestClient.get().uri("/api/classrooms")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data[0].id").isEqualTo("1");
    }

    @Test
    @WithMockUser
    void getById_ShouldReturnSuccess() {
        Classroom c = Classroom.builder().id("1").build();
        ClassroomResponse resp = ClassroomResponse.builder().id("1").build();

        when(getClassroomUseCase.findById("1")).thenReturn(Mono.just(c));
        when(classroomMapper.toResponse(c)).thenReturn(resp);

        webTestClient.get().uri("/api/classrooms/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo("1");
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreated() {
        CreateClassroomRequest req = CreateClassroomRequest.builder().classroomName("Aula 1").build();
        Classroom c = Classroom.builder().classroomName("Aula 1").build();
        ClassroomResponse resp = ClassroomResponse.builder().classroomName("Aula 1").build();

        when(classroomMapper.toDomain(any(CreateClassroomRequest.class))).thenReturn(c);
        when(createClassroomUseCase.execute(any())).thenReturn(Mono.just(c));
        when(classroomMapper.toResponse(any())).thenReturn(resp);

        webTestClient.mutateWith(csrf())
                .post().uri("/api/classrooms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.classroomName").isEqualTo("Aula 1");
    }
}
