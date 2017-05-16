package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Lesson;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class JpaLessonDao extends JpaGenericDao<Lesson> implements LessonDao {

    @Override
    public List<Lesson> getByGroup(Integer groupId) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        List<Lesson> lessons = new ArrayList<>();
        for (Course course : entityManager.find(Group.class, groupId).getCourses()) {
            lessons.addAll(course.getLessons());
        }
        entityManager.close();
        return lessons;
    }

}
