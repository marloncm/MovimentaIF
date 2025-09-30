package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.User;
import com.ifrs.movimentaif.movimentaifapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public void newUser(@RequestBody User user) throws Exception {
        User existingUser = userService.getUserById(user.getUserId());
        if (existingUser == null) {
            userService.saveUser(user);
        }
        // Se existir, não faz nada (apenas retorna 200 OK)
    }

    @GetMapping("/role/{role}")
    public List<User> getUserByRole(@PathVariable String role) throws ExecutionException, InterruptedException{
        return userService.getUsersByRole(role);
    }

    @GetMapping("/{uid}")
    public User getUserById(@PathVariable String uid) throws ExecutionException, InterruptedException{
        return userService.getUserById(uid);
    }

    @GetMapping("/appusers")
    public List<User> getAppUsers() throws ExecutionException, InterruptedException{
        return userService.getAppUsers();
    }

    @GetMapping("/status/{status}")
    public List<User> getUserByStatus(@PathVariable String status) throws ExecutionException, InterruptedException{
        return userService.getUsersByStatus(status);
    }

    @PutMapping("/{uid}")
    public User updateUser(@PathVariable String uid, @RequestBody User user) throws ExecutionException, InterruptedException {
        return userService.updateUser(uid, user);
    }
}
