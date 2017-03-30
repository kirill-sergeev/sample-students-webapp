package com.sergeev.studapp;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import com.sergeev.studapp.mongo.MongoLessonDao;

import java.sql.Date;

public class Main {
    public static void main(String[] args) throws PersistentException {

//        Group group = GroupService.create("AA-2017");
//        User user = UserService.createStudent("Kirill", "Sergeev", group.getId());
//        user.getAccount().setToken("111");
//        AccountService.update(user);
//        System.out.println(user);
//
//        long ji = System.currentTimeMillis();
//        User user = UserService.readByToken("111");
//        long jj = System.currentTimeMillis();
//        System.out.println(user);
//        System.out.println(jj - ji);


//        Group group = GroupService.create("AA-2017");
//        User user = UserService.createStudent("Kirill", "Sergeev", group.getId());
//        user.getAccount().setToken("111");
//        AccountService.update(user);
//        System.out.println(user);

//        User user = UserService.readByLogin("kirill_sergeev", "811265");
//        System.out.println(user);

//        User user = new User();
//        MongoUserDao dao = new MongoUserDao();
//        dao.persist(user);
//        UserService.updateStudent("aa", "aa", "58daa82e92dbc154fe77cb09", "58daa82e92dbc154fe77cb09");

        Lesson lesson = new Lesson();
        lesson.setType(Lesson.Type.PRACTICAL);
        lesson.setDate(Date.valueOf("2017-03-06"));
        lesson.setOrder(Lesson.Order.FIFTH);
        lesson.setCourse(null);
        MongoLessonDao dao = new MongoLessonDao();
        dao.persist(lesson);


    }
}