package ru.msu.cmc.webprack.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msu.cmc.webprack.DAO.ContractsDAO;
import ru.msu.cmc.webprack.DAO.EmployeeDAO;
import ru.msu.cmc.webprack.models.Contracts;
import ru.msu.cmc.webprack.models.Employees;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractsDAO contractDAO;
    private final EmployeeDAO employeeDAO;

    @Autowired
    public ContractServiceImpl(ContractsDAO contractDAO, EmployeeDAO employeeDAO) {
        this.contractDAO = contractDAO;
        this.employeeDAO = employeeDAO;
    }

    @Override
    public void assignContract(Long contractId, Long employeeId) {
        Contracts contract = contractDAO.getById(contractId);
        Employees employee = employeeDAO.getById(employeeId);

        // Бизнес-логика
        if (contract.getStatus() != Contracts.ContractStatus.NEW) {
            throw new IllegalStateException("Можно назначать только новые договоры");
        }

        contract.setAssignedEmployee(employee);
        contract.setStatus(Contracts.ContractStatus.IN_PROGRESS);
        contractDAO.update(contract);
    }

    @Override
    public void completeContract(Long contractId) {
        Contracts contract = contractDAO.getById(contractId);
        contract.setStatus(Contracts.ContractStatus.COMPLETED);
        contractDAO.update(contract);
    }
}
