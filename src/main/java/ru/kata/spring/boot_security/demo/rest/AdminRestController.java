package ru.kata.spring.boot_security.demo.rest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AdminRestController {

    private final UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    // Получить всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userService.listUsers();
    }

    // Получить одного пользователя по id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user, BindingResult br) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(br));
        }
        try {
            userService.add(user);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("username", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody @Valid User user,
                                        BindingResult br) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(br));
        }
        user.setId(id);

        try {
            userService.update(user);
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("username", "Пользователь с таким username уже существует"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("username", e.getMessage()));
        }
    }


    private Map<String, String> getErrors(BindingResult br) {
        Map<String, String> errs = new HashMap<>();
        br.getFieldErrors().forEach(fe -> errs.put(fe.getField(), fe.getDefaultMessage()));
        return errs;
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}