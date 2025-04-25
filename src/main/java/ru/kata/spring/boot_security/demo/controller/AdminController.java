package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        model.addAttribute("activeTab", "users");
        return "admin";
    }

    @PostMapping("/new")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result,
                          Model model, @AuthenticationPrincipal User currentUser) {
        if (result.hasErrors()) {
            model.addAttribute("activeTab", "form");
            model.addAttribute("users", userService.listUsers());
            model.addAttribute("allRoles", userService.listRoles());
            model.addAttribute("currentUser", currentUser);
            return "admin";
        }
        try {
            userService.add(user);
        } catch (IllegalArgumentException e) {
            result.rejectValue("username", "error.user", e.getMessage());
            model.addAttribute("users", userService.listUsers());
            model.addAttribute("allRoles", userService.listRoles());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("activeTab", "form");
            return "admin";
        }
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("allRoles", userService.listRoles());
        return "admin";
    }

    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,
                             @AuthenticationPrincipal User currentUser) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.listUsers());
            model.addAttribute("allRoles", userService.listRoles());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("activeTab", "users");
            model.addAttribute("showEditModal", true);
            return "admin";
        }

        User existingUser = userService.getUserById(user.getId());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }

        userService.update(user);

        return "redirect:/admin";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}


