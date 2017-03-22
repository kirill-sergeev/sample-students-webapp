package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PgGroupDao extends PgGenericDao<Group> implements GroupDao {

    protected static final String GROUP_ID = "group_id";
    protected static final String GROUP_TITLE = "title";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM groups Where group_id= ?;";
    }

    public String getSelectAllQuery() {
        return "SELECT * FROM groups";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO groups (title) VALUES (?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE groups SET title= ? WHERE group_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM groups WHERE group_id= ?;";
    }

    @Override
    protected List<Group> parseResultSet(ResultSet rs) throws PersistentException {
        List<Group> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getString(GROUP_ID));
                group.setTitle(rs.getString(GROUP_TITLE));
                result.add(group);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Group object) throws PersistentException {
        try {
            statement.setString(1, object.getTitle());
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Group object) throws PersistentException {
        try {
            statement.setString(1, object.getTitle());
            statement.setInt(2, Integer.parseInt(object.getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }
}
