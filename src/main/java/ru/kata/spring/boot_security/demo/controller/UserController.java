package ru.kata.spring.boot_security.demo.controller;

import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String showUserInfo(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.listRoles());
        return "admin";
    }

    @PostMapping("/admin/new")
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
            return "admin";
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String showEditUserForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("allRoles", userService.listRoles());
        return "admin";
    }

    @PostMapping("/admin/edit")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.listUsers());
            model.addAttribute("roles", userService.listRoles());
            return "admin";
        }
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
