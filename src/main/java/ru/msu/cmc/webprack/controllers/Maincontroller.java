package ru.msu.cmc.webprack.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Maincontroller {

    @GetMapping("/")
    public String dashboard(HttpServletRequest request, Model model) {
        System.out.println(request.getRequestURI());
        model.addAttribute("requestURI", request.getRequestURI());

        model.addAttribute("message", "Тест dashboard");
        return "dashboard";
    }
}


