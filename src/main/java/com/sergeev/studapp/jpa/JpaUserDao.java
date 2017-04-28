package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class JpaUserDao extends JpaGenericDao<User> implements UserDao {

    @Override
    public List<User> getByName(String name, User.Role role) throws PersistentException {
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM User u WHERE lower(u.firstName||' '||u.lastName) LIKE ?1 AND u.role = ?2", User.class);
        return query.setParameter(1, "%" + name.toLowerCase() + "%")
                .setParameter(2, role).getResultList();
    }

    @Override
    public List<User> getByGroup(Integer groupId) throws PersistentException {
        return new ArrayList<>(entityManager.find(Group.class, groupId).getStudents());
    }

    @Override
    public List<User> getAll(User.Role type) throws PersistentException {
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM User u WHERE u.role = ?1", User.class);
        return query.setParameter(1, type).getResultList();
    }

    @Override
    public User getByToken(String token) throws PersistentException {
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM Account a, User u WHERE u.account = a AND a.token = ?1", User.class);
        return query.setParameter(1, token).getSingleResult();
    }

    @Override
    public User getByLogin(String login, String password) throws PersistentException {
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM Account a, User u WHERE u.account = a AND a.login = ?1 AND a.password = ?2", User.class);
        return query.setParameter(1, login)
                .setParameter(2, password).getSingleResult();
    }
}
