package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private static final UserDao USER_DAO = DaoFactory.getDaoFactory().getUserDao();

    public static User addStudent(String firstName, String lastName, int groupId) {
        User user = UserService.add(firstName, lastName)
                .setRole(User.Role.STUDENT)
                .setGroup(GroupService.get(groupId));
        USER_DAO.save(user);
        return user;
    }

    public static User addTeacher(String firstName, String lastName) {
        User user = UserService.add(firstName, lastName).setRole(User.Role.TEACHER);
        USER_DAO.save(user);
        return user;
    }

    public static User updateStudent(String firstName, String lastName, int groupId, int userId) {
        User user = update(firstName, lastName, userId)
                .setGroup(GroupService.get(groupId));
        USER_DAO.update(user);
        return user;
    }

    public static User updateTeacher(String firstName, String lastName, int userId) {
        User user = update(firstName, lastName, userId);
        USER_DAO.update(user);
        return user;
    }

    public static User updateAccount(String token, int userId) {
        User user = USER_DAO.getById(userId);
        user.getAccount().setToken(token);
        USER_DAO.update(user);
        return user;
    }

    public static User.Role remove(int id) {
        User user = get(id);
        User.Role role = user.getRole();
        USER_DAO.remove(id);
        return role;
    }

    public static User get(int id) {
        return USER_DAO.getById(id);
    }

    public static List<User> getByGroup(int groupId) {
        return USER_DAO.getByGroup(groupId);
    }

    public static User getByToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        return USER_DAO.getByToken(token);
    }

    public static User getByLogin(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        return USER_DAO.getByLogin(login, password);
    }

    public static List<User> getAll(User.Role role) {
        return USER_DAO.getAll(role);
    }

    public static List<User> find(User.Role role, String name) {
        if (role == null || name == null || name.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        return USER_DAO.getByName(name, role);
    }

    public static Map<Course, Double> studentAvgMarks(int id) {
        User user = get(id);
        List<Course> courses = CourseService.getByGroup(user.getGroup().getId());
        Map<Course, Double> coursesMarks = new LinkedHashMap<>();

        for (Course course : courses) {
            double avgMark = MarkService.getAvgMark(id, course.getDiscipline().getId());
            coursesMarks.put(course, avgMark);
        }
        return coursesMarks;
    }

    private static User add(String firstName, String lastName) {
        if (!checkName(firstName, lastName)) {
            throw new ApplicationException("Bad parameters.");
        }
        User user = new User()
                .setFirstName(firstName)
                .setLastName(lastName);
        User.Account account = new User.Account()
                .setPassword(generatePassword())
                .setLogin(generateLogin(firstName, lastName));
        user.setAccount(account);
        return user;
    }

    private static User update(String firstName, String lastName, int userId) {
        if (!checkName(firstName, lastName)) {
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

    private UserService() {
    }

}
