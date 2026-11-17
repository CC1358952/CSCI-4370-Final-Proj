package uga.cs4370.sleighlist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /** /login?error shows the failed-login message (backend redirects here on bad credentials). */
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("title", "Log in");
        if (error != null) model.addAttribute("error", "That username and password don't match. Check them and try again.");
        return "login";
    }

    /** /register?error=taken|mismatch shows a sign-up error. */
    @GetMapping("/register")
    public String register(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("title", "Create account");
        if ("taken".equals(error)) model.addAttribute("error", "That username is taken. Pick another one.");
        if ("mismatch".equals(error)) model.addAttribute("error", "The passwords don't match. Type them again.");
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
