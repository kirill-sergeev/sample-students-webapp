package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgDisciplineDao extends PgGenericDao<Discipline> implements DisciplineDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgDisciplineDao.class);

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM disciplines WHERE discipline_id= ?;";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM disciplines ORDER BY title;";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO disciplines (title) VALUES (?);";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE disciplines SET title= ? WHERE discipline_id= ?;";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM disciplines WHERE discipline_id= ?;";
    }

    @Override
    protected List<Discipline> parseResultSet(ResultSet rs) {
        List<Discipline> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Discipline discipline = new Discipline();
                discipline.setId(rs.getInt(DISCIPLINE_ID));
                discipline.setTitle(rs.getString(TITLE));
                result.add(discipline);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Discipline object) {
        try {
            statement.setString(1, object.getTitle());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Discipline object) {
        try {
            statement.setString(1, object.getTitle());
            statement.setInt(2, object.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }
}
