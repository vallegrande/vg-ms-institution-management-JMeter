package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.mappers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.dto.request.CreateInstitutionRequest;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.dto.response.InstitutionResponse;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.Institution;
import pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.models.vo.InstitutionStatus;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionMapperParameterizedTest {

    private final InstitutionMapper mapper = new InstitutionMapper();

    @ParameterizedTest(name = "Prueba {index}: Mapeo de CreateInstitutionRequest con Nombre [{0}] y Código [{1}] a Dominio")
    @CsvSource({
            "Valle Grande, VG001, Public",
            "San José, SJ002, Private"
    })
    void debeMapearCreateRequestADominio(String name, String code, String type) {
        // Arrange
        CreateInstitutionRequest request = CreateInstitutionRequest.builder()
                .name(name)
                .codeInstitution(code)
                .institutionType(type)
                .build();

        // Act
        Institution domain = mapper.toDomain(request);

        // Assert
        assertEquals(name, domain.getName());
        assertEquals(code, domain.getCodeInstitution());
        assertEquals(type, domain.getInstitutionType());
        assertEquals(InstitutionStatus.ACTIVE, domain.getStatus());
        assertNotNull(domain.getCreatedAt());
    }

    @ParameterizedTest(name = "Prueba {index}: Mapeo de Institution Dominio con ID [{0}] y Estado [{1}] a Respuesta")
    @CsvSource({
            "inst-1, ACTIVE, Valle Grande",
            "inst-2, INACTIVE, San José"
    })
    void debeMapearDominioARespuesta(String id, InstitutionStatus status, String name) {
        // Arrange
        Institution domain = Institution.builder()
                .id(id)
                .status(status)
                .name(name)
                .codeInstitution("CODE123")
                .build();

        // Act
        InstitutionResponse response = mapper.toResponse(domain);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(status.name(), response.getStatus());
        assertEquals(name, response.getName());
    }
}
