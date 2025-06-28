package ru.msu.cmc.webprack;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.msu.cmc.webprack.DAO.UserDAO;
import ru.msu.cmc.webprack.models.Users;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DAOTest {

    @Autowired
    private UserDAO userDAO;

    //private Users testUser;

    @BeforeEach
    void setup() {
        Users testUser = new Users();
        testUser.setName("testuser");
        testUser.setPass("testpass");
        testUser.setRole("admin");
        userDAO.save(testUser);
    }

    @AfterEach
    void cleanup() {
        Users testUser = userDAO.getUserByUsername("testuser");
        if (testUser != null) {
            userDAO.delete(testUser);
        }
    }

    @Test
    void testGetById() {
        Users testUser = userDAO.getUserByUsername("testuser");
        Users found = userDAO.getById(testUser.getId());
        assertNotNull(found);
        assertEquals("testuser", found.getName());
    }

    @Test
    void testGetAll() {
        List<Users> allUsers = (List<Users>) userDAO.getAll();
        assertFalse(allUsers.isEmpty());
        assertTrue(allUsers.stream().anyMatch(u -> u.getName().equals("testuser")));
    }

    @Test
    void testUpdate() {
        Users testUser = userDAO.getUserByUsername("testuser");
        testUser.setPass("newpass");
        userDAO.update(testUser);

        Users updated = userDAO.getById(testUser.getId());
        assertEquals("newpass", updated.getPass());
    }

    @Test
    void testDelete() {
        Users testUser = userDAO.getUserByUsername("testuser");
        assertNotNull(testUser);
        userDAO.delete(testUser);
        Users deleted = userDAO.getById(testUser.getId());
        assertNull(deleted);
    }

    @Test
    void testGetUserByUsername() {
        Users testUser = userDAO.getUserByUsername("testuser");
        Users found = userDAO.getUserByUsername("testuser");
        assertNotNull(found);
        assertEquals(testUser.getId(), found.getId());

        Users notFound = userDAO.getUserByUsername("nonexistent");
        assertNull(notFound);
    }
}
