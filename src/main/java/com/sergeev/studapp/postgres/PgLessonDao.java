package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgLessonDao extends PgGenericDao<Lesson> implements LessonDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgLessonDao.class);

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM lessons WHERE lesson_id= ?;";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM lessons;";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO lessons (type, course_id, date, ordinal) VALUES (?, ?, ?, ?);";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE lessons SET type= ?, course_id= ?, date= ?, ordinal= ? WHERE lesson_id= ?;";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM lessons WHERE lesson_id= ?;";
    }

    @Override
    protected List<Lesson> parseResultSet(ResultSet rs) throws PersistentException {
        List<Lesson> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Lesson lesson = new Lesson();
                PgCourseDao pcd = new PgCourseDao();
                lesson.setId(rs.getInt(LESSON_ID));
                lesson.setCourse(pcd.getById(rs.getInt(COURSE_ID)));
                lesson.setDate(rs.getDate(DATE).toLocalDate());
                lesson.setOrder(Lesson.Order.values()[rs.getInt(ORDER) - 1]);
                lesson.setType(Lesson.Type.valueOf(rs.getString(TYPE)));
                result.add(lesson);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Lesson object) throws PersistentException {
        try {
            statement.setString(1, object.getType().name());
            statement.setInt(2, object.getCourse().getId());
            statement.setDate(3, Date.valueOf(object.getDate()));
            statement.setInt(4, object.getOrder().ordinal() + 1);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Lesson object) throws PersistentException {
        try {
            statement.setString(1, object.getType().name());
            statement.setInt(2, object.getCourse().getId());
            statement.setDate(3, Date.valueOf(object.getDate()));
            statement.setInt(4, object.getOrder().ordinal() + 1);
            statement.setInt(5, object.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Lesson> getByGroup(Integer groupId) throws PersistentException {
        List<Lesson> list;
        String sql = "SELECT * FROM lessons, courses WHERE lessons.course_id = courses.course_id AND courses.group_id= ? ORDER BY lessons.date, lessons.ordinal;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
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
