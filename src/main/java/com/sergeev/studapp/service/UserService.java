package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private static UserDao userDao = DaoFactory.getDaoFactory().getUserDao();

    public static User addStudent(String firstName, String lastName, int groupId) {
        User user = null;
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            user = add(firstName, lastName)
                    .setRole(User.Role.STUDENT)
                    .setGroup(dao.getGroupDao().getById(groupId));
            dao.getUserDao().save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User addTeacher(String firstName, String lastName) {
        User user = null;
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
            user = add(firstName, lastName)
                    .setRole(User.Role.TEACHER);
            dao.getUserDao().save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User updateStudent(String firstName, String lastName, int groupId, int userId) {
        User user = null;
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            user = update(firstName, lastName, userId)
                    .setGroup(dao.getGroupDao().getById(groupId));
            dao.getUserDao().update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User updateTeacher(String firstName, String lastName, int userId) {
        User user = null;
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
            user = update(firstName, lastName, userId);
            dao.getUserDao().update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User updateAccount(String token, int userId) {
        User user = null;
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            user = dao.getUserDao().getById(userId);
                   user.getAccount().setToken(token);
            dao.getUserDao().update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User.Role remove(int id) {
        User.Role role = null;
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            role = dao.getUserDao().getById(id).getRole();
            dao.getUserDao().remove(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    public static User get(int id) {
        return userDao.getById(id);
    }

    public static List<User> getByGroup(int groupId) {
        List<User> users;
        try {
            users = userDao.getByGroup(groupId);
        } catch (PersistentException e){
            users = Collections.emptyList();
        }
        return users;
    }

    public static User getByToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        return userDao.getByToken(token);
    }

    public static User getByLogin(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        return userDao.getByLogin(login, password);
    }

    public static List<User> getAll(User.Role role) {
        List<User> users;
        try {
            users = userDao.getAll(role);
        } catch (PersistentException e){
            users = Collections.emptyList();
        }
        return users;
    }

    public static List<User> find(User.Role role, String name) {
        if (role == null || name == null || name.isEmpty()) {
            throw new ApplicationException("Bad parameters.");
        }
        List<User> users;
        try {
            users = userDao.getByName(name, role);
        } catch (PersistentException e){
            users = Collections.emptyList();
        }
        return users;
    }

    public static Map<Course, Double> studentAvgMarks(int id) {
        Map<Course, Double> coursesMarks = new LinkedHashMap<>();
        try(DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            User user = dao.getUserDao().getById(id);
            List<Course> courses = dao.getCourseDao().getByGroup(user.getGroup().getId());
            for (Course course : courses) {
                double avgMark = dao.getMarkDao().getAvgMark(id, course.getDiscipline().getId());
                coursesMarks.put(course, avgMark);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        User user = userDao.getById(userId);
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
