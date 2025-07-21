package ru.msu.cmc.webprack;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.msu.cmc.webprack.DAO.ClientsDAO;
import ru.msu.cmc.webprack.DAO.ContractsDAO;
import ru.msu.cmc.webprack.DAO.EmployeeDAO;
import ru.msu.cmc.webprack.DAO.ServicesDAO;
import ru.msu.cmc.webprack.models.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Date;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContractsDAOTest {

    @Autowired
    private ContractsDAO contractsDAO;

    @Autowired
    private ClientsDAO clientsDAO;

    @Autowired
    private ServicesDAO servicesDAO;

    @Autowired
    private EmployeeDAO employeesDAO;

    private Clients testClient;
    private Services testService;
    private Employees testEmployee;

    @BeforeEach
    void setup() {
        testClient = new Clients(null, "John Doe", "123456789", "john.doe@example.com", "Some Address");
        clientsDAO.save(testClient);

        testService = new Services(null, "Web Development", "Website creation", 5000);
        servicesDAO.save(testService);

        testEmployee = new Employees(null, null, "Developer", "jane.smith@example.com", "987654321", "Office", null);
        employeesDAO.save(testEmployee);

        Contracts contract = new Contracts();
        contract.setClient(testClient);
        contract.setService(testService);
        contract.setAssignedEmployee(testEmployee);
        contract.setStart_date(new Date());
        contract.setEnd_date(new Date());
        contract.setStatus(Contracts.ContractStatus.NEW);
        contract.setTerm("1 year contract");
        contractsDAO.save(contract);
    }

    @AfterEach
    void cleanup() {
        Collection<Contracts> contracts = contractsDAO.getAll();
        for (Contracts contract : contracts) {
            if (contract.getTerm().equals("1 year contract")) {
                contractsDAO.delete(contract);
            }
        }
        clientsDAO.delete(testClient);
        servicesDAO.delete(testService);
        employeesDAO.delete(testEmployee);
    }

    @Test
    void testGetAllContracts() {
        Collection<Contracts> allContracts = contractsDAO.getAll();
        assertNotNull(allContracts);
        assertFalse(allContracts.isEmpty());
        assertTrue(allContracts.stream().anyMatch(contract -> contract.getClient().getName().equals("John Doe")));
    }

    @Test
    void testGetById() {
        Collection<Contracts> allContracts = contractsDAO.getAll();
        Contracts contract = allContracts.stream().filter(c -> "John Doe".equals(c.getClient().getName())).findFirst().orElse(null);
        assertNotNull(contract);
        Contracts found = contractsDAO.getById(contract.getId());
        assertNotNull(found);
        assertEquals(contract.getClient().getName(), found.getClient().getName());
    }

    @Test
    void testUpdate() {
        Collection<Contracts> allContracts = contractsDAO.getAll();
        Contracts contract = allContracts.stream().filter(c -> "John Doe".equals(c.getClient().getName())).findFirst().orElse(null);
        assertNotNull(contract);
        contract.setStatus(Contracts.ContractStatus.COMPLETED);
        contractsDAO.update(contract);
        Contracts updated = contractsDAO.getById(contract.getId());
        assertEquals(Contracts.ContractStatus.COMPLETED, updated.getStatus());
    }

    @Test
    void testDelete() {
        Collection<Contracts> allContracts = contractsDAO.getAll();
        Contracts contract = allContracts.stream().filter(c -> "John Doe".equals(c.getClient().getName())).findFirst().orElse(null);
        assertNotNull(contract);
        contractsDAO.delete(contract);
        Contracts deleted = contractsDAO.getById(contract.getId());
        assertNull(deleted);
    }
}
