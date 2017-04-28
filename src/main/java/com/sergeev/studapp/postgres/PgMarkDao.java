package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.MarkDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Mark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgMarkDao extends PgGenericDao<Mark> implements MarkDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgMarkDao.class);
    private static final String SQL_SELECT_MARK_BY_LESSON =
            "SELECT * FROM marks WHERE lesson_id = ?";
    private static final String SQL_SELECT_AVG_MARK_BY_DISCIPLINE_AND_STUDENT =
            "{call student_avg_mark_by_discipline(? , ?, ?)}";
    private static final String SQL_SELECT_MARK_BY_DISCIPLINE_AND_STUDENT =
            "SELECT * FROM marks m, lessons l, courses c WHERE m.lesson_id = l.lesson_id " +
                    "AND l.course_id = c.course_id AND m.user_id = ? AND c.discipline_id = ?";

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM marks WHERE mark_id = ?";
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
        return "UPDATE marks SET lesson_id = ?, user_id = ?, mark= ? WHERE mark_id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM marks WHERE mark_id = ?";
    }

    @Override
    protected List<Mark> parseResultSet(ResultSet rs, Connection con) {
        List<Mark> list = new ArrayList<>();
        try {
            while (rs.next()) {
                PgLessonDao lessonDao = new PgLessonDao();
                PgUserDao userDao = new PgUserDao();
                Mark mark = new Mark()
                        .setId(rs.getInt(MARK_ID))
                        .setLesson(lessonDao.getById(rs.getInt(LESSON_ID), con))
                        .setStudent(userDao.getById(rs.getInt(USER_ID), con))
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
        try (Connection con = PgDaoFactory.getConnection();
             CallableStatement st = con.prepareCall(
                     SQL_SELECT_AVG_MARK_BY_DISCIPLINE_AND_STUDENT)) {
            st.setInt(1, studentId);
            st.setInt(2, disciplineId);
            st.registerOutParameter(3, Types.NUMERIC);
            st.execute();
            BigDecimal decimal = st.getBigDecimal(3);
            avgMark = decimal == null ? 0: decimal.doubleValue();
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return avgMark;
    }

    @Override
    public List<Mark> getByLesson(Integer lessonId) {
        return getBy(SQL_SELECT_MARK_BY_LESSON, lessonId);
    }

    @Override
    public List<Mark> getByDisciplineAndStudent(Integer disciplineId, Integer studentId) {
        return getBy(SQL_SELECT_MARK_BY_DISCIPLINE_AND_STUDENT, disciplineId, studentId);
    }

}
