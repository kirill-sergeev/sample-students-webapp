package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistException;
import com.sergeev.studapp.model.Course;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgCourseDao extends PgGenericDao<Course, Integer> implements CourseDao {

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM courses WHERE course_id= ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM courses;";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO courses (discipline_id, group_id, teacher_id) VALUES (?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE courses SET discipline_id= ?, group_id= ?, teacher_id= ? WHERE course_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM courses WHERE course_id= ?;";
    }

    @Override
    protected List<Course> parseResultSet(ResultSet rs) throws PersistException {
        List<Course> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Course course = new Course();
                PgDisciplineDao pdd = new PgDisciplineDao();
                PgGroupDao pgd = new PgGroupDao();
                PgTeacherDao ptd = new PgTeacherDao();
                course.setId(rs.getInt("course_id"));
                course.setGroup(pgd.getByPK(rs.getInt("group_id")));
                course.setDiscipline(pdd.getByPK(rs.getInt("discipline_id")));
                course.setTeacher(ptd.getByPK(rs.getInt("teacher_id")));
                result.add(course);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Course object) throws PersistException {
        try {
            statement.setInt(1, object.getDiscipline().getId());
            statement.setInt(2, object.getGroup().getId());
            statement.setInt(3, object.getTeacher().getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Course object) throws PersistException {
        try {
            statement.setInt(1, object.getDiscipline().getId());
            statement.setInt(2, object.getGroup().getId());
            statement.setInt(3, object.getTeacher().getId());
            statement.setInt(4, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

}
