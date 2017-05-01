package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;


public class PgDisciplineDao extends PgGenericDao<Discipline> implements DisciplineDao {

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM disciplines WHERE id = ?";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM disciplines ORDER BY title";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO disciplines (title) VALUES (?)";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE disciplines SET title = ? WHERE id = ?";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM disciplines WHERE id = ?";
    }

    @Override
    protected List<Discipline> parseResultSet(ResultSet rs, Connection con) {
        List<Discipline> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Discipline discipline = new Discipline()
                        .setId(rs.getInt(ID))
                        .setTitle(rs.getString(TITLE));
                list.add(discipline);
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
    protected void prepareStatementForInsert(PreparedStatement st, Discipline discipline) {
        try {
            st.setString(1, discipline.getTitle());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Discipline discipline) {
        try {
            st.setString(1, discipline.getTitle());
            st.setInt(2, discipline.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

}
