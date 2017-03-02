package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.PersistException;
import com.sergeev.studapp.dao.TeacherDao;
import com.sergeev.studapp.model.Teacher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgTeacherDao extends PgGenericDao<Teacher, Integer> implements TeacherDao {

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM teachers WHERE teacher_id= ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM teachers;";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO teachers (first_name, last_name) VALUES (?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE teachers SET first_name= ?, last_name= ? WHERE teacher_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM teachers WHERE teacher_id= ?;";
    }

    @Override
    protected List<Teacher> parseResultSet(ResultSet rs) throws PersistException {
        List<Teacher> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getInt("teacher_id"));
                teacher.setFirstName(rs.getString("first_name"));
                teacher.setLastName(rs.getString("last_name"));
                result.add(teacher);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Teacher object) throws PersistException {
        try {
            statement.setString(1, object.getFirstName());
            statement.setString(2, object.getLastName());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Teacher object) throws PersistException {
        try {
            statement.setString(1, object.getFirstName());
            statement.setString(2, object.getLastName());
            statement.setInt(3, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}
