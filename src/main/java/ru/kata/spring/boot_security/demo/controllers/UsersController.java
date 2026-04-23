package ru.kata.spring.boot_security.demo.controllers;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final RoleRepository roleRepository;


    @GetMapping()
    public String showUsers(Model model) {

        model.addAttribute("users", userService.showAllUsers());

        return "users/users";
    }

    @GetMapping("/user")
    public String showUser(@RequestParam(value = "id", required = false) Integer id, Model model) {

        if (id == null || id <= 0) {
            model.addAttribute("errorMessage", "Ошибка отображения пользователя: значение id не может быть пустым, 0 или отрицательным.");
            return "users/users";
        } else {
            try {
                model.addAttribute("user", userService.showUserById(id));
            } catch (EntityNotFoundException e) {
                model.addAttribute("errorMessage", String.format("Ошибка отображения пользователя: %s", e.getMessage()));
                return "users/users";
            }
        }

        return "users/user";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("allRoles", roleRepository.findAll());
        return "users/new";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult userBindingResult,
                          @RequestParam(value = "roles", required = false) List<Integer> roleIds,
                          RedirectAttributes redirectAttributes) {

        if (userBindingResult.hasErrors()) {
            return "users/new";
        }

        // Обработка выбора ролей
        if (roleIds != null && !roleIds.isEmpty()) {
            Collection<Role> selectedRoles = roleRepository.findByIdIn(roleIds);
            user.setRoles(selectedRoles);
        } else {
            user.setRoles(Collections.emptyList());
        }

        userService.addUser(user);
        redirectAttributes.addFlashAttribute("successMessage",
                String.format("Пользователь %s успешно создан.", user.getName()));
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id == null || id <= 0) {
            model.addAttribute("errorMessage", "Ошибка редактирования пользователя: значение id не может быть пустым, 0 или отрицательным.");
            return "users/users";
        } else {
            try {
                User user = userService.showUserById(id);
                model.addAttribute("user", user);
                model.addAttribute("allRoles", roleRepository.findAll());
            } catch (EntityNotFoundException e) {
                model.addAttribute("errorMessage", String.format("Ошибка редактирования пользователя: %s", e.getMessage()));
                return "users/users";
            }
        }
        return "users/edit";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult userBindingResult,
                             @RequestParam(value = "roles", required = false) List<Integer> roleIds,
                             RedirectAttributes redirectAttributes) {

        if (userBindingResult.hasErrors()) {
            return "users/edit";
        }

        // Обработка выбора ролей
        if (roleIds != null && !roleIds.isEmpty()) {
            Collection<Role> selectedRoles = roleRepository.findByIdIn(roleIds);
            user.setRoles(selectedRoles);
        } else {
            user.setRoles(Collections.emptyList());
        }

        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно обновлён.");

        return "redirect:/users/user?id=" + user.getId();
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Integer id, RedirectAttributes redirectAttributes) {
        try {
            String userName = userService.showUserById(id).getName();
            userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("successMessage", String.format("Пользователь %s успешно удалён.", userName));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", String.format("Ошибка при удалении пользователя: %s", e.getMessage()));
        }

//    } catch (EntityNotFoundException e) {
//        redirectAttributes.addFlashAttribute("errorMessage", String.format("Пользователь с id=%d не найден.", id));
//    } catch (IllegalArgumentException e) {
//        redirectAttributes.addFlashAttribute("errorMessage", "Некорректный id пользователя.");
//    }

        return "redirect:/users";
    }

}
