package ru.kata.spring.boot_security.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public DataInitializer(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        if (roleRepository.findByName("ROLE_USER") == null) {
            roleRepository.save(new Role("ROLE_USER"));
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");

        if (userService.listUsers().isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setRoles(Set.of(adminRole, userRole));
            userService.add(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.setFirstName("User");
            user.setLastName("Userov");
            user.setRoles(Set.of(userRole));
            userService.add(user);
        }
    }
}

