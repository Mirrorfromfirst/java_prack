package ru.msu.cmc.webprack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cmc.webprack.DAO.ClientsDAO;
import ru.msu.cmc.webprack.DAO.ContractsDAO;
import ru.msu.cmc.webprack.DAO.EmployeeDAO;
import ru.msu.cmc.webprack.DAO.ServicesDAO;
import ru.msu.cmc.webprack.models.Clients;
import ru.msu.cmc.webprack.models.Contracts;
import ru.msu.cmc.webprack.models.Employees;
import ru.msu.cmc.webprack.models.Services;
import ru.msu.cmc.webprack.services.ContractService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ContractController {

    @Autowired
    private ContractsDAO contractsDAO;

    @Autowired
    private ClientsDAO clientsDAO;

    @Autowired
    private ServicesDAO servicesDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    public static LocalDate toLocalDate(java.util.Date date) {
        if (date instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        } else {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }


    @GetMapping("/contracts")
    public String getContracts(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            Model model) {

        Collection<Contracts> allContracts = contractsDAO.getAll();

        List<Contracts> filteredContracts = allContracts.stream()
                .filter(contract -> clientId == null || contract.getClient().getId().equals(clientId))
                .filter(contract -> employeeName == null || employeeName.isEmpty() ||
                        (contract.getAssignedEmployee() != null &&
                                contract.getAssignedEmployee().getName().equalsIgnoreCase(employeeName)))
                .filter(contract -> dateFrom == null ||
                        toLocalDate(contract.getStart_date()).isAfter(dateFrom))
                .filter(contract -> dateTo == null ||
                        toLocalDate(contract.getEnd_date()).isBefore(dateTo))
                .collect(Collectors.toList());

        model.addAttribute("contracts", filteredContracts);
        System.out.println(contractsDAO.getAll());
        model.addAttribute("employees", employeeDAO.getAll());
        return "contracts";
    }

    @GetMapping("/contracts/new")
    public String showAddContractForm(Model model) {
        model.addAttribute("contract", new Contracts());
        model.addAttribute("clients", clientsDAO.getAll());
        model.addAttribute("services", servicesDAO.getAll());
        return "newContract";
    }

    @PostMapping("/contracts/new")
    public String addContract(
            @RequestParam Long clientId,
            @RequestParam Long serviceId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String terms,
            Model model) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            Clients client = clientsDAO.getById(clientId);
            Services service = servicesDAO.getById(serviceId);

            System.out.println(client);
            System.out.println(service);
            Contracts newContract = new Contracts();
            newContract.setClient(client);
            newContract.setService(service);
            newContract.setStart_date(start);
            newContract.setEnd_date(end);
            newContract.setTerm(terms);

            contractsDAO.save(newContract);
            return "redirect:/contracts?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при добавлении договора: " + e.getMessage());
            model.addAttribute("clients", clientsDAO.getAll());
            model.addAttribute("services", servicesDAO.getAll());
            return "newContract";
        }
    }

    @GetMapping("/contracts/{id}")
    public String viewContract(@PathVariable Long id, Model model) {
        Contracts contract = contractsDAO.getById(id);
        if (contract == null) {
            return "redirect:/contracts?error=not_found";
        }

        model.addAttribute("contract", contract);
        return "contractDetails";
    }

    @GetMapping("/contracts/{id}/assign")
    public String showAssignForm(@PathVariable Long id, Model model) {
        Contracts contract = contractsDAO.getById(id);
        List<Employees> employees = employeeDAO.getAllEmployees();

        model.addAttribute("contract", contract);
        model.addAttribute("employees", employees);
        return "assignContract";
    }

    @PostMapping("/contracts/{id}/assign")
    public String assignContract(
            @PathVariable Long id,
            @RequestParam Long employeeId) {

        Contracts contract = contractsDAO.getById(id);
        Employees employee = employeeDAO.getById(employeeId);

        contract.setAssignedEmployee(employee);
        contract.setStatus(Contracts.ContractStatus.IN_PROGRESS);
        contractsDAO.update(contract);

        return "redirect:/contracts/" + id + "?assigned=true";
    }

    @GetMapping("/contracts/{id}/complete")
    public String showCompleteForm(@PathVariable Long id, Model model) {
        Contracts contract = contractsDAO.getById(id);
        model.addAttribute("contract", contract);
        return "completeContract";
    }

    @PostMapping("/contracts/{id}/complete")
    public String completeContract(
            @PathVariable Long id,
            @RequestParam(required = false) String notes) {

        Contracts contract = contractsDAO.getById(id);
        contract.setStatus(Contracts.ContractStatus.COMPLETED);
        contractsDAO.update(contract);

        return "redirect:/contracts/" + id + "?completed=true";
    }

    @GetMapping("/contracts/{id}/delete")
    public String confirmDeleteClient(@PathVariable Long id, Model model) {
        Contracts contract = contractsDAO.getById(id);
        if (contract == null) {
            model.addAttribute("error", "Контракт не найден");
            return "redirect:/contracts";
        }
        model.addAttribute("contract", contract);
        return "confirmDeleteContract";
    }

    @PostMapping("/contracts/{id}/delete")
    public String deleteClient(@PathVariable Long id, Model model) {
        try {
            Contracts contract = contractsDAO.getById(id);
            if (contract == null) {
                model.addAttribute("error", "Контракт не найден");
                return "redirect:/contracts";
            }
            contractsDAO.delete(contract);
            return "redirect:/contracts?deleteSuccess=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при удалении контракта: " + e.getMessage());
            return "redirect:/contracts";
        }
    }
}