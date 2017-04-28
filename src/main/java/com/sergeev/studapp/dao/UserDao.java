package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.User;

import java.util.List;

public interface UserDao extends GenericDao<User>{

    User getByLogin(String login, String password);

    User getByToken(String token);

    List<User> getAll(User.Role type);

    List<User> getByGroup(Integer groupId);

    List<User> getByName(String name, User.Role type);

}
