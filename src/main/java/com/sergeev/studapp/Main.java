package com.sergeev.studapp;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Student;

public class Main {
    public static void main(String[] args) throws PersistentException {
//        Discipline discipline = new Discipline();
//        discipline.setTitle("Algebra");
//        Query<Discipline> a = MongoDaoFactory.getStore().createQuery(Discipline.class).filter("title =", discipline.getTitle());
//        MongoDaoFactory.getStore().delete(a);
//        System.out.println(MongoDaoFactory.getStore().createQuery(Discipline.class).asList());

//        Datastore store = MongoDaoFactory.getStore();
//
//        Group group = new Group();
//        Student student = new Student();
//        Teacher teacher = new Teacher();
//        Discipline discipline = new Discipline();
//        Course course = new Course();
//        Lesson lesson = new Lesson();
//        Mark mark = new Mark();
//
//        group.setTitle("AA-2017");
//
//        student.setGroup(group);
//        student.setFirstName("Kirill");
//        student.setLastName("Sergeev");
//
//        teacher.setFirstName("Vasya");
//        teacher.setLastName("Pupkin");
//
//        discipline.setTitle("Mathematics");
//
//        course.setGroup(group);
//        course.setTeacher(teacher);
//        course.setDiscipline(discipline);
//
//        lesson.setCourse(course);
//        lesson.setOrder(Lesson.Order.SECOND);
//        lesson.setType(Lesson.Type.LAB);
//        lesson.setDate(Date.valueOf("2017-01-02"));
//
//        mark.setLesson(lesson);
//        mark.setStudent(student);
//        mark.setValue(100);
//
//        store.save(student);
//        store.save(course);
//        store.save(lesson);
//        store.save(mark);
//
//        System.out.println(store.getKey(mark));

        Student student = new Student();
//        Group group = new Group();
//        group.setTitle("AA-2018");
//        student.setGroup(group);
//        student.setFirstName("Lol");
//        student.setLastName("Lolovich");
//        System.out.println(student);

        System.out.println(DaoFactory.getDaoFactory(DaoFactory.MONGO).getStudentDao().getAll());
    }
}