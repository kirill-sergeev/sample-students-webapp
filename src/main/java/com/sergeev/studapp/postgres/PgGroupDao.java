package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.postgres.PgConstants.*;

public class PgGroupDao extends PgGenericDao<Group> implements GroupDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgGroupDao.class);

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM groups Where group_id= ?;";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM groups";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO groups (title) VALUES (?);";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE groups SET title= ? WHERE group_id= ?;";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM groups WHERE group_id= ?;";
    }

    @Override
    protected List<Group> parseResultSet(ResultSet rs) throws PersistentException {
        List<Group> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getInt(GROUP_ID));
                group.setTitle(rs.getString(TITLE));
                result.add(group);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Group object) throws PersistentException {
        try {
            statement.setString(1, object.getTitle());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Group object) throws PersistentException {
        try {
            statement.setString(1, object.getTitle());
            statement.setInt(2, object.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }
}
