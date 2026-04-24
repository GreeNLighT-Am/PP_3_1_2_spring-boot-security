package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> showAllUsers() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles", User.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> showUserById(int id) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id", User.class);
        query.setParameter("id", id);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional
    public void updateUser(User updatedUser) {
        entityManager.merge(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserById(int id) {
        showUserById(id).ifPresent(entityManager::remove);
    }

}
