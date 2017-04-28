package com.sergeev.studapp.jpa;

import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import com.sergeev.studapp.model.Mark;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JpaMarkDao extends JpaGenericDao<Mark> implements MarkDao {

    @Override
    public Double getAvgMark(Integer studentId, Integer disciplineId) throws PersistentException {
        StoredProcedureQuery storedProcedure = entityManager
                .createStoredProcedureQuery("student_avg_mark_by_discipline")
                .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter(3, BigDecimal.class, ParameterMode.OUT)
                .setParameter(1, studentId)
                .setParameter(2, disciplineId);
        storedProcedure.execute();
        return ((BigDecimal) storedProcedure.getOutputParameterValue(3)).doubleValue();
    }

    @Override
    public List<Mark> getByLesson(Integer lessonId) throws PersistentException {
        return new ArrayList<>(entityManager.find(Lesson.class, lessonId).getMarks());
    }

    @Override
    public List<Mark> getByDisciplineAndStudent(Integer disciplineId, Integer studentId) throws PersistentException {
        TypedQuery<Mark> query = entityManager.createQuery
                ("SELECT m FROM Mark m, Lesson l, Course c WHERE m.lesson = l " +
                        "AND l.course = c AND c.discipline.id = ?1 AND m.student.id = ?2", Mark.class);
        return query.setParameter(1, disciplineId)
                .setParameter(2, studentId).getResultList();
    }
}
