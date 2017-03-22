package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.User;

public class UserService {

    private DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);

    public User createStudent(String firstName, String lastName, String groupId){
        User student = createUser(firstName, lastName);
        student.setType(User.AccountType.STUDENT);

        try {
            student.setGroup(daoFactory.getGroupDao().getById(groupId));
            student = daoFactory.getUserDao().persist(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return student;
    }

    public User createTeacher(String firstName, String lastName){
        User teacher = createUser(firstName, lastName);
        teacher.setType(User.AccountType.TEACHER);

        try {
            teacher = daoFactory.getUserDao().persist(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public User updateStudent(String firstName, String lastName, String userId, String groupId){
        User student = updateUser(userId, firstName, lastName);
        student.setType(User.AccountType.STUDENT);

        try {
            student.setGroup(daoFactory.getGroupDao().getById(groupId));
            daoFactory.getUserDao().update(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return student;
    }

    public User updateTeacher(String firstName, String lastName, String userId){
        User teacher = updateUser(userId, firstName, lastName);
        teacher.setType(User.AccountType.TEACHER);

        try {
            daoFactory.getUserDao().update(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public User.AccountType deleteUser(String userId){
        User.AccountType type = null;
        try {
            type = daoFactory.getUserDao().getById(userId).getType();
            daoFactory.getUserDao().delete(userId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
        return type;
    }

    private User createUser(String firstName, String lastName){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(firstName.toLowerCase() + "_" + lastName.toLowerCase());
        user.setPassword("pass");
        return user;
    }

    private User updateUser(String firstName, String lastName, String userId){
        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

}
