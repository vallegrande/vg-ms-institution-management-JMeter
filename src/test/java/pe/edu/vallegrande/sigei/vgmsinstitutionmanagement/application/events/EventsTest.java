package pe.edu.vallegrande.sigei.vgmsinstitutionmanagement.application.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventsTest {

    @Test
    void testEvents() {
        ClassroomCreatedEvent e1 = new ClassroomCreatedEvent("1", "inst-1", "Aula 1", "5");
        assertEquals("1", e1.classroomId());
        assertEquals("Aula 1", e1.classroomName());

        InstitutionCreatedEvent e2 = new InstitutionCreatedEvent("inst-1", "Name", "MOD123");
        assertEquals("inst-1", e2.institutionId());

        InstitutionUpdatedEvent e3 = new InstitutionUpdatedEvent("inst-1", "all");
        assertEquals("inst-1", e3.institutionId());
        assertEquals("all", e3.fieldsChanged());

        AnnouncementCreatedEvent e4 = new AnnouncementCreatedEvent("inst-1", "Title", "Msg", "All");
        assertEquals("Title", e4.title());
    }
}
