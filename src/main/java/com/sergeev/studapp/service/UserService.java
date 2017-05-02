package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.User;

import java.util.*;

public class UserService {

    private static final UserDao USER_DAO = DaoFactory.getDaoFactory().getUserDao();

    public static User createStudent(String firstName, String lastName, Integer groupId) throws ApplicationException {
        User user = UserService.create(firstName, lastName);
        user.setRole(User.Role.STUDENT);
        user.setGroup(GroupService.read(groupId));

        try {
            USER_DAO.save(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save student.", e);
        }

        return user;
    }

    public static User createTeacher(String firstName, String lastName) throws ApplicationException {
        User user = UserService.create(firstName, lastName);
        user.setRole(User.Role.TEACHER);

        try {
            USER_DAO.save(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save teacher.", e);
        }

        return user;
    }

    public static User read(Integer id) {
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

    public static User readByToken(String token) {
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

    public static User readByLogin(String login, String password) {
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
            e.printStackTrace();
            users = Collections.emptyList();
        }

        return users;
    }

    public static List<User> readByGroup(Integer groupId) {
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

    public static User updateStudent(String firstName, String lastName, Integer groupId, Integer userId) {
        User user = update(firstName, lastName, userId);
        user.setGroup(GroupService.read(groupId));

        try {
            USER_DAO.update(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update student.", e);
        }

        return user;
    }

    public static User updateTeacher(String firstName, String lastName, Integer userId) {
        User user = update(firstName, lastName, userId);

        try {
            USER_DAO.update(user);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update teacher.", e);
        }

        return user;
    }

    public static User updateAccount(String token, Integer userId) {
        User user = USER_DAO.getById(userId);
        user.getAccount().setToken(token);
        USER_DAO.update(user);
        return user;
    }

    public static User.Role delete(Integer id) {
        if (id == null) {
            throw new ApplicationException("Bad parameters.");
        }

        User user = read(id);
        User.Role role = user.getRole();

        try {
            USER_DAO.remove(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot remove user, because user not found.", e);
        }

        return role;
    }

    public static List<User> find(User.Role role, String name) {
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

    public static Map<Course, Double> studentAvgMarks(Integer id) {
        User user = read(id);
        List<Course> courses = CourseService.readByGroup(user.getGroup().getId());
        Map<Course, Double> coursesMarks = new LinkedHashMap<>();

        for (Course course : courses) {
            double avgMark = MarkService.calculateAvgMark(id, course.getDiscipline().getId());
            coursesMarks.put(course, avgMark);
        }

        return coursesMarks;
    }

    private static User create(String firstName, String lastName) {
        if (!checkName(firstName, lastName)) {
            throw new ApplicationException("Bad parameters.");
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);

        User.Account account = new User.Account()
                .setPassword(generatePassword())
                .setLogin(generateLogin(firstName, lastName));
        user.setAccount(account);
        return user;
    }

    private static User update(String firstName, String lastName, Integer userId) {
        if (userId == null || !checkName(firstName, lastName)) {
            throw new ApplicationException("Bad parameters.");
        }

        User user = USER_DAO.getById(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    private static boolean checkName(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            return false;
        }
        String expression = "(?u)^\\p{Lu}[\\p{Ll}-]{1,29}$";
        return firstName.matches(expression) && lastName.matches(expression);
    }

    private static String generateLogin(String firstName, String lastName) {
        return (firstName + "_" + lastName).toLowerCase();
    }

    private static String generatePassword() {
        Random random = new Random();
        return String.valueOf(random.nextInt(899999) + 100000);
    }
    
}
