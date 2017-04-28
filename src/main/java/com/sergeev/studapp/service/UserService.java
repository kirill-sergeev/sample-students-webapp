package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private static final UserDao USER_DAO = DaoFactory.getDaoFactory().getUserDao();

    public static User createStudent(String firstName, String lastName, Integer groupId) throws ApplicationException {
        User user = UserService.create(firstName, lastName);
        user.setRole(User.Role.STUDENT);
        user.setGroup(GroupService.read(groupId));

        try {
            user = USER_DAO.save(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save student.", e);
        }

        return user;
    }

    public static User createTeacher(String firstName, String lastName) throws ApplicationException {
        User user = UserService.create(firstName, lastName);
        user.setRole(User.Role.TEACHER);

        try {
            user = USER_DAO.save(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save teacher.", e);
        }

        return user;
    }

    public static User createAdmin(String firstName, String lastName) throws ApplicationException {
        User user = UserService.create(firstName, lastName);
        user.setRole(User.Role.ADMIN);

        try {
            user = USER_DAO.save(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save admin.", e);
        }

        return user;
    }

    public static User read(Integer id) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException("Bad parameters.");
        }
        User user;

        try {
            user = USER_DAO.getById(id);
        } catch (PersistentException e) {
            throw new ApplicationException("User not found.", e);
        }

        return user;
    }

    public static User readByToken(String token) throws ApplicationException {
        if (token == null || token.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        User user;

        try {
            user = USER_DAO.getByToken(token);
        } catch (PersistentException e) {
            throw new ApplicationException("User not found.", e);
        }

        return user;
    }

    public static User readByLogin(String login, String password) throws ApplicationException {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }

        User user;
        try {
            user = USER_DAO.getByLogin(login, password);
        } catch (PersistentException e) {
            throw new ApplicationException("User not found.", e);
        }

        return user;
    }

    public static List<User> readAll(User.Role role) {

        List<User> users;
        try {
            users = USER_DAO.getAll(role);
        } catch (PersistentException e) {
            users = Collections.emptyList();
        }

        return users;
    }

    public static List<User> readByGroup(Integer groupId) throws ApplicationException {
        if (groupId == null) {
            throw new ApplicationException("Bad parameters.");
        }
        List<User> users;

        try {
            users = USER_DAO.getByGroup(groupId);
        } catch (PersistentException e) {
            users = Collections.emptyList();
        }

        return users;
    }

    public static User updateStudent(String firstName, String lastName, Integer groupId, Integer userId) throws ApplicationException {
        User user = UserService.update(firstName, lastName, userId);
        user.setRole(User.Role.STUDENT);
        user.setGroup(GroupService.read(groupId));

        try {
            USER_DAO.update(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update student.", e);
        }

        return user;
    }

    public static User updateTeacher(String firstName, String lastName, Integer userId) throws ApplicationException {
        User user = UserService.update(firstName, lastName, userId);
        user.setRole(User.Role.TEACHER);

        try {
            USER_DAO.update(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update teacher.", e);
        }

        return user;
    }

    public static User.Role delete(Integer id) throws ApplicationException {
        if (id == null) {
            throw new ApplicationException("Bad parameters.");
        }

        User user = UserService.read(id);
        User.Role role = user.getRole();
        Integer accountId = user.getAccount().getId();

        try {
            USER_DAO.remove(id);
            AccountService.delete(accountId);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot remove user, because user not found.", e);
        }

        return role;
    }

    public static List<User> find(User.Role role, String name) throws ApplicationException {
        if (name == null || name.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }

        List<User> users;

        try {
            users = USER_DAO.getByName(name, role);
        } catch (PersistentException e) {
            users = Collections.emptyList();
        }

        return users;
    }

    public static Map<Course, Double> studentAvgMarks(Integer id) throws ApplicationException {
        User user = UserService.read(id);
        List<Course> courses = CourseService.readByGroup(user.getGroup().getId());
        Map<Course, Double> coursesMarks = new LinkedHashMap<>();

        for (Course course : courses) {
            double avgMark = MarkService.calculateAvgMark(id, course.getDiscipline().getId());
            coursesMarks.put(course, avgMark);
        }

        return coursesMarks;
    }

    private static User create(String firstName, String lastName) throws ApplicationException {
        if (!checkName(firstName, lastName)) {
            throw new ApplicationException("Bad parameters.");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        Account account = AccountService.create(user);
        user.setAccount(account);
        return user;
    }

    private static User update(String firstName, String lastName, Integer userId) throws ApplicationException {
        if (userId == null || !checkName(firstName, lastName)) {
            throw new ApplicationException("Bad parameters.");
        }

        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        Account account = AccountService.update(user);
        user.setAccount(account);
        return user;
    }

    private static boolean checkName(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            return false;
        }
        String expression = "(?u)^\\p{Lu}[\\p{Ll}-]{1,29}$";
        return firstName.matches(expression) && lastName.matches(expression);
    }
}
