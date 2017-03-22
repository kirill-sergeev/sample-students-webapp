package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
    private static UserDao userDao = daoFactory.getUserDao();
    private static GroupDao groupDao = daoFactory.getGroupDao();

    public static User createStudent(String firstName, String lastName, String groupId){
        User student = create(firstName, lastName);
        student.setType(User.AccountType.STUDENT);

        try {
            student.setGroup(groupDao.getById(groupId));
            student = userDao.persist(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return student;
    }

    public static User createTeacher(String firstName, String lastName){
        User teacher = create(firstName, lastName);
        teacher.setType(User.AccountType.TEACHER);

        try {
            teacher = userDao.persist(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public static List<User> readAll(User.AccountType type){
        List<User> users = new ArrayList<>();

        try {
            users = userDao.getAll(type);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static User updateStudent(String firstName, String lastName, String userId, String groupId){
        User student = update(userId, firstName, lastName);
        student.setType(User.AccountType.STUDENT);

        try {
            student.setGroup(groupDao.getById(groupId));
            userDao.update(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return student;
    }

    public static User updateTeacher(String firstName, String lastName, String userId){
        User teacher = update(userId, firstName, lastName);
        teacher.setType(User.AccountType.TEACHER);

        try {
            userDao.update(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public static User.AccountType delete(String userId){
        User.AccountType type = null;
        try {
            type = userDao.getById(userId).getType();
            userDao.delete(userId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
        return type;
    }

    private static User create(String firstName, String lastName){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(firstName.toLowerCase() + "_" + lastName.toLowerCase());
        user.setPassword("pass");
        return user;
    }

    private static User update(String firstName, String lastName, String userId){
        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

}
