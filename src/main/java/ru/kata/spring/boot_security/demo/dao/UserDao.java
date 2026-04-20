package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDao {

    void addUser(User user);

    List<User> showAllUsers();

    User showUserById(int id);

    void updateUser(User updatedUser);

    void deleteUserById(int id);

}
