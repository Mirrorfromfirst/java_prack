package ru.msu.cmc.webprack.testDAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprack.DAO.ClientsDAO;
import ru.msu.cmc.webprack.models.Clients;
import ru.msu.cmc.webprack.models.Users;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ClientsDAOTest {

    @Autowired
    private ClientsDAO clientsDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private Users testUser1;
    private Users testUser2;

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            testUser1 = new Users();
            testUser1.setId(1L);
            testUser1.setName("client_user1");
            session.save(testUser1);

            testUser2 = new Users();
            testUser2.setId(2L);
            testUser2.setName("client_user2");
            session.save(testUser2);

            Clients client1 = new Clients();
            client1.setUser(testUser1);
            client1.setName("Иван");
            client1.setPhone("12345");
            client1.setEmail("ivan@example.com");
            client1.setAddress("Москва");
            session.save(client1);

            Clients client2 = new Clients();
            client2.setUser(testUser2);
            client2.setName("Ольга");
            client2.setPhone("67890");
            client2.setEmail("olga@example.com");
            client2.setAddress("Питер");
            session.save(client2);

            session.getTransaction().commit();
        }
    }

    @AfterEach
    void clearTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE Clients CASCADE").executeUpdate();
            session.createNativeQuery("TRUNCATE Users CASCADE").executeUpdate();
            session.createNativeQuery("ALTER SEQUENCE users_user_id_seq RESTART WITH 1").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testGetById() {
        Clients client = clientsDAO.getById(1L);
        assertNotNull(client);
        assertEquals("Иван", client.getName());
    }

    @Test
    void testSearchClients() {
        List<Clients> result = clientsDAO.searchClients("12345", "Иван", new Date());

        assertEquals(1, result.size());
        assertEquals("Иван", result.get(0).getName());
    }

    @Test
    void testDeleteClient() {
        Clients client = clientsDAO.getById(1L);
        assertNotNull(client);

        clientsDAO.delete(client);
        Clients deleted = clientsDAO.getById(1L);
        assertNull(deleted);
    }

    @Test
    void testUpdateClient() {
        Clients client = clientsDAO.getById(2L);
        assertNotNull(client);
        client.setEmail("new_email@example.com");
        clientsDAO.update(client);

        Clients updated = clientsDAO.getById(2L);
        assertEquals("new_email@example.com", updated.getEmail());
    }
}
