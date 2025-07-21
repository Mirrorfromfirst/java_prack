package ru.msu.cmc.webprack;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.msu.cmc.webprack.DAO.ClientsDAO;
import ru.msu.cmc.webprack.models.Clients;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientsTest {

    @Autowired
    private ClientsDAO clientsDAO;

    @BeforeEach
    void setup() {
        Clients client1 = new Clients(null, "Alice", "123", "alice@example.com", "Address 1");
        Clients client2 = new Clients(null, "Bob", "456", "bob@example.com", "Address 2");

        clientsDAO.save(client1);
        clientsDAO.save(client2);
    }

    @AfterEach
    void cleanup() {
        List<Clients> clients = clientsDAO.getAllUsers();
        for (Clients client : clients) {
            if (client.getName().equals("Alice") || client.getName().equals("Bob")) {
                clientsDAO.delete(client);
            }
        }
    }

    @Test
    void testGetAllUsers() {
        List<Clients> allClients = clientsDAO.getAllUsers();
        assertNotNull(allClients);
        assertFalse(allClients.isEmpty());
        assertTrue(allClients.stream().anyMatch(c -> c.getName().equals("Alice")));
        assertTrue(allClients.stream().anyMatch(c -> c.getName().equals("Bob")));
    }

    @Test
    void testGetById() {
        Clients client = clientsDAO.getAllUsers().stream().filter(c -> "Alice".equals(c.getName())).findFirst().orElse(null);
        assertNotNull(client);

        Clients found = clientsDAO.getById(client.getId());
        assertNotNull(found);
        assertEquals("Alice", found.getName());
    }

    @Test
    void testUpdate() {
        Clients client = clientsDAO.getAllUsers().stream().filter(c -> "Alice".equals(c.getName())).findFirst().orElse(null);
        assertNotNull(client);

        client.setPhone("789");
        clientsDAO.update(client);

        Clients updated = clientsDAO.getById(client.getId());
        assertEquals("789", updated.getPhone());
    }

    @Test
    void testDelete() {
        Clients client = clientsDAO.getAllUsers().stream().filter(c -> "Bob".equals(c.getName())).findFirst().orElse(null);
        assertNotNull(client);

        clientsDAO.delete(client);

        Clients deleted = clientsDAO.getById(client.getId());
        assertNull(deleted);
    }
}
