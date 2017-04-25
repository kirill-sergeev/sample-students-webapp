package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.CourseDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgCourseDao extends PgGenericDao<Course> implements CourseDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgCourseDao.class);

    private static final String DISCIPLINE = "discipline";
    private static final String GROUP = "group";
    private static final String TEACHER = "teacher";
    private static final String DISCIPLINE_GROUP = "discipline_group";

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM courses WHERE course_id= ?;";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM courses;";
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO courses (discipline_id, group_id, user_id) VALUES (?, ?, ?);";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE courses SET discipline_id= ?, group_id= ?, user_id= ? WHERE course_id= ?;";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM courses WHERE course_id= ?;";
    }

    @Override
    protected List<Course> parseResultSet(ResultSet rs) throws PersistentException {
        List<Course> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Course course = new Course();
                PgDisciplineDao pdd = new PgDisciplineDao();
                PgGroupDao pgd = new PgGroupDao();
                PgUserDao ptd = new PgUserDao();
                course.setId(rs.getInt(COURSE_ID));
                course.setGroup(pgd.getById(rs.getInt(GROUP_ID)));
                course.setDiscipline(pdd.getById(rs.getInt(DISCIPLINE_ID)));
                course.setTeacher(ptd.getById(rs.getInt(USER_ID)));
                result.add(course);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Course object) throws PersistentException {
        try {
            statement.setInt(1, object.getDiscipline().getId());
            statement.setInt(2, object.getGroup().getId());
            statement.setInt(3, object.getTeacher().getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Course object) throws PersistentException {
        try {
            statement.setInt(1, object.getDiscipline().getId());
            statement.setInt(2, object.getGroup().getId());
            statement.setInt(3, object.getTeacher().getId());
            statement.setInt(4, object.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Course> getByDiscipline(Integer disciplineId) throws PersistentException {
        return getBy(DISCIPLINE, disciplineId);
    }

    @Override
    public List<Course> getByGroup(Integer groupId) throws PersistentException {
        return getBy(GROUP, groupId);
    }

    @Override
    public List<Course> getByTeacher(Integer userId) throws PersistentException {
        return getBy(TEACHER, userId);
    }

    @Override
    public Course getByDisciplineAndGroup(Integer disciplineId, Integer groupId) throws PersistentException {
        return getBy(DISCIPLINE_GROUP, disciplineId, groupId).listIterator().next();
    }

    private List<Course> getBy(String type, Integer param, Integer... params) throws PersistentException, IllegalArgumentException {
        List<Course> list;
        String sql;
        switch (type) {
            case DISCIPLINE:
                sql = "SELECT * FROM courses WHERE discipline_id= ?;";
                break;
            case GROUP:
                sql = "SELECT * FROM courses WHERE group_id= ?;";
                break;
            case TEACHER:
                sql = "SELECT * FROM courses WHERE user_id= ?;";
                break;
            case DISCIPLINE_GROUP:
                sql = "SELECT * FROM courses WHERE discipline_id= ? AND group_id= ?";
                break;
            default:
                throw new IllegalArgumentException();
        }

        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, param);
            if (params.length == 1) {
                statement.setInt(2, params[0]);
            }
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }
}
