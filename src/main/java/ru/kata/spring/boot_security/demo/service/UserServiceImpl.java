package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private Set<Role> fetchRealRoles(Set<Role> rawRoles) {
        Set<Role> realRoles = new HashSet<>();
        for (Role role : rawRoles) {
            roleRepository.findById(role.getId()).ifPresent(realRoles::add);
        }
        return realRoles;
    }


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void add(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Пользователь с таким username уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(fetchRealRoles(user.getRoles()));
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public List<User> listUsers() {
//        return userRepository.findAll();
        List<User> users = userRepository.findAll();
        System.out.println("ПОЛЬЗОВАТЕЛИ ИЗ БД:");
        users.forEach(u -> System.out.println(u.getId() + " " + u.getUsername()));
        return users;
    }
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    public void update(User user) {
        user.setRoles(fetchRealRoles(user.getRoles()));
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
