package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.LessonDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Lesson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgLessonDao extends PgGenericDao<Lesson, Integer> implements LessonDao {

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
                lesson.setId(rs.getInt("lesson_id"));
                lesson.setCourse(pcd.getByPK(rs.getInt("course_id")));
                lesson.setDate(rs.getDate("lesson_date"));
                lesson.setOrder(Lesson.Order.getById(rs.getInt("lesson_order")));
                lesson.setType(Lesson.Type.getById(rs.getInt("lesson_type_id")));
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
            statement.setInt(1, object.getType().getId());
            statement.setInt(2, object.getCourse().getId());
            statement.setDate(3, object.getDate());
            statement.setInt(4, object.getOrder().getId());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Lesson object) throws PersistentException {
        try {
            statement.setInt(1, object.getType().getId());
            statement.setInt(2, object.getCourse().getId());
            statement.setDate(3, object.getDate());
            statement.setInt(4, object.getOrder().getId());
            statement.setInt(5, object.getId());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Lesson> getByGroup(Integer groupId) throws PersistentException {
        List<Lesson> list;
        String sql = "SELECT * FROM lessons, courses WHERE lessons.course_id = courses.course_id AND courses.group_id= ? ORDER BY lessons.lesson_date, lesson_order;";
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
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
