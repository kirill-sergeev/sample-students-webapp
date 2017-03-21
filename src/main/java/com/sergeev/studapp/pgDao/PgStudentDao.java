package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.StudentDao;
import com.sergeev.studapp.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgStudentDao extends PgGenericDao<Student> implements StudentDao {

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM students WHERE student_id= ? ORDER BY first_name, last_name;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM students ORDER BY first_name, last_name;";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE students SET group_id= ?, first_name= ?, last_name= ? WHERE student_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM students WHERE student_id= ?;";
    }

    @Override
    protected List<Student> parseResultSet(ResultSet rs) throws PersistentException {
        List<Student> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Student student = new Student();
                PgGroupDao pgd = new PgGroupDao();
                student.setGroup(pgd.getById(rs.getString("group_id")));
                student.setId(rs.getString("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                result.add(student);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Student object) throws PersistentException {
        try {
            statement.setInt(1, Integer.parseInt(object.getGroup().getId()));
            statement.setString(2, object.getFirstName());
            statement.setString(3, object.getLastName());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Student object) throws PersistentException {
        try {
            statement.setInt(1, Integer.parseInt(object.getGroup().getId()));
            statement.setString(2, object.getFirstName());
            statement.setString(3, object.getLastName());
            statement.setInt(4, Integer.parseInt(object.getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<Student> getByName(String name) throws PersistentException {
        List<Student> list;
        String sql = "SELECT * FROM students WHERE lower(first_name||' '||last_name) LIKE (?);";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
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

    @Override
    public List<Student> getByGroup(String groupId) throws PersistentException {
        List<Student> list;
        String sql = "SELECT * FROM students WHERE group_id= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(groupId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return list;
    }

}
