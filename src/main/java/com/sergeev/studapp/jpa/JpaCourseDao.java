package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import com.sergeev.studapp.model.Discipline;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JpaCourseDao extends JpaGenericDao<Course> implements CourseDao {

    @Override
    public List<Course> getByDiscipline(Integer disciplineId) throws PersistentException {
       return new ArrayList<>(entityManager.find(Discipline.class, disciplineId).getCourses());
    }

    @Override
    public List<Course> getByGroup(Integer groupId) throws PersistentException {
        return new ArrayList<>(entityManager.find(Group.class, groupId).getCourses());
    }

    @Override
    public List<Course> getByTeacher(Integer userId) throws PersistentException {
        return new ArrayList<>(entityManager.find(User.class, userId).getCourses());
    }

    @Override
    public Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId) throws PersistentException {
        Set<Course> byDiscipline = entityManager.find(Discipline.class, disciplineId).getCourses();
        Set<Course> byGroup = entityManager.find(Group.class, groupId).getCourses();
        byDiscipline.retainAll(byGroup);
        return byDiscipline.iterator().next();
    }
}
