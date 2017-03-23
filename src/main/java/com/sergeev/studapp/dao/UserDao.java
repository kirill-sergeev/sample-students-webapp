package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.User;

import java.util.List;

public interface UserDao extends GenericDao<User>{

    List<User> getByName(String name, User.Role type) throws PersistentException;

    List<User> getByGroup(String groupId) throws PersistentException;

    List<User> getAll(User.Role type) throws PersistentException;
}
