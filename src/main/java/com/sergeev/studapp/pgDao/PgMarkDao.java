package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistException;
import com.sergeev.studapp.model.Mark;

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
        return "INSERT INTO lessons (lesson_id, student_id, mark) VALUES (?, ?, ?);";
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
    protected List<Mark> parseResultSet(ResultSet rs) throws PersistException {
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
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Mark object) throws PersistException {
        try {
            statement.setInt(1, object.getLesson().getId());
            statement.setInt(2, object.getStudent().getId());
            statement.setInt(3, object.getValue());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Mark object) throws PersistException {
        try {
            statement.setInt(1, object.getLesson().getId());
            statement.setInt(2, object.getStudent().getId());
            statement.setInt(3, object.getValue());
            statement.setInt(4, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}
