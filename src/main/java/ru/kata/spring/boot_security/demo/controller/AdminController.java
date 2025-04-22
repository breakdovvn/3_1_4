package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showAdminPanel(Model model, @AuthenticationPrincipal User currentUser) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.listRoles());
        model.addAttribute("currentUser", currentUser);
        return "admin";
    }

    @PostMapping("/new")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.listUsers());
            model.addAttribute("roles", userService.listRoles());
            return "admin";
        }
        try {
            userService.add(user);
        } catch (IllegalArgumentException e) {
            result.rejectValue("username", "error.user", e.getMessage());
            model.addAttribute("users", userService.listUsers());
            model.addAttribute("roles", userService.listRoles());
            return "admin";
        }
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String showEditUserForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("allRoles", userService.listRoles());
        return "admin";
    }

    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.listUsers());
            model.addAttribute("roles", userService.listRoles());
            return "admin";
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
