package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.mappers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Classroom;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.ClassroomStatus;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.entities.ClassroomEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClassroomPersistenceMapperParameterizedTest {

    private final ClassroomPersistenceMapper mapper = new ClassroomPersistenceMapper();

    @ParameterizedTest(name = "Prueba {index}: Mapeo de ClassroomEntity con ID [{0}], Estado [{1}], Capacidad [{2}], Color [{3}] a Dominio")
    @CsvSource({
            "f47ac10b-58cc-4372-a567-0e02b2c3d479, ACTIVE, 30, Red",
            "550e8400-e29b-41d4-a716-446655440000, INACTIVE, 25, Blue",
            ", ACTIVE, 20, Green"
    })
    void debeMapearEntidadADominio(String id, String status, Integer capacity, String color) {
        // Arrange
        UUID uuid = id != null ? UUID.fromString(id) : null;
        ClassroomEntity entity = ClassroomEntity.builder()
                .classroomId(uuid)
                .status(status)
                .capacity(capacity)
                .color(color)
                .classroomName("Aula A")
                .classroomAge("5 años")
                .build();

        // Act
        Classroom domain = mapper.toDomain(entity);

        // Assert
        assertEquals(id, domain.getId());
        assertEquals(status, domain.getStatus() != null ? domain.getStatus().name() : null);
        assertEquals(capacity, domain.getCapacity());
        assertEquals(color, domain.getColor());
        assertEquals("Aula A", domain.getClassroomName());
    }

    @ParameterizedTest(name = "Prueba {index}: Mapeo de Classroom Dominio con ID [{0}], Estado [{1}], Capacidad [{2}] a Entidad")
    @CsvSource({
            "f47ac10b-58cc-4372-a567-0e02b2c3d479, ACTIVE, 30",
            "550e8400-e29b-41d4-a716-446655440000, INACTIVE, 25",
            ", ACTIVE, 20"
    })
    void debeMapearDominioAEntidad(String id, ClassroomStatus status, Integer capacity) {
        // Arrange
        Classroom domain = Classroom.builder()
                .id(id)
                .status(status)
                .capacity(capacity)
                .classroomName("Aula B")
                .build();

        // Act
        ClassroomEntity entity = mapper.toEntity(domain);

        // Assert
        assertEquals(id != null ? UUID.fromString(id) : null, entity.getClassroomId());
        assertEquals(status != null ? status.name() : null, entity.getStatus());
        assertEquals(capacity, entity.getCapacity());
        assertEquals(id == null, entity.isNewEntry());
    }
}
