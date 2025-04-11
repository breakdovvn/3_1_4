package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public void add(User user) {
//        entityManager.persist(user);
//    }
//
//    @Override
//    public User getUserById(Long id) {
//        return entityManager.find(User.class, id);
//    }
//
//    @Override
//    public List<User> listUsers() {
//        return entityManager.createQuery("FROM User", User.class).getResultList();
//    }
//
//    @Override
//    public void update(User user) {
//        entityManager.merge(user);
//    }
//
//    @Override
//    public void delete(Long id) {
//        User user = getUserById(id);
//        if (user != null) {
//            entityManager.remove(user);
//        }
//    }
}
