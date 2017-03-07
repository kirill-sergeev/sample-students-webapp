package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgDisciplineDao extends PgGenericDao<Discipline, Integer> implements DisciplineDao {

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM disciplines WHERE discipline_id= ?;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM disciplines;";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO courses (title) VALUES (?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE disciplines SET title= ? WHERE discipline_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM disciplines WHERE discipline_id= ?;";
    }

    @Override
    protected List<Discipline> parseResultSet(ResultSet rs) throws PersistentException {
        List<Discipline> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Discipline discipline = new Discipline();
                discipline.setId(rs.getInt("discipline_id"));
                discipline.setTitle(rs.getString("title"));
                result.add(discipline);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Discipline object) throws PersistentException {
        try {
            statement.setString(1, object.getTitle());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Discipline object) throws PersistentException {
        try {
            statement.setString(1, object.getTitle());
            statement.setInt(2, object.getId());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }
}
