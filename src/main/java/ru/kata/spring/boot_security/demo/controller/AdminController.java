package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("titleTable", "Список всех пользователей:");
        return "admin";
    }

    @GetMapping("/{id}")
    public String showUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("titleTable", "Страница пользователя:");
        return "user";
    }

    @GetMapping("/addUser")
    public String addNewUser(Model model, @ModelAttribute("user") User user ) {
        List<Role> roles = roleService.getUniqAllRoles();
        model.addAttribute("rolesAdd", roles);
        return "newUser";
    }

    @PostMapping
    public String addCreateNewUser(@ModelAttribute("user") User user,Model model) {
        try {
            userService.createNewUser(user);
        } catch (Exception er) {
            System.err.println("Пользователь с таким email уже существует!");
        }return "redirect:/admin";
    }

    @GetMapping("/{id}/editUser")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUser(id));
        List<Role> roles = roleService.getUniqAllRoles();
        model.addAttribute("rolesAdd", roles);
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user) {
        try{
            userService.updateUser(user);
        }catch (Exception e) {
            System.err.println("Пользователь с таким email уже существует!");
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
        }
    }

