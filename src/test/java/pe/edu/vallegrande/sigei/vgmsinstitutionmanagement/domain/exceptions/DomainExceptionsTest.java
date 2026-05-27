package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionsTest {

    @Test
    void testExceptions() {
        assertAll(
                () -> assertNotNull(new ClassroomCapacityException("Error")),
                () -> assertNotNull(new ClassroomNotFoundException("uuid")),
                () -> assertNotNull(new ConflictException("Conflict")),
                () -> assertNotNull(new DomainException("Domain Error")),
                () -> assertNotNull(new DuplicateModularCodeException("12345")),
                () -> assertNotNull(new InstitutionNotFoundException("inst-123")),
                () -> assertNotNull(new NotFoundException("Not Found"))
        );
    }

    @Test
    void testExceptionMessages() {
        ClassroomNotFoundException ex = new ClassroomNotFoundException("123");
        assertTrue(ex.getMessage().contains("123"));
        
        DuplicateModularCodeException ex2 = new DuplicateModularCodeException("MOD-001");
        assertTrue(ex2.getMessage().contains("MOD-001"));
    }
}
