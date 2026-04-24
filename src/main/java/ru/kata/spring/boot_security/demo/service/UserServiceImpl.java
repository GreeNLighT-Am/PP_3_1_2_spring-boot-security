package ru.kata.spring.boot_security.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserRepository userRepository;

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public List<User> showAllUsers() {
        return userDao.showAllUsers();
    }

    @Override
    public Optional<User> showUserById(int id) {
        return userDao.showUserById(id);
    }

    @Override
    public void updateUser(User updatedUser) {
        userDao.updateUser(updatedUser);
    }

    @Override
    public void deleteUserById(int id) {
        userDao.deleteUserById(id);
    }

    @Override
    public boolean isNameUnique(String name, Integer userId) {
        Optional<User> existingUser = userRepository.findUserByName(name);
        if (existingUser.isEmpty()) {
            return true; // Имя свободно
        }
        // Если userId не null, проверяем, что это тот же пользователь (редактирование)
        if (userId != null && existingUser.get().getId() == userId) {
            return true; // Это тот же пользователь — уникальность сохраняется
        }
        return false; // Найден другой пользователь с таким именем
    }

    @Override
    public boolean isEmailUnique(String email, Integer userId) {
        Optional<User> existingUser = userRepository.findUserByEmail(email);
        if (existingUser.isEmpty()) {
            return true; // Email свободно
        }
        // Если userId не null, проверяем, что это тот же пользователь (редактирование)
        if (userId != null && existingUser.get().getId() == userId) {
            return true; // Это тот же пользователь — уникальность сохраняется
        }
        return false; // Найден другой пользователь с таким email
    }

}
