package ru.msu.cmc.webprack.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.webprack.DAO.ClientsDAO;
import ru.msu.cmc.webprack.DAO.UserDAO;
import ru.msu.cmc.webprack.models.Clients;
import ru.msu.cmc.webprack.models.Users;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Controller
public class ClientConroller {

    @Autowired
    ClientsDAO clientsDAO;

    /*@GetMapping("/clients")
    public String getClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        List<Clients> allClients = clientsDAO.getAllUsers();
        //System.out.println(allClients.get(0).getClass()); // Что это за класс?
        int totalElements = allClients.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = page * size;
        int end = Math.min(start + size, totalElements);
        List<Clients> pageContent = allClients.subList(start, end);

        // Создаём Map с данными для пагинации
        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("content", pageContent);
        paginationData.put("number", page);
        paginationData.put("totalPages", totalPages);
        paginationData.put("first", page == 0);
        paginationData.put("last", page == totalPages - 1);
        paginationData.put("active", "active");
        System.out.println(paginationData);
        model.addAttribute("clients", paginationData);
        return "clients";
    }*/

    @GetMapping("/clients")
    public String getClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            Model model) {

        // Получаем всех клиентов из DAO
        List<Clients> allClients = clientsDAO.getAllUsers();

        // Применяем фильтры
        List<Clients> filteredClients = allClients.stream()
                .filter(client -> name == null || name.isEmpty() ||
                        (client.getName() != null && client.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(client -> phone == null || phone.isEmpty() ||
                        (client.getPhone() != null && client.getPhone().contains(phone)))
                .filter(client -> email == null || email.isEmpty() ||
                        (client.getEmail() != null && client.getEmail().toLowerCase().contains(email.toLowerCase())))
        .collect(Collectors.toList());

        // Пагинация
        int totalElements = filteredClients.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Проверка на выход за пределы страниц
        if (page >= totalPages && totalPages > 0) {
            page = totalPages - 1;
        }

        int start = page * size;
        int end = Math.min(start + size, totalElements);
        List<Clients> pageContent = filteredClients.subList(start, end);

        // Создаём Map с данными для пагинации и фильтров
        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("content", pageContent);
        paginationData.put("number", page);
        paginationData.put("size", size);
        paginationData.put("totalPages", totalPages);
        paginationData.put("first", page == 0);
        paginationData.put("last", page == totalPages - 1 || totalPages == 0);
        paginationData.put("totalElements", totalElements);

        // Добавляем параметры фильтров для отображения в форме
        model.addAttribute("clients", paginationData);
        model.addAttribute("currentName", name);
        model.addAttribute("currentPhone", phone);
        model.addAttribute("currentEmail", email);
        model.addAttribute("currentActive", active);
        model.addAttribute("currentDateFrom", dateFrom);
        model.addAttribute("currentDateTo", dateTo);

        return "clients";
    }


    // Показ формы добавления клиента
    @GetMapping("/clients/new")
    public String showAddClientForm(Model model) {
        model.addAttribute("client", new Clients());
        return "newClient";
    }

    // Обработка формы добавления клиента
    @PostMapping("/clients/new")
    public String addClient(
            @RequestParam String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String inn,
            @RequestParam(required = false) String contactPerson,
            @RequestParam(required = false) String notes,
            Model model
    ) {
        try {
            Clients newClient = new Clients();
            newClient.setName(name);
            newClient.setAddress(address != null ? address : "-");
            newClient.setPhone(phone != null ? phone : "-");
            newClient.setEmail(email != null ? email : "-");
            clientsDAO.save(newClient);
            return "redirect:/clients?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при добавлении клиента: " + e.getMessage());
            return "newClient";
        }
    }

    @GetMapping("/clients/{id}/delete")
    public String confirmDeleteClient(@PathVariable Long id, Model model) {
        Clients client = clientsDAO.getById(id);
        if (client == null) {
            model.addAttribute("error", "Клиент не найден");
            return "redirect:/clients";
        }
        model.addAttribute("client", client);
        return "confirmDelete";
    }

    @PostMapping("/clients/{id}/delete")
    public String deleteClient(@PathVariable Long id, Model model) {
        try {
            Clients client = clientsDAO.getById(id);
            if (client == null) {
                model.addAttribute("error", "Клиент не найден");
                return "redirect:/clients";
            }
            clientsDAO.delete(client);
            return "redirect:/clients?deleteSuccess=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при удалении клиента: " + e.getMessage());
            return "redirect:/clients";
        }
    }

    @GetMapping("/clients/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Clients client = clientsDAO.getById(id);
        if (client == null) {
            model.addAttribute("error", "Клиент не найден");
            return "redirect:/clients";
        }
        model.addAttribute("client", client);
        return "editClient";
    }

    @PostMapping("/clients/{id}/edit")
    public String updateClient(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String inn,
            @RequestParam(required = false) String contactPerson,
            @RequestParam(required = false) String notes,
            Model model) {

        try {
            Clients client = clientsDAO.getById(id);
            if (client == null) {
                model.addAttribute("error", "Клиент не найден");
                return "redirect:/clients";
            }

            // Обновляем данные клиента
            client.setName(name);
            client.setAddress(!"-".equals(address) ? address : null);
            client.setPhone(!"-".equals(phone) ? phone : null);
            client.setEmail(!"-".equals(email) ? email : null);
            clientsDAO.update(client);
            return "redirect:/clients";
            //return "redirect:/clients/" + id + "?editSuccess=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении клиента: " + e.getMessage());
            return "editClient";
        }
    }

    @GetMapping("/clients/{id}")
    public String viewClient(@PathVariable Long id, Model model) {
        Clients client = clientsDAO.getById(id);
        if (client == null) {
            model.addAttribute("error", "Клиент с ID " + id + " не найден");
            return "redirect:/clients";
        }
        model.addAttribute("client", client);
        return "ClientCard"; // имя вашего HTML-шаблона
    }

    // Дополнительные методы для редактирования/удаления могут быть добавлены здесь
}

