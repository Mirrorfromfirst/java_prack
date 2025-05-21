package ru.msu.cmc.webprack.testDAO;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprack.DAO.UserDAO;
import ru.msu.cmc.webprack.models.Users;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class UserDAOTest {

    @Autowired
    private UserDAO usersDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private Users user1;
    private Users user2;

    @BeforeEach
    @Transactional
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            user1 = new Users();
            user1.setName("admin");
            user1.setPass("hash1");
            user1.setRole("ADMIN");
            session.save(user1);

            user2 = new Users();
            user2.setName("client");
            user2.setPass("hash2");
            user2.setRole("CLIENT");
            session.save(user2);

            session.getTransaction().commit();
        }
    }

    @AfterEach
    void clearTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE Users CASCADE").executeUpdate();
            session.createNativeQuery("ALTER SEQUENCE users_user_id_seq RESTART WITH 1").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testGetById() {
        Users fetched = usersDAO.getById(user1.getId());
        assertNotNull(fetched);
        assertEquals("admin", fetched.getName());
    }

    @Test
    void testGetAllUsers() {
        Collection<Users> allUsers = usersDAO.getAll();
        assertEquals(2, allUsers.size());
    }

    @Test
    void testDeleteUser() {
        Users toDelete = usersDAO.getById(user2.getId());
        assertNotNull(toDelete);

        usersDAO.delete(toDelete);
        assertNull(usersDAO.getById(user2.getId()));
    }

    @Test
    void testUpdateUser() {
        Users u = usersDAO.getById(user1.getId());
        assertNotNull(u);
        u.setName("newAdmin");
        usersDAO.update(u);

        Users updated = usersDAO.getById(user1.getId());
        assertEquals("newAdmin", updated.getName());
    }
}
