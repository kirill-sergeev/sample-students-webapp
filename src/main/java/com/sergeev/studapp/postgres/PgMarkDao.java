package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.postgres.PgLessonDao.LESSON_ID;
import static com.sergeev.studapp.postgres.PgUserDao.USER_ID;

public class PgMarkDao extends PgGenericDao<Mark> implements MarkDao {

    protected static final String MARK_ID = "mark_id";
    protected static final String VALUE = "mark";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM marks WHERE mark_id= ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM marks;";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO marks (lesson_id, user_id, mark) VALUES (?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE marks SET lesson_id= ?, user_id= ?, mark= ? WHERE mark_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
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
                mark.setId(rs.getString(MARK_ID));
                mark.setLesson(pld.getById(rs.getString(LESSON_ID)));
                mark.setStudent(pud.getById(rs.getString(USER_ID)));
                mark.setValue(rs.getInt(VALUE));
                result.add(mark);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Mark object) throws PersistentException {
        try {
            statement.setInt(1, Integer.parseInt(object.getLesson().getId()));
            statement.setInt(2, Integer.parseInt(object.getStudent().getId()));
            statement.setInt(3, object.getValue());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Mark object) throws PersistentException {
        try {
            statement.setInt(1, Integer.parseInt(object.getLesson().getId()));
            statement.setInt(2, Integer.parseInt(object.getStudent().getId()));
            statement.setInt(3, object.getValue());
            statement.setInt(4, Integer.parseInt(object.getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public Double getAvgMark(String studentId, String disciplineId) throws PersistentException {
        Double avgMark;
        String sql = "SELECT student_avg_mark_by_discipline(?,?);";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(studentId));
            statement.setInt(2, Integer.parseInt(disciplineId));
            ResultSet rs = statement.executeQuery();
            rs.next();
            avgMark = rs.getDouble(1);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return avgMark;
    }

    @Override
    public List<Mark> getByLesson(String lessonId) throws PersistentException {
        List<Mark> list;
        String sql = "SELECT * FROM marks WHERE lesson_id= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(lessonId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return list;
    }

    @Override
    public List<Mark> getByStudentAndDiscipline(String studentId, String disciplineId) throws PersistentException {
        List<Mark> list;
        String sql = "SELECT * FROM marks m, lessons l, courses c WHERE m.lesson_id = l.lesson_id AND l.course_id = c.course_id AND m.user_id = ? AND c.discipline_id = ?";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(studentId));
            statement.setInt(2, Integer.parseInt(disciplineId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

}
