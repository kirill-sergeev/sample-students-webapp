package com.sergeev.studapp;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistException;

public class Main {

    public static void main(String[] args) throws PersistException {
        DaoFactory pgFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
//        StudentDao std = pgFactory.getStudentDao();
//        GroupDao grd = pgFactory.getGroupDao();
//        Student st = new Student();
//        Group gr = new Group();
//        try {
//            gr = grd.getByPK(1);
//        } catch (PersistException e) {
//            e.printStackTrace();
//        }
//        st.setGroup(gr);
//        st.setFirstName("Kirill");
//        st.setLastName("Sergeev");
//        try {
//            std.persist(st);
//        } catch (PersistException e) {
//            e.printStackTrace();
//        }

        LessonDao ld = pgFactory.getLessonDao();
//        Lesson lesson = new Lesson();
//        PgCourseDao course = new PgCourseDao();
//        lesson.setOrder(Lesson.Order.getById(1));
//        lesson.setCourse(course.getByPK(1));
//        lesson.setDate(Date.valueOf("2017-02-03"));
//        lesson.setType(Lesson.Type.LAB);
//        ld.persist(lesson);
        System.out.println(ld.getByPK(101));

    }

}
