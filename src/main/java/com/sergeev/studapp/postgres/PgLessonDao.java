package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgLessonDao extends PgGenericDao<Lesson> implements LessonDao {

    private static final String SQL_SELECT_LESSON_BY_GROUP =
            "SELECT * FROM lessons l, courses c WHERE l.course_id = c.id " +
                    "AND c.group_id = ? ORDER BY l.date, l.ordinal";

    public PgLessonDao(Connection con) {
        super(con);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM lessons WHERE id = ?";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM lessons;";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO lessons (type_id, course_id, date, ordinal) VALUES (?, ?, ?, ?)";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE lessons SET type_id = ?, course_id = ?, date = ?, ordinal = ? WHERE id = ?";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM lessons WHERE id = ?";
    }

    @Override
    protected List<Lesson> parseResultSet(ResultSet rs) {
        List<Lesson> list = new ArrayList<>();
        try {
            while (rs.next()) {
                PgCourseDao courseDao = new PgCourseDao(con);
                Lesson lesson = new Lesson()
                        .setId(rs.getInt(ID))
                        .setCourse(courseDao.getById(rs.getInt(COURSE_ID)))
                        .setDate(rs.getDate(DATE).toLocalDate())
                        .setOrder(Lesson.Order.values()[rs.getInt(ORDER) - 1])
                        .setType(Lesson.Type.values()[rs.getInt(TYPE) - 1]);
                list.add(lesson);
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
    protected void prepareStatementForInsert(PreparedStatement st, Lesson lesson) {
        try {
            st.setInt(1, lesson.getType().ordinal() + 1);
            st.setInt(2, lesson.getCourse().getId());
            st.setDate(3, Date.valueOf(lesson.getDate()));
            st.setInt(4, lesson.getOrder().ordinal() + 1);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Lesson lesson) {
        try {
            st.setInt(1, lesson.getType().ordinal() + 1);
            st.setInt(2, lesson.getCourse().getId());
            st.setDate(3, Date.valueOf(lesson.getDate()));
            st.setInt(4, lesson.getOrder().ordinal() + 1);
            st.setInt(5, lesson.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Lesson> getByGroup(Integer groupId) {
        return getByParams(SQL_SELECT_LESSON_BY_GROUP, groupId);
    }

}
