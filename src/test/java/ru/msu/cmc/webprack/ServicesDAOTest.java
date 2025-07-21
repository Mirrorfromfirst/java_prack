package ru.msu.cmc.webprack;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.msu.cmc.webprack.DAO.ServicesDAO;
import ru.msu.cmc.webprack.models.Services;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServicesDAOTest {

    @Autowired
    private ServicesDAO servicesDAO;

    @BeforeEach
    void setup() {
        Services service1 = new Services(null, "Web Development", "Full stack development services", 1000);
        Services service2 = new Services(null, "SEO", "Search Engine Optimization services", 500);

        servicesDAO.save(service1);
        servicesDAO.save(service2);
    }

    @AfterEach
    void cleanup() {
        for (Services service: servicesDAO.getAllServices() ){
            if (service.getName().equals("Web Development") || service.getName().equals("SEO") || service.getName().equals("Search Engine Optimization services")){
                servicesDAO.delete(service);
            }
        }
    }

    @Test
    void testGetAllServices() {
        List<Services> allServices = servicesDAO.getAllServices();
        assertNotNull(allServices);
        assertFalse(allServices.isEmpty());
        assertTrue(allServices.stream().anyMatch(service -> service.getName().equals("Web Development")));
    }

    @Test
    void testGetById() {
        Services service = servicesDAO.getAllServices().stream().filter(s -> "Web Development".equals(s.getName())).findFirst().orElse(null);
        assertNotNull(service);

        Services found = servicesDAO.getById(service.getId());
        assertNotNull(found);
        assertEquals("Web Development", found.getName());
    }

    @Test
    void testUpdate() {
        Services service = servicesDAO.getAllServices().stream().filter(s -> "SEO".equals(s.getName())).findFirst().orElse(null);
        assertNotNull(service);

        service.setDescription("Updated SEO services");
        servicesDAO.update(service);

        Services updated = servicesDAO.getById(service.getId());
        assertEquals("Updated SEO services", updated.getDescription());
    }

    @Test
    void testDelete() {
        Services service = servicesDAO.getAllServices().stream().filter(s -> "SEO".equals(s.getName())).findFirst().orElse(null);
        assertNotNull(service);

        servicesDAO.delete(service);

        Services deleted = servicesDAO.getById(service.getId());
        assertNull(deleted);
    }
}
