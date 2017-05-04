package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgMarkDao extends PgGenericDao<Mark> implements MarkDao {

    private static final String SQL_SELECT_MARK_BY_LESSON =
            "SELECT * FROM marks WHERE lesson_id = ?";
    private static final String SQL_SELECT_AVG_MARK_BY_DISCIPLINE_AND_STUDENT =
            "{call student_avg_mark_by_discipline(? , ?, ?)}";
    private static final String SQL_SELECT_MARK_BY_DISCIPLINE_AND_STUDENT =
            "SELECT * FROM marks m, lessons l, courses c WHERE m.lesson_id = l.id " +
                    "AND l.course_id = c.id AND c.discipline_id = ? AND m.user_id = ?";

    public PgMarkDao(Connection con) {
        super(con);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM marks WHERE id = ?";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM marks";
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO marks (lesson_id, user_id, mark) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE marks SET lesson_id = ?, user_id = ?, mark= ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM marks WHERE id = ?";
    }

    @Override
    protected List<Mark> parseResultSet(ResultSet rs) {
        List<Mark> list = new ArrayList<>();
        try {
            while (rs.next()) {
                PgLessonDao lessonDao = new PgLessonDao(con);
                PgUserDao userDao = new PgUserDao(con);
                Mark mark = new Mark()
                        .setId(rs.getInt(ID))
                        .setLesson(lessonDao.getById(rs.getInt(LESSON_ID)))
                        .setStudent(userDao.getById(rs.getInt(USER_ID)))
                        .setValue(rs.getInt(VALUE));
                list.add(mark);
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
    protected void prepareStatementForInsert(PreparedStatement st, Mark mark) {
        try {
            st.setInt(1, mark.getLesson().getId());
            st.setInt(2, mark.getStudent().getId());
            st.setInt(3, mark.getValue());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Mark mark) {
        try {
            st.setInt(1, mark.getLesson().getId());
            st.setInt(2, mark.getStudent().getId());
            st.setInt(3, mark.getValue());
            st.setInt(4, mark.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public Double getAvgMark(Integer studentId, Integer disciplineId) {
        double avgMark;
        try (CallableStatement st = con.prepareCall(
                SQL_SELECT_AVG_MARK_BY_DISCIPLINE_AND_STUDENT)) {
            st.setInt(1, studentId);
            st.setInt(2, disciplineId);
            st.registerOutParameter(3, Types.NUMERIC);
            st.execute();
            BigDecimal decimal = st.getBigDecimal(3);
            avgMark = decimal == null ? 0 : decimal.doubleValue();
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return avgMark;
    }

    @Override
    public List<Mark> getByLesson(Integer lessonId) {
        return getByParams(SQL_SELECT_MARK_BY_LESSON, lessonId);
    }

    @Override
    public List<Mark> getByDisciplineAndStudent(Integer disciplineId, Integer studentId) {
        return getByParams(SQL_SELECT_MARK_BY_DISCIPLINE_AND_STUDENT, disciplineId, studentId);
    }

}
