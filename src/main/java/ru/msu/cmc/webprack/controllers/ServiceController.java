package ru.msu.cmc.webprack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cmc.webprack.DAO.ServicesDAO;
import ru.msu.cmc.webprack.models.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ServiceController {

    @Autowired
    private ServicesDAO servicesDAO;

    /*@GetMapping("/services")
    public String getServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        List<Services> allServices = servicesDAO.getAllServices();
        int totalElements = allServices.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = page * size;
        int end = Math.min(start + size, totalElements);
        List<Services> pageContent = allServices.subList(start, end);

        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("content", pageContent);
        paginationData.put("number", page);
        paginationData.put("totalPages", totalPages);
        paginationData.put("first", page == 0);
        paginationData.put("last", page == totalPages - 1);

        model.addAttribute("services", paginationData);
        return "services";
    }*/

    @GetMapping("/services")
    public String getServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer priceFrom,
            @RequestParam(required = false) Integer priceTo,
            Model model) {

        // Получаем все услуги из DAO
        List<Services> allServices = servicesDAO.getAllServices();

        // Применяем фильтры
        List<Services> filteredServices = allServices.stream()
                .filter(service -> name == null || name.isEmpty() ||
                        (service.getName() != null && service.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(service -> priceFrom == null ||
                        (service.getPrice() != 0 && service.getPrice() >= priceFrom))
                .filter(service -> priceTo == null ||
                        (service.getPrice() != 0 && service.getPrice() <= priceTo))
                .collect(Collectors.toList());

        // Пагинация
        int totalElements = filteredServices.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Проверка на выход за пределы страниц
        if (page >= totalPages && totalPages > 0) {
            page = totalPages - 1;
        }

        int start = page * size;
        int end = Math.min(start + size, totalElements);
        List<Services> pageContent = filteredServices.subList(start, end);

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
        model.addAttribute("services", paginationData);
        model.addAttribute("currentName", name);
        model.addAttribute("currentPriceFrom", priceFrom);
        model.addAttribute("currentPriceTo", priceTo);

        return "services";
    }

    @GetMapping("/services/new")
    public String showAddServiceForm(Model model) {
        model.addAttribute("service", new Services());
        return "newService";
    }

    @PostMapping("/services/new")
    public String addService(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam int price,
            Model model
    ) {
        try {
            Services newService = new Services();
            long id = 1;
            newService.setId(id);
            newService.setName(name);
            newService.setDescription(description != null ? description : "-");
            newService.setPrice(price);
            servicesDAO.save(newService);
            return "redirect:/services";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при добавлении услуги: " + e.getMessage());
            return "newService";
        }
    }

    @GetMapping("/services/{id}/delete")
    public String confirmDeleteService(@PathVariable Long id, Model model) {
        Services service = servicesDAO.getById(id);
        if (service == null) {
            model.addAttribute("error", "Услуга не найдена");
            return "redirect:/services";
        }
        model.addAttribute("service", service);
        return "confirmDeleteService";
    }

    @PostMapping("/services/{id}/delete")
    public String deleteService(@PathVariable Long id, Model model) {
        try {
            Services service = servicesDAO.getById(id);
            if (service == null) {
                model.addAttribute("error", "Услуга не найдена");
                return "redirect:/services";
            }
            servicesDAO.delete(service);
            return "redirect:/services?deleteSuccess=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при удалении услуги: " + e.getMessage());
            return "redirect:/services";
        }
    }

    @GetMapping("/services/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Services service = servicesDAO.getById(id);
        if (service == null) {
            model.addAttribute("error", "Услуга не найдена");
            return "redirect:/services";
        }
        model.addAttribute("service", service);
        return "editService";
    }

    @PostMapping("/services/{id}/edit")
    public String updateService(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam int price,
            Model model) {

        try {
            Services service = servicesDAO.getById(id);
            if (service == null) {
                model.addAttribute("error", "Услуга не найдена");
                return "redirect:/services";
            }

            service.setName(name);
            service.setDescription(!"-".equals(description) ? description : null);
            service.setPrice(price);
            servicesDAO.update(service);
            return "redirect:/services";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении услуги: " + e.getMessage());
            return "editService";
        }
    }

    @GetMapping("/services/{id}")
    public String viewService(@PathVariable Long id, Model model) {
        Services service = servicesDAO.getById(id);
        if (service == null) {
            model.addAttribute("error", "Услуга с ID " + id + " не найдена");
            return "redirect:/services";
        }
        model.addAttribute("service", service);
        return "serviceCard";
    }
}