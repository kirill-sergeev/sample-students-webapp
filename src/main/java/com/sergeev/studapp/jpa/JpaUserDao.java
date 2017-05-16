package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class JpaUserDao extends JpaGenericDao<User> implements UserDao {

    @Override
    public User getByToken(String token) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM User u WHERE u.account.token = ?1", User.class);
        User user = query.setParameter(1, token).getSingleResult();
        entityManager.close();
        return user;
    }

    @Override
    public User getByLogin(String login, String password) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM User u WHERE u.account.login = ?1 AND u.account.password = ?2", User.class);
        User user = query.setParameter(1, login)
                .setParameter(2, password).getSingleResult();
        entityManager.close();
        return user;
    }

    @Override
    public List<User> getByName(String name, User.Role role) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM User u WHERE lower(u.firstName||' '||u.lastName) LIKE ?1 AND u.role = ?2", User.class);
        List<User> users = query.setParameter(1, "%" + name.toLowerCase() + "%")
                .setParameter(2, role).getResultList();
        entityManager.close();
        return users;
    }

    @Override
    public List<User> getByGroup(Integer groupId) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        List<User> users = new ArrayList<>(entityManager.find(Group.class, groupId).getStudents());
        entityManager.close();
        return users;
    }

    @Override
    public List<User> getAll(User.Role type) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM User u WHERE u.role = ?1", User.class);
        List<User> users = query.setParameter(1, type).getResultList();
        entityManager.close();
        return users;
    }

}
