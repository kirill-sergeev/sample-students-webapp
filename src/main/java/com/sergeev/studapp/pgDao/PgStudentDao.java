package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.PersistException;
import com.sergeev.studapp.dao.StudentDao;
import com.sergeev.studapp.model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgStudentDao extends PgGenericDao<Student, Integer> implements StudentDao {

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM students WHERE student_id= ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM students;";
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
    protected List<Student> parseResultSet(ResultSet rs) throws PersistException {
        List<Student> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Student student = new Student();
                PgGroupDao pgd = new PgGroupDao();
                student.setGroup(pgd.getByPK(rs.getInt("group_id")));
                student.setId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                result.add(student);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Student object) throws PersistException {
        try {
            statement.setInt(1, object.getGroup().getId());
            statement.setString(2, object.getFirstName());
            statement.setString(3, object.getLastName());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Student object) throws PersistException {
        try {
            statement.setInt(1, object.getGroup().getId());
            statement.setString(2, object.getFirstName());
            statement.setString(3, object.getLastName());
            statement.setInt(4, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

}
