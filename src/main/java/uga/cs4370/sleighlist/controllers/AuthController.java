package uga.cs4370.sleighlist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Login / register screens. POST handlers are placeholders that skip
 * authentication entirely; Person 2 replaces them with the real
 * UserService (hashed passwords, session user).
 */
@Controller
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "redirect:/groups";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Log in");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Create account");
        return "register";
    }

    @PostMapping({"/login", "/register"})
    public String submit() {
        return "redirect:/groups";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
