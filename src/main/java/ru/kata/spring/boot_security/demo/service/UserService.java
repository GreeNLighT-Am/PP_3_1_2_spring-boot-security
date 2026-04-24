package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void addUser(User user);

    Optional<User> findUserByName(String username);

    Optional<User> findUserByEmail(String email);

    List<User> showAllUsers();

    Optional<User> showUserById(int id);

    void updateUser(User user);

    void deleteUserById(int id);

    boolean isNameUnique(String name, Integer userId);

    boolean isEmailUnique(String name, Integer userId);

}
