package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgCourseDao extends PgGenericDao<Course> implements CourseDao {

    private static final String SQL_SELECT_COURSE_BY_DISCIPLINE =
            "SELECT * FROM courses WHERE discipline_id = ?";
    private static final String SQL_SELECT_COURSE_BY_GROUP =
            "SELECT * FROM courses WHERE group_id = ?";
    private static final String SQL_SELECT_COURSE_BY_TEACHER =
            "SELECT * FROM courses WHERE user_id = ?";
    private static final String SQL_SELECT_COURSE_BY_DISCIPLINE_AND_GROUP =
            "SELECT * FROM courses WHERE discipline_id = ? AND group_id = ?";

    public PgCourseDao(Connection con) {
        super(con);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM courses WHERE id = ?";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM courses";
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO courses (discipline_id, group_id, user_id) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE courses SET discipline_id = ?, group_id = ?, user_id = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM courses WHERE id = ?";
    }

    @Override
    protected List<Course> parseResultSet(ResultSet rs) {
        List<Course> list = new ArrayList<>();
        try {
            while (rs.next()) {
                PgDisciplineDao disciplineDao = new PgDisciplineDao(con);
                PgGroupDao groupDao = new PgGroupDao(con);
                PgUserDao userDao = new PgUserDao(con);
                Course course = new Course()
                        .setId(rs.getInt(ID))
                        .setGroup(groupDao.getById(rs.getInt(GROUP_ID)))
                        .setDiscipline(disciplineDao.getById(rs.getInt(DISCIPLINE_ID)))
                        .setTeacher(userDao.getById(rs.getInt(USER_ID)));
                list.add(course);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list.isEmpty()) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement st, Course course) {
        try {
            st.setInt(1, course.getDiscipline().getId());
            st.setInt(2, course.getGroup().getId());
            st.setInt(3, course.getTeacher().getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Course course) {
        try {
            st.setInt(1, course.getDiscipline().getId());
            st.setInt(2, course.getGroup().getId());
            st.setInt(3, course.getTeacher().getId());
            st.setInt(4, course.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Course> getByDiscipline(Integer disciplineId) {
        return getByParams(SQL_SELECT_COURSE_BY_DISCIPLINE, disciplineId);
    }

    @Override
    public List<Course> getByGroup(Integer groupId) {
        return getByParams(SQL_SELECT_COURSE_BY_GROUP, groupId);
    }

    @Override
    public List<Course> getByTeacher(Integer userId) {
        return getByParams(SQL_SELECT_COURSE_BY_TEACHER, userId);
    }

    @Override
    public Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId) {
        return getByParams(SQL_SELECT_COURSE_BY_DISCIPLINE_AND_GROUP, disciplineId, groupId).get(0);
    }

}
