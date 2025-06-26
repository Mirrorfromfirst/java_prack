package ru.msu.cmc.webprack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.msu.cmc.webprack.DAO.UserDAO;
import ru.msu.cmc.webprack.models.Users;

/*@Controller
public class AuthController {
    @Autowired
    private UserDAO userDAO;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";// шаблон login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        Users user = userDAO.getUserByUsername(username);
        if (user == null || !user.getPass().equals(password)) {
            redirectAttributes.addFlashAttribute("error",
                    "Неверный логин или пароль");
            return "redirect:/login";
        }
        session.setAttribute("user", user);
        session.setAttribute("username", user.getName());
        return "redirect:/";
    }

    @GetMapping("/logout-success")
    public String logoutPage() {
        return "redirect:/login?logout";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }
}*/


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        if (error != null) {
            model.addAttribute("loginError", "Неверный логин или пароль");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Вы успешно вышли из системы");
        }
        System.out.println("out" + error + logout);
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String name,
            @RequestParam String role,
            RedirectAttributes redirectAttrs,
            Model model) {

        // Валидация
        if (userDAO.getUserByUsername(username) != null) {
            model.addAttribute("usernameError", "Этот логин уже занят");
            model.addAttribute("username", username);
            model.addAttribute("name", name);
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            model.addAttribute("username", username);
            model.addAttribute("name", name);
            return "register";
        }

        // Создание пользователя
        Users newUser = new Users();
        newUser.setName(username);
        newUser.setPass(passwordEncoder.encode(password));
        newUser.setName(name);
        newUser.setRole(role);

        userDAO.save(newUser);

        redirectAttrs.addFlashAttribute("registrationSuccess", "Регистрация успешна! Теперь вы можете войти.");
        return "redirect:/login";
    }
}
