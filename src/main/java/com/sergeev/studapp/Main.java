package com.sergeev.studapp;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistException;
import com.sergeev.studapp.dao.StudentDao;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Student;

public class Main {

    public static void main(String[] args) throws PersistException {
        DaoFactory pgFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
        StudentDao std = pgFactory.getStudentDao();
        GroupDao grd = pgFactory.getGroupDao();
        Student st = new Student();
        Group gr = new Group();
        try {
            gr = grd.getByPK(Integer.valueOf(5));
        } catch (PersistException e) {
            e.printStackTrace();
        }
        st.setGroup(gr);
        st.setFirstName("Kirill");
        st.setLastName("Sergeev");
        Student st2 = std.persist(st);
         System.out.println(st2);


        //LessonDao ld = pgFactory.getLessonDao();
//        Lesson lesson = new Lesson();
//        PgCourseDao course = new PgCourseDao();
//        lesson.setOrder(Lesson.Order.getById(1));
//        lesson.setCourse(course.getByPK(1));
//        lesson.setDate(Date.valueOf("2017-02-03"));
//        lesson.setType(Lesson.Type.LAB);
//        ld.persist(lesson);
        //System.out.println(ld.getByPK(101));

//        String name = "Brian";
//        StudentDao sd = pgFactory.getStudentDao();
//        ArrayList<Student> st = null;
//        try {
//            st = (ArrayList<Student>) sd.getByName(name);
//        } catch (PersistException e) {
//            e.printStackTrace();
//        }
//
//        for(Student student: st){
//            System.out.println(student);
//        }
    }

}
