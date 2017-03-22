package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.User;

import java.util.List;

public interface UserDao extends GenericDao<User>{

    List<User> getByName(String name, User.AccountType type) throws PersistentException;

    List<User> getByGroup(String groupId) throws PersistentException;

    List<User> getAll(User.AccountType type) throws PersistentException;

    User getAccountInfo(String login) throws PersistentException;
}
