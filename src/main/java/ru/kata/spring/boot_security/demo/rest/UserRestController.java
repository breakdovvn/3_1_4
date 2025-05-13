package ru.kata.spring.boot_security.demo.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;

@RestController
public class UserRestController {
    @GetMapping("/api/currentUser")
    public User getCurrentUser(@AuthenticationPrincipal User user) {
        return user;
    }
}

