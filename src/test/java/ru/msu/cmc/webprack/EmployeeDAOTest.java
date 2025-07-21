package ru.msu.cmc.webprack;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.msu.cmc.webprack.DAO.EmployeeDAO;
import ru.msu.cmc.webprack.models.Employees;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeDAOTest {

    @Autowired
    private EmployeeDAO employeeDAO;

    @BeforeEach
    void setup() {
        Employees employee1 = new Employees(null,null,"Alice", "Manager", "alice@example.com", "123456", "Address 1");
        Employees employee2 = new Employees( null, null,"Bob", "Developer", "bob@example.com", "654321", "Address 2");

        employeeDAO.save(employee1);
        employeeDAO.save(employee2);
    }

    @AfterEach
    void cleanup() {
        List<Employees> employees = employeeDAO.getAllEmployees();
        for (Employees employee : employees) {
            if (employee.getName().equals("Alice") || employee.getName().equals("Bob")) {
                employeeDAO.delete(employee);
            }
        }
    }

    @Test
    void testGetAllEmployees() {
        List<Employees> allEmployees = employeeDAO.getAllEmployees();
        assertNotNull(allEmployees);
        assertFalse(allEmployees.isEmpty());
        assertTrue(allEmployees.stream().anyMatch(e -> e.getName().equals("Alice")));
        assertTrue(allEmployees.stream().anyMatch(e -> e.getName().equals("Bob")));
    }

    @Test
    void testGetById() {
        Employees employee = employeeDAO.getAllEmployees().stream().filter(e -> "Alice".equals(e.getName())).findFirst().orElse(null);
        assertNotNull(employee);

        Employees found = employeeDAO.getById(employee.getId());
        assertNotNull(found);
        assertEquals("Alice", found.getName());
    }

    @Test
    void testUpdate() {
        Employees employee = employeeDAO.getAllEmployees().stream().filter(e -> "Alice".equals(e.getName())).findFirst().orElse(null);
        assertNotNull(employee);

        employee.setPosition("Senior Manager");
        employeeDAO.update(employee);

        Employees updated = employeeDAO.getById(employee.getId());
        assertEquals("Senior Manager", updated.getPosition());
    }

    @Test
    void testDelete() {
        Employees employee = employeeDAO.getAllEmployees().stream().filter(e -> "Bob".equals(e.getName())).findFirst().orElse(null);
        assertNotNull(employee);

        employeeDAO.delete(employee);

        Employees deleted = employeeDAO.getById(employee.getId());
        assertNull(deleted);
    }
}
