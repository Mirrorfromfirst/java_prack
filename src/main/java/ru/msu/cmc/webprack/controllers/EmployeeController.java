package ru.msu.cmc.webprack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cmc.webprack.DAO.EmployeeDAO;
import ru.msu.cmc.webprack.models.Employees;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeDAO employeesDAO;

    @GetMapping("/employees")
    public String getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        List<Employees> allEmployees = employeesDAO.getAllEmployees();
        int totalElements = allEmployees.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = page * size;
        int end = Math.min(start + size, totalElements);
        List<Employees> pageContent = allEmployees.subList(start, end);

        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("content", pageContent);
        paginationData.put("number", page);
        paginationData.put("totalPages", totalPages);
        paginationData.put("first", page == 0);
        paginationData.put("last", page == totalPages - 1);

        model.addAttribute("employees", paginationData);
        return "employees";
    }

    @GetMapping("/employees/new")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employees());
        return "newEmployee";
    }

    @PostMapping("/employees/new")
    public String addEmployee(
            @RequestParam String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            Model model
    ) {
        try {
            Employees newEmployee = new Employees();
            newEmployee.setName(name);
            newEmployee.setPosition(position != null ? position : "-");
            newEmployee.setPhone(phone != null ? phone : "-");
            newEmployee.setEmail(email != null ? email : "-");
            newEmployee.setAddress(address != null ? address : "-");
            employeesDAO.save(newEmployee);
            return "redirect:/employees?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при добавлении сотрудника: " + e.getMessage());
            return "newEmployee";
        }
    }

    @GetMapping("/employees/{id}/delete")
    public String confirmDeleteEmployee(@PathVariable Long id, Model model) {
        Employees employee = employeesDAO.getById(id);
        if (employee == null) {
            model.addAttribute("error", "Сотрудник не найден");
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee);
        return "confirmDeleteEmployee";
    }

    @PostMapping("/employees/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        try {
            Employees employee = employeesDAO.getById(id);
            if (employee == null) {
                model.addAttribute("error", "Сотрудник не найден");
                return "redirect:/employees";
            }
            employeesDAO.delete(employee);
            return "redirect:/employees?deleteSuccess=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при удалении сотрудника: " + e.getMessage());
            return "redirect:/employees";
        }
    }

    @GetMapping("/employees/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employees employee = employeesDAO.getById(id);
        if (employee == null) {
            model.addAttribute("error", "Сотрудник не найден");
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee);
        return "editEmployee";
    }

    @PostMapping("/employees/{id}/edit")
    public String updateEmployee(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            Model model) {

        try {
            Employees employee = employeesDAO.getById(id);
            if (employee == null) {
                model.addAttribute("error", "Сотрудник не найден");
                return "redirect:/employees";
            }

            employee.setName(name);
            employee.setPosition(!"-".equals(position) ? position : null);
            employee.setPhone(!"-".equals(phone) ? phone : null);
            employee.setEmail(!"-".equals(email) ? email : null);
            employee.setAddress(!"-".equals(address) ? address : null);
            employeesDAO.update(employee);
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении сотрудника: " + e.getMessage());
            return "editEmployee";
        }
    }

    @GetMapping("/employees/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Employees employee = employeesDAO.getById(id);
        if (employee == null) {
            model.addAttribute("error", "Сотрудник с ID " + id + " не найден");
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee);
        return "employeeCard";
    }
}