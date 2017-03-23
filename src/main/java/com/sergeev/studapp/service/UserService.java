package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private static UserDao userDao = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getUserDao();

    public static User createStudent(String firstName, String lastName, String groupId) {
        User student = UserService.create(firstName, lastName);
        student.setType(User.AccountType.STUDENT);
        student.setGroup(GroupService.read(groupId));

        try {
            student = userDao.persist(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return student;
    }

    public static User createTeacher(String firstName, String lastName) {
        User teacher = UserService.create(firstName, lastName);
        teacher.setType(User.AccountType.TEACHER);

        try {
            teacher = userDao.persist(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public static User read(String id) {
        User user = null;

        try {
            user = userDao.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static List<User> readAll(User.AccountType type) {
        List<User> users = null;

        try {
            users = userDao.getAll(type);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<User> readByGroup(String groupId) {
        List<User> users = null;

        try {
            users = userDao.getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static User updateStudent(String firstName, String lastName, String userId, String groupId) {
        User student = UserService.update(userId, firstName, lastName);
        student.setType(User.AccountType.STUDENT);
        student.setGroup(GroupService.read(groupId));

        try {
            userDao.update(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return student;
    }

    public static User updateTeacher(String firstName, String lastName, String userId) {
        User teacher = UserService.update(userId, firstName, lastName);
        teacher.setType(User.AccountType.TEACHER);

        try {
            userDao.update(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public static User.AccountType delete(String id) {
        User.AccountType type = UserService.read(id).getType();

        try {
            userDao.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return type;
    }

    public static List<User> find(User.AccountType type, String name) {
        List<User> users = null;

        try {
            users = userDao.getByName(name, type);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static Map<Course, Double> studentAvgMarks(String id) {
        User student = UserService.read(id);
        List<Course> courses = CourseService.readByDiscipline(student.getGroup().getId());
        Map<Course, Double> coursesMarks = new LinkedHashMap<>();

        for (Course course : courses) {
            double avgMark = MarkService.calculateAvgMark(id, course.getDiscipline().getId());
            coursesMarks.put(course, avgMark);
        }

        return coursesMarks;
    }

    private static User create(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(firstName.toLowerCase() + "_" + lastName.toLowerCase());
        user.setPassword("pass");
        return user;
    }

    private static User update(String firstName, String lastName, String userId) {
        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

}
