package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.mappers;

import io.r2dbc.postgresql.codec.Json;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.Address;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.ContactMethod;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.InstitutionStatus;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.infrastructure.persistence.entities.InstitutionEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionPersistenceMapperParameterizedTest {

    private final InstitutionPersistenceMapper mapper = new InstitutionPersistenceMapper();

    @ParameterizedTest(name = "Prueba {index}: Mapeo de InstitutionEntity con Estado [{0}] y JSON de Dirección [{1}] a Dominio")
    @MethodSource("provideEntityData")
    void debeMapearEntidadADominio(String statusStr, String addressJson, InstitutionStatus expectedStatus, String expectedDept) {
        // Arrange
        InstitutionEntity entity = InstitutionEntity.builder()
                .status(statusStr)
                .address(addressJson != null ? Json.of(addressJson) : null)
                .build();

        // Act
        Institution domain = mapper.toDomain(entity);

        // Assert
        assertEquals(expectedStatus, domain.getStatus());
        if (expectedDept == null) {
            assertNull(domain.getAddress());
        } else {
            assertNotNull(domain.getAddress());
            assertEquals(expectedDept, domain.getAddress().getDepartment());
        }
    }

    private static Stream<Arguments> provideEntityData() {
        return Stream.of(
                Arguments.of("ACTIVE", "{\"department\":\"Lima\",\"province\":\"Lima\",\"district\":\"Miraflores\"}", InstitutionStatus.ACTIVE, "Lima"),
                Arguments.of("INACTIVE", "{\"department\":\"Ica\",\"province\":\"Cañete\",\"district\":\"San Vicente\"}", InstitutionStatus.INACTIVE, "Ica"),
                Arguments.of(null, null, null, null),
                Arguments.of("ACTIVE", "{}", InstitutionStatus.ACTIVE, null)
        );
    }

    @ParameterizedTest(name = "Prueba {index}: Mapeo de Institution Dominio con {1} Métodos de Contacto a Entidad")
    @MethodSource("provideContactMethods")
    void debeMapearDominioAEntidad(List<ContactMethod> contactMethods, int expectedSize) {
        // Arrange
        Institution domain = Institution.builder()
                .contactMethods(contactMethods)
                .build();

        // Act
        InstitutionEntity entity = mapper.toEntity(domain);

        // Assert
        if (contactMethods == null || contactMethods.isEmpty()) {
            assertTrue(entity.getContactMethods() == null || entity.getContactMethods().asString().equals("null") || entity.getContactMethods().asString().equals("[]"));
        } else {
            assertNotNull(entity.getContactMethods());
            assertTrue(entity.getContactMethods().asString().contains("\"type\""));
        }
    }

    private static Stream<Arguments> provideContactMethods() {
        return Stream.of(
                Arguments.of(List.of(new ContactMethod("EMAIL", "test@test.com")), 1),
                Arguments.of(List.of(new ContactMethod("PHONE", "999888777"), new ContactMethod("EMAIL", "info@school.edu")), 2),
                Arguments.of(List.of(), 0),
                Arguments.of(null, 0)
        );
    }
}
