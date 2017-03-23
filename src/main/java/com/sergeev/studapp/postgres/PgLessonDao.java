package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.postgres.PgCourseDao.COURSE_ID;

public class PgLessonDao extends PgGenericDao<Lesson> implements LessonDao {

    protected static final String LESSON_ID = "lesson_id";
    protected static final String LESSON_DATE = "lesson_date";
    protected static final String LESSON_ORDER = "lesson_order";
    protected static final String LESSON_TYPE = "lesson_type_id";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM lessons WHERE lesson_id= ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM lessons;";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO lessons (lesson_type_id, course_id, lesson_date, lesson_order) VALUES (?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE lessons SET lesson_type_id= ?, course_id= ?, lesson_date= ?, lesson_order= ? WHERE lesson_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM lessons WHERE lesson_id= ?;";
    }

    @Override
    protected List<Lesson> parseResultSet(ResultSet rs) throws PersistentException {
        List<Lesson> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Lesson lesson = new Lesson();
                PgCourseDao pcd = new PgCourseDao();
                lesson.setId(rs.getString(LESSON_ID));
                lesson.setCourse(pcd.getById(rs.getString(COURSE_ID)));
                lesson.setDate(rs.getDate(LESSON_DATE));
                lesson.setOrder(Lesson.Order.getByNumber(rs.getInt(LESSON_ORDER)));
                lesson.setType(Lesson.Type.getById(rs.getString(LESSON_TYPE)));
                result.add(lesson);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Lesson object) throws PersistentException {
        try {
            statement.setInt(1, Integer.parseInt(object.getType().getId()));
            statement.setInt(2, Integer.parseInt(object.getCourse().getId()));
            statement.setDate(3, object.getDate());
            statement.setInt(4, object.getOrder().getNumber());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Lesson object) throws PersistentException {
        try {
            statement.setInt(1, Integer.parseInt(object.getType().getId()));
            statement.setInt(2, Integer.parseInt(object.getCourse().getId()));
            statement.setDate(3, object.getDate());
            statement.setInt(4, object.getOrder().getNumber());
            statement.setInt(5, Integer.parseInt(object.getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Lesson> getByGroup(String groupId) throws PersistentException {
        List<Lesson> list;
        String sql = "SELECT * FROM lessons, courses WHERE lessons.course_id = courses.course_id AND courses.group_id= ? ORDER BY lessons.lesson_date, lesson_order;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(groupId));
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
