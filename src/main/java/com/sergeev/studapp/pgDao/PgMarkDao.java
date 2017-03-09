package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgMarkDao extends PgGenericDao<Mark, Integer> implements MarkDao {

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
        return "INSERT INTO marks (lesson_id, student_id, mark) VALUES (?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE marks SET lesson_id= ?, student_id= ?, mark= ? WHERE mark_id= ?;";
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
                PgStudentDao psd = new PgStudentDao();
                mark.setId(rs.getInt("mark_id"));
                mark.setLesson(pld.getByPK(rs.getInt("lesson_id")));
                mark.setStudent(psd.getByPK(rs.getInt("student_id")));
                mark.setValue(rs.getInt("mark"));
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
            statement.setInt(1, object.getLesson().getId());
            statement.setInt(2, object.getStudent().getId());
            statement.setInt(3, object.getValue());
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public Double getAvgMark(Integer studentId, Integer disciplineId) throws PersistentException {
        Double avgMark;
        String sql = "SELECT student_avg_mark_by_discipline(?,?);";
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, disciplineId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            avgMark = rs.getDouble(1);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return avgMark;
    }

    @Override
    public List<Mark> getByLesson(Integer lessonId) throws PersistentException {
        List<Mark> list;
        String sql = "SELECT * FROM marks WHERE lesson_id= ?;";
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lessonId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return list;
    }

    @Override
    public List<Mark> getByStudentAndDiscipline(Integer studentId, Integer disciplineId) throws PersistentException {
        List<Mark> list;
        String sql = "SELECT * FROM marks m, lessons l, courses c WHERE m.lesson_id = l.lesson_id AND l.course_id = c.course_id AND m.student_id = ? AND c.discipline_id = ?";
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, disciplineId);
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
