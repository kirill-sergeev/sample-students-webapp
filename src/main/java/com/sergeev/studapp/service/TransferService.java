package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.*;
import com.sergeev.studapp.mongo.MongoDaoFactory;
import com.sergeev.studapp.postgres.PgDaoFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferService {

    private static List<Account> accounts;
    private static List<Course> courses;
    private static List<Discipline> disciplines;
    private static List<Group> groups;
    private static List<Lesson> lessons;
    private static List<Mark> marks;
    private static List<User> users;

    private static void exportFrom(int database) throws PersistentException {

        DaoFactory dao = DaoFactory.getDaoFactory(database);
        accounts = dao.getAccountDao().getAll();
        courses = dao.getCourseDao().getAll();
        disciplines = dao.getDisciplineDao().getAll();
        groups = dao.getGroupDao().getAll();
        lessons = dao.getLessonDao().getAll();
        marks = dao.getMarkDao().getAll();
        users = dao.getUserDao().getAll();

    }

    private static void importTo(int database) throws PersistentException {

        DaoFactory dao = DaoFactory.getDaoFactory(database);
        Integer oldId;
        Integer newId;
        Map<Integer, Integer> accountIDs = new HashMap<>();
        Map<Integer, Integer> courseIDs = new HashMap<>();
        Map<Integer, Integer> disciplineIDs = new HashMap<>();
        Map<Integer, Integer> groupIDs = new HashMap<>();
        Map<Integer, Integer> lessonIDs = new HashMap<>();
        Map<Integer, Integer> userIDs = new HashMap<>();

        prepareSchema(database);

        for (Account account : accounts) {
            oldId = account.getId();
            account.setId(null);
            String token = account.getToken();
            account = dao.getAccountDao().save(account);
            account.setToken(token);
            dao.getAccountDao().update(account);
            newId = account.getId();
            accountIDs.put(oldId, newId);
        }

        for (Group group : groups) {
            oldId = group.getId();
            group.setId(null);
            group = dao.getGroupDao().save(group);
            newId = group.getId();
            groupIDs.put(oldId, newId);
        }

        for (User user : users) {
            oldId = user.getId();
            user.setId(null);
            user.setAccount(new Account().setId(accountIDs.get(user.getAccount().getId())));
            if (user.getRole() == User.Role.STUDENT) {
                user.setGroup(new Group().setId(groupIDs.get(user.getGroup().getId())));
            }
            user = dao.getUserDao().save(user);
            newId = user.getId();
            userIDs.put(oldId, newId);
        }

        for (Discipline discipline : disciplines) {
            oldId = discipline.getId();
            discipline.setId(null);
            discipline = dao.getDisciplineDao().save(discipline);
            newId = discipline.getId();
            disciplineIDs.put(oldId, newId);
        }

        for (Course course : courses) {
            oldId = course.getId();
            course.setId(null);
            course.setDiscipline(new Discipline().setId(disciplineIDs.get(course.getDiscipline().getId())));
            course.setGroup(new Group().setId(groupIDs.get(course.getGroup().getId())));
            course.setTeacher(new User().setId(userIDs.get(course.getTeacher().getId())));
            course = dao.getCourseDao().save(course);
            newId = course.getId();
            courseIDs.put(oldId, newId);
        }

        for (Lesson lesson : lessons) {
            oldId = lesson.getId();
            lesson.setId(null);
            lesson.setCourse(new Course().setId(courseIDs.get(lesson.getCourse().getId())));
            lesson = dao.getLessonDao().save(lesson);
            newId = lesson.getId();
            lessonIDs.put(oldId, newId);
        }

        for (Mark mark : marks) {
            mark.setId(null);
            mark.setLesson(new Lesson().setId(lessonIDs.get(mark.getLesson().getId())));
            mark.setStudent(new User().setId(userIDs.get(mark.getStudent().getId())));
            dao.getMarkDao().save(mark);
        }
    }

    private static void prepareSchema(int database) {
        if (database == DaoFactory.POSTGRES) {
            String schema = null;
            String data = null;
            try {
                schema = new String(Files.readAllBytes(Paths.get("pg_schema.sql")), Charset.forName("UTF8"));
                data = new String(Files.readAllBytes(Paths.get("pg_data.sql")), Charset.forName("UTF8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sql = schema + data;
            try (Connection connection = PgDaoFactory.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (database == DaoFactory.MONGO) {
            MongoDaoFactory.getConnection().drop();
        }
    }

    public static void main(String[] args) throws PersistentException {
        exportFrom(DaoFactory.POSTGRES);
        importTo(DaoFactory.MONGO);
    }
}
