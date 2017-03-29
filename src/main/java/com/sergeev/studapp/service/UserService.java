package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private static final UserDao USER_DAO = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getUserDao();

    public static User createStudent(String firstName, String lastName, String groupId) {
        User student = UserService.create(firstName, lastName);
        student.setType(User.Role.STUDENT);
        student.setGroup(GroupService.read(groupId));

        try {
            student = USER_DAO.persist(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return student;
    }

    public static User createTeacher(String firstName, String lastName) {
        User teacher = UserService.create(firstName, lastName);
        teacher.setType(User.Role.TEACHER);

        try {
            teacher = USER_DAO.persist(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public static User read(String id) {
        User user = null;

        try {
            user = USER_DAO.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static User readByToken(String token) {
        User user = null;

        try {
            user = USER_DAO.getByToken(token);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static User readByLogin(String login, String password) {
        User user = null;

        try {
            user = USER_DAO.getByLogin(login, password);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static List<User> readAll(User.Role type) {
        List<User> users = new ArrayList<>();

        try {
            users = USER_DAO.getAll(type);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<User> readByGroup(String groupId) {
        List<User> users = new ArrayList<>();

        try {
            users = USER_DAO.getByGroup(groupId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static User updateStudent(String firstName, String lastName, String groupId, String userId) {
        User student = UserService.update(firstName, lastName, userId);
        student.setType(User.Role.STUDENT);
        student.setGroup(GroupService.read(groupId));

        try {
            USER_DAO.update(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return student;
    }

    public static User updateTeacher(String firstName, String lastName, String userId) {
        User teacher = UserService.update(firstName, lastName, userId);
        teacher.setType(User.Role.TEACHER);

        try {
            USER_DAO.update(teacher);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return teacher;
    }

    public static User.Role delete(String id) {
        User user = UserService.read(id);
        User.Role type = user.getType();
        String accountId = user.getAccount().getId();

        try {
            USER_DAO.delete(id);
            AccountService.delete(accountId);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return type;
    }

    public static List<User> find(User.Role type, String name) {
        List<User> users = new ArrayList<>();

        try {
            users = USER_DAO.getByName(name, type);
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
        Account account = AccountService.create(user);
        user.setAccount(account);
        return user;
    }

    private static User update(String firstName, String lastName, String userId) {
        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        Account account = AccountService.update(user);
        user.setAccount(account);
        return user;
    }

}
