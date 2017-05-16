package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JpaCourseDao extends JpaGenericDao<Course> implements CourseDao {

    @Override
    public List<Course> getByDiscipline(Integer disciplineId) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        List<Course> courses = new ArrayList<>(entityManager.find(Discipline.class, disciplineId).getCourses());
        entityManager.close();
        return courses;
    }

    @Override
    public List<Course> getByGroup(Integer groupId) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        List<Course> courses = new ArrayList<>(entityManager.find(Group.class, groupId).getCourses());
        entityManager.close();
        return courses;
    }

    @Override
    public List<Course> getByTeacher(Integer userId) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        List<Course> courses = new ArrayList<>(entityManager.find(User.class, userId).getCourses());
        entityManager.close();
        return courses;
    }

    @Override
    public Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId) {
        EntityManager entityManager = JpaDaoFactory.getConnection();
        Set<Course> byDiscipline = entityManager.find(Discipline.class, disciplineId).getCourses();
        Set<Course> byGroup = entityManager.find(Group.class, groupId).getCourses();
        byDiscipline.retainAll(byGroup);
        entityManager.close();
        return byDiscipline.iterator().next();
    }

}
