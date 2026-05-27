package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.mappers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.dto.request.CreateClassroomRequest;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.dto.response.ClassroomResponse;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Classroom;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.ClassroomStatus;

import static org.junit.jupiter.api.Assertions.*;

class ClassroomMapperParameterizedTest {

    private final ClassroomMapper mapper = new ClassroomMapper();

    @ParameterizedTest(name = "Prueba {index}: Mapeo de CreateClassroomRequest con Nombre [{0}] y Capacidad [{1}] a Dominio")
    @CsvSource({
            "Aula 101, 30, Red",
            "Aula 202, 25, Blue",
            "Laboratorio, 20, Green"
    })
    void debeMapearCreateRequestADominio(String name, Integer capacity, String color) {
        // Arrange
        CreateClassroomRequest request = CreateClassroomRequest.builder()
                .classroomName(name)
                .capacity(capacity)
                .color(color)
                .classroomAge("5 años")
                .institutionId("inst-123")
                .build();

        // Act
        Classroom domain = mapper.toDomain(request);

        // Assert
        assertEquals(name, domain.getClassroomName());
        assertEquals(capacity, domain.getCapacity());
        assertEquals(color, domain.getColor());
        assertEquals(ClassroomStatus.ACTIVE, domain.getStatus());
        assertNotNull(domain.getCreatedAt());
    }

    @ParameterizedTest(name = "Prueba {index}: Mapeo de Classroom Dominio con ID [{0}] y Estado [{1}] a Respuesta")
    @CsvSource({
            "uuid-1, ACTIVE, Aula A",
            "uuid-2, INACTIVE, Aula B"
    })
    void debeMapearDominioARespuesta(String id, ClassroomStatus status, String name) {
        // Arrange
        Classroom domain = Classroom.builder()
                .id(id)
                .status(status)
                .classroomName(name)
                .capacity(30)
                .build();

        // Act
        ClassroomResponse response = mapper.toResponse(domain);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(status.name(), response.getStatus());
        assertEquals(name, response.getClassroomName());
    }
}
