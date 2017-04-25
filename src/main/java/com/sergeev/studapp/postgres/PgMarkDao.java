package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.postgres.PgConstants.*;

public class PgMarkDao extends PgGenericDao<Mark> implements MarkDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgMarkDao.class);

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM marks WHERE mark_id= ?;";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM marks;";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO marks (lesson_id, user_id, mark) VALUES (?, ?, ?);";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE marks SET lesson_id= ?, user_id= ?, mark= ? WHERE mark_id= ?;";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM marks WHERE mark_id= ?;";
    }

    @Override
    protected List<Mark> parseResultSet(ResultSet rs) throws PersistentException {
        List<Mark> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Mark mark = new Mark();
                PgLessonDao pld = new PgLessonDao();
                PgUserDao pud = new PgUserDao();
                mark.setId(rs.getInt(MARK_ID));
                mark.setLesson(pld.getById(rs.getInt(LESSON_ID)));
                mark.setStudent(pud.getById(rs.getInt(USER_ID)));
                mark.setValue(rs.getInt(VALUE));
                result.add(mark);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Mark object) throws PersistentException {
        try {
            statement.setInt(1, object.getLesson().getId());
            statement.setInt(2, object.getStudent().getId());
            statement.setInt(3, object.getValue());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Mark object) throws PersistentException {
        try {
            statement.setInt(1, object.getLesson().getId());
            statement.setInt(2, object.getStudent().getId());
            statement.setInt(3, object.getValue());
            statement.setInt(4, object.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public Double getAvgMark(Integer studentId, Integer disciplineId) throws PersistentException {
        Double avgMark;
        String sql = "SELECT student_avg_mark_by_discipline(?,?);";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, disciplineId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            avgMark = rs.getDouble(1);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return avgMark;
    }

    @Override
    public List<Mark> getByLesson(Integer lessonId) throws PersistentException {
        List<Mark> list;
        String sql = "SELECT * FROM marks WHERE lesson_id= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lessonId);
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

    @Override
    public List<Mark> getByDisciplineAndStudent(Integer disciplineId, Integer studentId) throws PersistentException {
        List<Mark> list;
        String sql = "SELECT * FROM marks m, lessons l, courses c WHERE m.lesson_id = l.lesson_id AND l.course_id = c.course_id AND m.user_id = ? AND c.discipline_id = ?";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2,disciplineId);
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
