package com.sergeev.studapp;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.*;
import com.sergeev.studapp.mongo.*;
import com.sergeev.studapp.service.ApplicationException;
import com.sergeev.studapp.service.LessonService;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main1(String[] args) throws PersistentException {

        Group group = new Group();
        group.setTitle("AA-2017");
        MongoGroupDao gdao = new MongoGroupDao();
        gdao.persist(group);

        Discipline discipline = new Discipline();
        discipline.setTitle("Algebra");
        MongoDisciplineDao ddao = new MongoDisciplineDao();
        ddao.persist(discipline);

        Account account = new Account();
        account.setLogin("kirill_sergeev");
        account.setPassword("111");
        MongoAccountDao adao = new MongoAccountDao();
        adao.persist(account);

        User user = new User();
        user.setType(User.Role.TEACHER);
        user.setFirstName("Kirill");
        user.setLastName("Sergeev");
        user.setAccount(account);
        MongoUserDao udao = new MongoUserDao();
        udao.persist(user);


        User user1 = new User();
        user1.setType(User.Role.STUDENT);
        user1.setFirstName("Vasya");
        user1.setLastName("Pupkin");
        user1.setAccount(account);
        user1.setGroup(group);
        udao.persist(user1);

        Course course = new Course();
        course.setTeacher(user);
        course.setDiscipline(discipline);
        course.setGroup(group);
        MongoCourseDao cdao = new MongoCourseDao();
        cdao.persist(course);

        Lesson lesson = new Lesson();
        lesson.setType(Lesson.Type.PRACTICAL);
        lesson.setDate(LocalDate.parse("2017-03-06"));
        lesson.setOrder(Lesson.Order.FIFTH);
        lesson.setCourse(course);
        MongoLessonDao ldao = new MongoLessonDao();
        ldao.persist(lesson);

        Mark mark = new Mark();
        mark.setStudent(user1);
        mark.setLesson(lesson);
        mark.setValue(100);
        MongoMarkDao mdao = new MongoMarkDao();
        mdao.persist(mark);

        System.out.println("lessons");
        List<Lesson> lessons;
        lessons = ldao.getAll();
        System.out.println(lessons);

        System.out.println("users");
        List<User> users;
        users = udao.getAll();
        System.out.println(users);

        System.out.println("groups");
        List<Group> groups;
        groups =gdao.getAll();
        System.out.println(groups);

        System.out.println("marks");
        List <Mark> marks;
        marks = mdao.getAll();
        System.out.println(marks);

        System.out.println("courses");
        List <Course>courses;
        courses = cdao.getAll();
        System.out.println(courses);

       // System.out.println( ldao.getByGroup(group.getId()));
    }

    public static void main(String[] args) throws ApplicationException {
        Lesson lesson = LessonService.create("58df7b84c6ff8e3dc3e212d4", "58df7d02c6ff8e40440af884", Lesson.Type.LAB.getId(), Lesson.Order.FIRST.getNumber().toString(), "2017-01-01");
        System.out.println(lesson.getDate());

    }
}